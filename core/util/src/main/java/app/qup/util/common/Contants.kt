package app.qup.util.common

import app.qup.util.BuildConfig


// val BASE_URL = "https://api.qupdev.com/"
val BASE_URL = if (BuildConfig.DEBUG) "https://dev-business.qupdev.com:8765/" else "https://api.qupdev.com/"
val DEFAULT_LANGUAGE_ID =  if (BASE_URL == "https://api.qupdev.com/") "5af17b7ec6bb84acde661f2d" else "60d5a7e0fb004f000c6ad86a"

const val QUP_SHARED_PREFERENCE = "QUP_SHARED_PREFERENCE"
const val QUP_DATASTORE_PREFS = "QUP_DATASTORE_PREFS"
const val JWT_ACCESS_TOKEN_PREF = "JWT_ACCESS_TOKEN_PREF"
const val JWT_REFRESH_TOKEN_PREF = "JWT_REFRESH_TOKEN_PREF"
const val CLIENT_USERNAME = "qup-mobile"
const val CLIENT_PASSWORD = "mob@46\$qup"
const val USER_MOBILE_NUMBER = "USER_MOBILE_NUMBER"

// Date Time
const val LOCAL_TIME_FORMAT = "hh:mm a"
const val LOCAL_DATE_FORMAT = "dd-MMM-yyyy"
const val LOCAL_DATE_TIME_FORMAT = "dd MMM, yyyy - hh:mm:ss aa"