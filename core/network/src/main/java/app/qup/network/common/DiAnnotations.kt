package app.qup.network.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnAuthorisedInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class QupInterceptorOkHttpClient