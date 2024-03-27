package app.qup.commcredits.presentation.admin_notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.qup.commcredits.databinding.FragmentAdminNotesBinding
import app.qup.ui.common.setWidthPercent
import app.qup.util.common.millisToDateString


class AdminNotesFragment : DialogFragment() {
    private var _binding: FragmentAdminNotesBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<AdminNotesFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWidthPercent(100)
        callFunctions()
    }

    private fun callFunctions() {
        showData()
        closeDialog()
    }

    private fun showData() {
        args.adminNote.let { adminNote ->
            binding.tvDate.text = millisToDateString(adminNote.noteDate)
            binding.tvStage.text = adminNote.noteAtStage
            binding.tvAdminNote.text = adminNote.notes
        }
    }

    private fun closeDialog() {
        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}