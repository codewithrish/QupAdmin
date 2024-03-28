package app.qup.commcredits.presentation.approve

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
import app.qup.commcredits.data.remote.dto.request.ApproveNotificationCreditsRequestDto
import app.qup.commcredits.data.remote.dto.request.ApproveSmsCreditsRequestDto
import app.qup.commcredits.databinding.FragmentApproveCreditsBinding
import app.qup.commcredits.domain.model.NotificationRechargeRequestModel
import app.qup.commcredits.domain.model.SmsRechargeRequestModel
import app.qup.ui.common.setWidthPercent
import app.qup.ui.common.snack
import app.qup.util.common.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApproveCreditsFragment : DialogFragment() {
    private var _binding: FragmentApproveCreditsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ApproveCreditsFragmentArgs>()
    private val approveCreditsViewModel by viewModels<ApproveCreditsViewModel>()

    var areCreditsApproved: ((Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApproveCreditsBinding.inflate(inflater, container, false)
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
        approveClick()
        cancelClick()
        closeDialog()
    }

    private fun approveClick() {
        binding.btnApprove.setOnClickListener {
            args.smsRequest?.let {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Are you sure ?")
                builder.setMessage("You want to approve ${it.doctorName}'s ${it.noOfSMSCredits} SMS Credits")
                builder.setPositiveButton("Yes") { _, _ ->
                    approveCredits(smsRechargeRequestModel = it)
                }
                builder.setNegativeButton("No"){ _, _ -> }
                val alertDialog = builder.create()
                alertDialog.show()
            }
            args.notificationRequest?.let {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Are you sure ?")
                builder.setMessage("You want to approve ${it.doctorName}'s ${it.noOfNotificationCredits} Notification Credits")
                builder.setPositiveButton("Yes") { _, _ ->
                    approveCredits(notificationRechargeRequestModel = it)
                }
                builder.setNegativeButton("No"){ _, _ -> }
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
    }

    private fun approveCredits(smsRechargeRequestModel: SmsRechargeRequestModel? = null, notificationRechargeRequestModel: NotificationRechargeRequestModel? = null) {
        smsRechargeRequestModel?.let {
            approveCreditsViewModel.approveSmsCredits(
                requestId = it.rechargeRequestId,
                approveSmsCreditsRequestDto = ApproveSmsCreditsRequestDto(
                    adminNote = binding.etAdminNote.text.toString().trim().ifEmpty { null },
                    approvedNoOfSMSCredits = it.noOfSMSCredits
                )
            )
        }
        notificationRechargeRequestModel?.let {
            approveCreditsViewModel.approveNotificationCredits(
                requestId = it.rechargeRequestId,
                approveNotificationCreditsRequestDto = ApproveNotificationCreditsRequestDto(
                    adminNote = binding.etAdminNote.text.toString().trim().ifEmpty { null },
                    approvedNoOfNotificationCredits = it.noOfNotificationCredits
                )
            )
        }
        approveCreditsViewModel.approveSmsCredits.observe(viewLifecycleOwner) {
            it.smsRechargeRequestModel?.let {
                requireContext().makeToast("Credits Approved")
                findNavController().previousBackStackEntry?.savedStateHandle?.set(CREDITS_APPROVED, true)
                findNavController().popBackStack()
            }
        }
        approveCreditsViewModel.approveNotificationCredits.observe(viewLifecycleOwner) {
            it.notificationRechargeRequestModel?.let {
                requireContext().makeToast("Credits Approved")
                findNavController().previousBackStackEntry?.savedStateHandle?.set(CREDITS_APPROVED, true)
                findNavController().popBackStack()
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