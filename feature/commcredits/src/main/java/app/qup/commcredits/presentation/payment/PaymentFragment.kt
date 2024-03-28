package app.qup.commcredits.presentation.payment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.qup.commcredits.common.CREDITS_APPROVED
import app.qup.commcredits.data.remote.dto.request.MarkPaymentDoneRequestDto
import app.qup.commcredits.databinding.FragmentPaymentBinding
import app.qup.commcredits.domain.model.NotificationRechargeRequestModel
import app.qup.commcredits.domain.model.SmsRechargeRequestModel
import app.qup.ui.common.setWidthPercent
import app.qup.util.common.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : DialogFragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val paymentViewModel by viewModels<PaymentViewModel>()
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
        doneClick()
        cancelClick()
        closeDialog()
    }

    private fun doneClick() {
        binding.btnDone.setOnClickListener {
            val amount = binding.etAmount.text.toString().trim()
            if (amount.isNotEmpty()) {
                args.smsRequest?.let {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Are you sure ?")
                    builder.setMessage("You want to mark ${it.doctorName}'s $amount Amount Paid")
                    builder.setPositiveButton("Yes") { _, _ ->
                        markPaymentDone(smsRechargeRequestModel = it)
                    }
                    builder.setNegativeButton("No"){ _, _ -> }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
                args.notificationRequest?.let {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Are you sure ?")
                    builder.setMessage("You want to mark ${it.doctorName}'s $amount Amount Paid")
                    builder.setPositiveButton("Yes") { _, _ ->
                        markPaymentDone(notificationRechargeRequestModel = it)
                    }
                    builder.setNegativeButton("No"){ _, _ -> }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            } else {
                requireContext().makeToast("Enter Some Amount")
            }
        }
    }

    private fun markPaymentDone(smsRechargeRequestModel: SmsRechargeRequestModel? = null, notificationRechargeRequestModel: NotificationRechargeRequestModel? = null) {
        val amount = binding.etAmount.text.toString().trim().ifEmpty { null }
        smsRechargeRequestModel?.let {
            paymentViewModel.makeSmsPaymentPaid(
                requestId = it.rechargeRequestId,
                markPaymentDoneRequestDto = MarkPaymentDoneRequestDto(
                    adminNote = binding.etAdminNote.text.toString().trim().ifEmpty { null },
                    amountPaid = amount?.toInt(),
                )
            )
        }
        notificationRechargeRequestModel?.let {
            paymentViewModel.makeNotificationPaymentPaid(
                requestId = it.rechargeRequestId,
                markPaymentDoneRequestDto = MarkPaymentDoneRequestDto(
                    adminNote = binding.etAdminNote.text.toString().trim().ifEmpty { null },
                    amountPaid = amount?.toInt(),
                )
            )
        }
        paymentViewModel.smsPayment.observe(viewLifecycleOwner) {
            it.success?.let { success ->
                if (success) {
                    requireContext().makeToast("Credits Approved")
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(CREDITS_APPROVED, true)
                    findNavController().popBackStack()
                }
            }
        }
        paymentViewModel.notificationPayment.observe(viewLifecycleOwner) {
            it.success?.let { success ->
                if (success) {
                    requireContext().makeToast("Credits Approved")
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(CREDITS_APPROVED, true)
                    findNavController().popBackStack()
                }
            }
        }
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