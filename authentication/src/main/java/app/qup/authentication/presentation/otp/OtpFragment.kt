package app.qup.authentication.presentation.otp

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import app.qup.authentication.R
import app.qup.authentication.common.OTP_LENGTH
import app.qup.authentication.databinding.FragmentOtpBinding
import app.qup.ui.common.hideKeyboard
import app.qup.ui.common.safeNavigate
import app.qup.ui.common.snack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class OtpFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<OtpFragmentArgs>()
    private val otpViewModel by viewModels<OtpViewModel>()

    private val navController: NavController? by lazy { view?.findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFunctions()
    }

    private fun callFunctions() {
        logoSetup()
//        loadToken()
        otpTextChangeListener()
        submitOtp()
        displayUserName()
        resendOtp()
    }

    private fun logoSetup() {
        binding.imgLogo.setImageResource(when (resources.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> app.qup.ui.R.drawable.img_qup_dark
            Configuration.UI_MODE_NIGHT_NO -> app.qup.ui.R.drawable.img_qup
            Configuration.UI_MODE_NIGHT_UNDEFINED -> app.qup.ui.R.drawable.img_qup
            else -> app.qup.ui.R.drawable.img_qup
        })
    }

    private fun resendOtp() {
        binding.tvResendOtp.setOnClickListener {
            otpViewModel.resendOtp(args.mobileNumber)
        }
        otpViewModel.resendOtp.observe(viewLifecycleOwner) {
            it.resendOtpInfo?.let {
                binding.tvResendOtp.isVisible = false
                binding.root.snack(getString(R.string.otp_sent_again))
            }
        }
    }
    private fun displayUserName() {
        args.signInInfo.let {
            binding.tvDoctorName.text = getString(R.string.user_name, it.firstName, it.lastName)
        }
    }
//    private fun loadToken() {
//        tokenViewModel.accessToken.observe(viewLifecycleOwner) { token ->
//            if (token != null) {
//                val action = OtpFragmentDirections.actionOtpFragmentToHomeFragment()
//                navController?.safeNavigate(action)
//            }
//        }
//    }
    private fun otpTextChangeListener() {
        binding.etOtp.addTextChangedListener {
            val input = it.toString()
            if (input.length == OTP_LENGTH) {
                binding.etOtp.hideKeyboard()
            }
            otpViewModel.validateOtpForm(otp = input)
        }
    }
    private fun submitOtp() {
        otpViewModel.validForm.observe(viewLifecycleOwner) { isValid ->
            binding.btnOtpSubmit.isEnabled = isValid
        }
        binding.btnOtpSubmit.setOnClickListener {
            otpViewModel.verifyOtp(
                username = args.mobileNumber,
                password = binding.etOtp.text.toString()
            )
        }
        otpViewModel.verifyOtp.observe(viewLifecycleOwner) {
            it.tokens?.let { tokens ->
                runBlocking {
                    otpViewModel.saveAccessToken(tokens.accessToken)
                    otpViewModel.saveRefreshToken(tokens.refreshToken)
                    otpViewModel.saveUserMobileNumber(args.mobileNumber)
                    val action = OtpFragmentDirections.actionOtpFragmentToHomeGraph()
                    navController?.safeNavigate(action)
                }
            }
            it.isLoading.let {  isLoading ->
                binding.progressBar.isVisible = isLoading
            }
            it.error?.let { error ->
                if (error.isNotEmpty()) {
                    binding.root.snack(error)
                }
            }
        }
    }
}