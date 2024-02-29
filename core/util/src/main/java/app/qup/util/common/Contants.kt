package app.qup.util.common

import app.qup.util.BuildConfig


// val BASE_URL = "https://api.qupdev.com/"
val BASE_URL = if (BuildConfig.DEBUG) "http://68.183.83.230:8765/" else "https://api.qupdev.com/"

const val QUP_SHARED_PREFERENCE = "QUP_SHARED_PREFERENCE"
const val QUP_DATASTORE_PREFS = "QUP_DATASTORE_PREFS"
const val JWT_ACCESS_TOKEN_PREF = "JWT_ACCESS_TOKEN_PREF"
const val JWT_REFRESH_TOKEN_PREF = "JWT_REFRESH_TOKEN_PREF"
const val CLIENT_USERNAME = "qup-mobile"
const val CLIENT_PASSWORD = "mob@46\$qup"
const val USER_MOBILE_NUMBER = "USER_MOBILE_NUMBER"