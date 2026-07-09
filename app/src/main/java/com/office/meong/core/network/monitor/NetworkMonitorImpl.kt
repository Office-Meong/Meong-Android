package com.office.meong.core.network.monitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import java.util.Collections
import javax.inject.Inject

class NetworkMonitorImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : NetworkMonitor {

    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            // 여러 네트워크 상태가 동시 변경될 때 ConcurrentModificationException 발생 위험응로 인해 synchronized 사용
            private val validNetworks = Collections.synchronizedSet(mutableSetOf<Network>())

            override fun onAvailable(network: Network) {
                validNetworks += network
                channel.trySend(validNetworks.isNotEmpty())
            }

            // 네트워크 연결 자체는 유지되지만 인터넷만 끊기는 경우같은 변경 상태 감지용
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val isValidated = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                if (isValidated) {
                    validNetworks += network
                } else {
                    validNetworks -= network
                }
                channel.trySend(validNetworks.isNotEmpty())
            }

            override fun onLost(network: Network) {
                validNetworks -= network
                channel.trySend(validNetworks.isNotEmpty())
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) // 실제 통신이 되는지
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, callback)
        } catch (e: SecurityException) {
            channel.trySend(false)
            channel.close(e)
            return@callbackFlow
        }

        awaitClose {
            try {
                connectivityManager.unregisterNetworkCallback(callback)
            } catch (e: IllegalArgumentException) {
                // 이미 해제됐거나 등록되지 않은 콜백을 해제하려 할 때의 크래시 방지
                Timber.e("NetworkMonitor unregisterNetworkCallback  에러: ${e.message}")
            }
        }
    }
        .distinctUntilChanged() // 네트워크가 여러개 연결되어 같은 값을 연속 방출할 때를 막음
        .conflate()
}
