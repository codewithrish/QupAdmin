package app.qup.commcredits.presentation.recharge_entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.qup.commcredits.common.CREDITS_APPROVED
import app.qup.commcredits.data.remote.dto.request.TopUpSmsRequestDto
import app.qup.commcredits.databinding.FragmentAddRechargeBinding
import app.qup.ui.common.setWidthPercent
import app.qup.util.common.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRechargeFragment : DialogFragment() {
    private var _binding: FragmentAddRechargeBinding? = null
    private val binding get() = _binding!!

    private val addRechargeViewModel by viewModels<AddRechargeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRechargeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWidthPercent(100)
        callFunctions()
    }

    private fun callFunctions() {
        cancelClick()
        closeDialog()
        addRechargeEntry()
    }

    private fun addRechargeEntry() {
        binding.btnUpdate.setOnClickListener {
            val amount = binding.etRechargeAmount.text.toString()
            if (amount.isNotEmpty()) {
                addRechargeViewModel.topPupSmsCredits(
                    topUpSmsRequestDto = TopUpSmsRequestDto(
                        topUpSMSCreditsNumber = amount.toInt()
                    )
                )
            } else {
                requireContext().makeToast("Enter Some Amount")
            }
        }
        addRechargeViewModel.topPupSmsCredits.observe(viewLifecycleOwner) {
            it.topUpSms?.let {
                requireContext().makeToast("Recharge Successful")
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