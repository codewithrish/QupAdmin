package app.qup.admin

import androidx.lifecycle.ViewModel
import app.qup.util.common.JWT_ACCESS_TOKEN_PREF
import app.qup.util.common.QupSharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val qupSharedPrefManager: QupSharedPrefManager
): ViewModel() {
    fun getAccessToken(): String? = qupSharedPrefManager.getStringValue(JWT_ACCESS_TOKEN_PREF)
}