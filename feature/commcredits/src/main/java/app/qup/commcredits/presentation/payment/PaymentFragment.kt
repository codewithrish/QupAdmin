package app.qup.commcredits.presentation.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.qup.commcredits.databinding.FragmentPaymentBinding
import app.qup.ui.common.setWidthPercent


class PaymentFragment : DialogFragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<PaymentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWidthPercent(100)
        callFunctions()
    }

    private fun callFunctions() {
        args.smsRequest?.let {
            binding.etAmount.setText(it.noOfSMSCredits.toString())
        }
        args.notificationRequest?.let {
            binding.etAmount.setText(it.noOfNotificationCredits.toString())
        }
        cancelClick()
        closeDialog()
    }

    private fun cancelClick() {
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun closeDialog() {
        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}