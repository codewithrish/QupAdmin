package app.qup.authentication.domain.use_case

import javax.inject.Inject

class ValidateLoginFormUseCase @Inject constructor() {
    operator fun invoke(
        mobileNumber: String,
        cbChecked: Boolean
    ) : Boolean = mobileNumber.length == 10 && cbChecked
}