package app.qup.entity_management.presentation.search_entity.entity_details

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import app.qup.util.common.makeToast
import com.codewithrish.entity_management.databinding.FragmentEntityDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EntityDetailsFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentEntityDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EntityDetailsFragmentArgs>()
    private val entityDetailsViewModel by viewModels<EntityDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFunctions()
    }

    private fun callFunctions() {
        getEntityDetails()
        copyEntityId()
    }

    private fun copyEntityId() {
        binding.tvEntityId.setOnClickListener {
            val clipboard = ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
            val clip = ClipData.newPlainText("label",binding.tvEntityId.text.toString())
            clipboard.setPrimaryClip(clip)
            requireContext().makeToast("Copied to Clipboard")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getEntityDetails() {
        entityDetailsViewModel.getEntityById(args.entityId)
        entityDetailsViewModel.getEntity.observe(viewLifecycleOwner) {
            it.entity?.let { entity ->
                binding.tvEntityName.text = entity.name
                binding.tvEntityType.text = entity.entityType.type
                binding.tvEntityWebsite.text = entity.website.ifEmpty { "NA" }
                binding.tvEntityAddress.text = "Address1: ${entity.addressLine1.ifEmpty { "NA" }}\n" +
                        "Address2: ${entity.addressLine2.ifEmpty { "NA" }}\n" +
                        "Pincode: ${entity.pincode.ifEmpty { "NA" }}\n" +
                        "City: ${entity.city.ifEmpty { "NA" }}\n" +
                        "State: ${entity.state.ifEmpty { "NA" }}\n" +
                        "Country: ${entity.country.ifEmpty { "NA" }}"
                binding.tvEntityLandline.text = entity.landlineNumber.joinToString { it1 -> "${it1.stdCode}-${it1.landlineNumber}" }.ifEmpty { "NA" }
                binding.tvEntityMobile.text = entity.mobileNumber.joinToString(", ").ifEmpty { "NA" }
                binding.tvEntityEmail.text = entity.email.joinToString(", ").ifEmpty { "NA" }
                binding.tvEntityId.text = entity.entityId
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { layout ->
                val behaviour = BottomSheetBehavior.from(layout)
                setupFullHeight(layout)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
}