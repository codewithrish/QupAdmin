package app.qup.authentication.presentation.login

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.qup.authentication.R
import app.qup.authentication.common.MOBILE_NUMBER_LENGTH
import app.qup.authentication.data.remote.dto.request.SignInRequestDto
import app.qup.authentication.databinding.FragmentLoginBinding
import app.qup.ui.common.hideKeyboard
import app.qup.ui.common.safeNavigate
import app.qup.ui.common.snack
import app.qup.util.common.UserRole
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

    private val loginViewModel by viewModels<LoginViewModel>()

    private var mobileNumber: String = ""
    private var cbChecked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFunctions()
    }
    private fun callFunctions() {
        logoSetup()
        mobileNumberTextChangeListener()
        termsAndConditions()
        login()
    }

    private fun logoSetup() {
        binding.imgLogo.setImageResource(when (resources.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> app.qup.ui.R.drawable.img_qup_dark
            Configuration.UI_MODE_NIGHT_NO -> app.qup.ui.R.drawable.img_qup
            Configuration.UI_MODE_NIGHT_UNDEFINED -> app.qup.ui.R.drawable.img_qup
            else -> app.qup.ui.R.drawable.img_qup
        })
    }

    private fun login() {
        loginViewModel.validForm.observe(viewLifecycleOwner) { isValid ->
            binding.btnLogin.isEnabled = isValid
        }
        binding.btnLogin.setOnClickListener {
            loginViewModel.login(
                mobileNumber = mobileNumber,
                signInRequestDto = SignInRequestDto(
                    requestedRole = UserRole.SUPER_ADMIN.name
                )
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.login.collectLatest {
                    it.signInInfo?.let { signInInfo ->
                        val action = LoginFragmentDirections.actionLoginFragmentToOtpFragment(
                            mobileNumber = mobileNumber,
                            signInInfo = signInInfo
                        )
                        navController?.safeNavigate(action)
                    }
                    it.error?.let { error ->
                        if (error.isNotEmpty()) {
                            binding.root.snack(error)
                        }
                    }
                    it.isLoading.let {  isLoading ->
                        binding.progressBar.isVisible = isLoading
                    }
                }
            }
        }
    }
    private fun mobileNumberTextChangeListener() {
        binding.etMobileNumber.addTextChangedListener {
            val input = it.toString()
            mobileNumber = input
            if (input.length == MOBILE_NUMBER_LENGTH) {
                binding.etMobileNumber.hideKeyboard()
            }
            loginViewModel.validateLoginForm(mobileNumber = mobileNumber, cbChecked = cbChecked)
        }
    }
    private fun termsAndConditions() {
        binding.cbTermsConditions.setOnCheckedChangeListener { _, isChecked ->
            cbChecked = isChecked
            loginViewModel.validateLoginForm(mobileNumber = mobileNumber, cbChecked = cbChecked)
        }
        // Decorate Text
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.cancelPendingInputEvents()
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_conditions_url))))
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.cancelPendingInputEvents()
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url))))
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        val termsConditions = SpannableString("Terms, Conditions")
        val privacyPolicy = SpannableString("Privacy Policy")
        termsConditions.setSpan(clickableSpan, 0, termsConditions.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        privacyPolicy.setSpan(clickableSpan1, 0, privacyPolicy.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val cs = TextUtils.expandTemplate("I agree to the ^1 & ^2", termsConditions, privacyPolicy)
        binding.cbTermsConditions.text = cs
        binding.cbTermsConditions.movementMethod = LinkMovementMethod.getInstance()
    }
}