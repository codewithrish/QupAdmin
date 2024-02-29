package app.qup.authentication.domain.use_case

import javax.inject.Inject

class ValidateOtpFormUseCase @Inject constructor() {
    operator fun invoke(
        otp: String
    ) : Boolean = otp.length == 6
}