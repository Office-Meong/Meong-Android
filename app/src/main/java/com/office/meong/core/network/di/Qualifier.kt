package com.office.meong.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthNetwork

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuthNetwork

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoLocalNetwork
