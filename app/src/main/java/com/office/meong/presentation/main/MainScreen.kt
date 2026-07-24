package com.office.meong.presentation.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.core.designsystem.component.dialog.MeongDialog
import com.office.meong.core.designsystem.component.dialog.action.MeongConfirmAction
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.trigger.DialogTrigger
import com.office.meong.core.model.trigger.GlobalUiEventHolder
import com.office.meong.core.model.trigger.RefreshState
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.core.trigger.LocalRefreshState
import com.office.meong.presentation.main.component.MainBottomBar
import com.office.meong.presentation.main.state.MainAppState
import com.office.meong.presentation.main.state.rememberDialogStateHolder
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun MainContainer(
    appState: MainAppState,
) {
    MainScreen(
        appState = appState
    )
}

@Composable
fun MainScreen(
    appState: MainAppState,
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    val isBottomBarVisible by appState.isBottomBarVisible.collectAsStateWithLifecycle()
    val currentTab by appState.currentTab.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentSnackbarState by remember { mutableStateOf<SnackbarState?>(null) }

    val dialogState = rememberDialogStateHolder()
    val refreshState = remember { RefreshState() }
    val snackbarHostState = remember { SnackbarHostState() }

    val onShowToast: (String) -> Unit = remember {
        { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val onShowSnackbar: (SnackbarState) -> Unit = remember(coroutineScope, snackbarHostState) {
        { state ->
            currentSnackbarState = state
            coroutineScope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()

                val job = launch {
                    snackbarHostState.showSnackbar(
                        message = state.message,
                    )
                }
                job.invokeOnCompletion {
                    if (currentSnackbarState == state) {
                        currentSnackbarState = null
                    }
                }
                delay(2000L.milliseconds)
                job.cancel()
            }
        }
    }

    val eventHolder = remember(dialogState, onShowToast, onShowSnackbar) {
        GlobalUiEventHolder(
            dialogTrigger = DialogTrigger(
                show = { onConfirm ->
                    dialogState.showDialog(onConfirm)
                },
                dismiss = {
                    dialogState.dismissDialog()
                }
            ),
            showToast = onShowToast,
            showSnackbar = onShowSnackbar,
        )
    }

    HandleBackPressToExit(
        onShowToast = {
            onShowToast("버튼을 한번 더 누르면 앱이 종료됩니다.")
        }
    )

    LaunchedEffect(isOffline, dialogState.dialogState.isVisible) {
        if (isOffline && !dialogState.dialogState.isVisible) {
            dialogState.showDialog { dialogState.dismissDialog() }
        }
    }

    CompositionLocalProvider(
        LocalGlobalUiEventTrigger provides eventHolder,
        LocalRefreshState provides refreshState
    ) {
        Scaffold(
            containerColor = MeongTheme.colors.white,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            bottomBar = {
                MainBottomBar(
                    isVisible = isBottomBarVisible,
                    tabs = MainTab.entries.toImmutableList(),
                    currentTab = currentTab,
                    onTabSelected = appState::navigateToTab
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) { innerPadding ->
            MeongNavHost(
                paddingValues = innerPadding,
                appState = appState
            )
        }

        if (dialogState.dialogState.isVisible) {
            MeongDialog(
                title = "인터넷 연결을 확인해주세요!",
                confirmAction = MeongConfirmAction(
                    text = "재시도",
                    onClick = {
                        if (!isOffline) {
                            dialogState.dismissDialog()
                        } else {
                            onShowToast("네트워크가 아직 연결되지 않았어요")
                        }
                    }
                ),
                onDismiss = {
                    if (!isOffline) {
                        dialogState.dismissDialog()
                    }
                }
            )
        }
    }
}

@Composable
private fun HandleBackPressToExit(
    enabled: Boolean = true,
    exitDuration: Long = 2000L,
    onShowToast: () -> Unit = {}
) {
    val activity = LocalActivity.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        if (System.currentTimeMillis() - backPressedTime <= exitDuration) {
            activity?.finish()
        } else {
            onShowToast()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
