package app.qup.commcredits.presentation.recharge_entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.commcredits.data.remote.dto.request.TopUpSmsRequestDto
import app.qup.commcredits.domain.use_case.TopUpSmsState
import app.qup.commcredits.domain.use_case.TopUpSmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddRechargeViewModel @Inject constructor(
    private val topUpSmsUseCase: TopUpSmsUseCase,
) : ViewModel() {
    private val _topPupSmsCredits: MutableLiveData<TopUpSmsState> = MutableLiveData()
    val topPupSmsCredits: LiveData<TopUpSmsState>
        get() = _topPupSmsCredits

    fun topPupSmsCredits(
        topUpSmsRequestDto: TopUpSmsRequestDto
    ) {
        topUpSmsUseCase(topUpSmsRequestDto).onEach {
            _topPupSmsCredits.postValue(it)
        }.launchIn(viewModelScope)
    }
}