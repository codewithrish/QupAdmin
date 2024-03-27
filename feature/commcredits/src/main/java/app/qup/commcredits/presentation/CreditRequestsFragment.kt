package app.qup.commcredits.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.qup.commcredits.common.CREDITS_APPROVED
import app.qup.commcredits.databinding.FragmentCreditRequestsBinding
import app.qup.commcredits.domain.model.NotificationRechargeRequestModel
import app.qup.commcredits.domain.model.SmsRechargeRequestModel
import app.qup.ui.common.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreditRequestsFragment : Fragment(), MenuProvider {
    private var _binding: FragmentCreditRequestsBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

    private val creditRequestsViewModel by viewModels<CreditRequestsViewModel>()

    private lateinit var smsAdapter: SmsRechargeRequestAdapter
    private lateinit var notificationAdapter: NotificationRechargeRequestAdapter

    private val smsRequests: MutableList<SmsRechargeRequestModel> = mutableListOf()
    private val notificationRequests: MutableList<NotificationRechargeRequestModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        callFunctions()
    }

    private fun callFunctions() {
        toggleListener()
        searchListener()
        binding.tgCommType.check(binding.optionSms.id)
        navController?.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            CREDITS_APPROVED
        )?.observe(viewLifecycleOwner) { creditsApproved ->
            if (creditsApproved) {
                loadCreditRequests()
            }
        }
    }

    private fun searchListener() {
        binding.etDoctorEntity.addTextChangedListener {
            val query = it.toString().trim().lowercase()
            when (binding.tgCommType.checkedButtonId) {
                binding.optionSms.id ->
                    if (this::smsAdapter.isInitialized)
                        smsAdapter.submitList(if (query.isEmpty()) smsRequests else smsRequests.filter { it1 ->
                            it1.doctorName.trim().lowercase().contains(query) ||
                            it1.entityName.trim().lowercase().contains(query) }
                        )

                binding.optionNotification.id ->
                    if (this::notificationAdapter.isInitialized)
                        notificationAdapter.submitList(if (query.isEmpty()) notificationRequests else notificationRequests.filter { it1 ->
                            it1.doctorName.trim().lowercase().contains(query) ||
                            it1.entityName.trim().lowercase().contains(query) }
                        )
            }
        }
    }

    private fun toggleListener() {
        binding.tgCommType.addOnButtonCheckedListener { _, _, isChecked ->
            if (isChecked) {
                binding.etDoctorEntity.setText("")
                loadCreditRequests()
            }
        }
    }

    private fun loadCreditRequests() {
        when (binding.tgCommType.checkedButtonId) {
            binding.optionSms.id -> creditRequestsViewModel.getSmsCreditRequests()
            binding.optionNotification.id -> creditRequestsViewModel.getNotificationCreditRequests()
        }
        creditRequestsViewModel.smsCreditRequests.observe(viewLifecycleOwner) {
            it.smsRechargeRequestModels?.let { smsRequests ->
                this.smsRequests.clear()
                this.smsRequests.addAll(smsRequests)
                setupSmsAdapter()
                smsAdapter.submitList(smsRequests)
            }
            binding.rvCreditRequests.isVisible = !it.isLoading
            binding.progressBar.isVisible = it.isLoading
        }
        creditRequestsViewModel.notificationCreditRequests.observe(viewLifecycleOwner) {
            it.notificationRechargeRequestModels?.let { notificationRequests ->
                this.notificationRequests.clear()
                this.notificationRequests.addAll(notificationRequests)
                setupNotificationAdapter()
                notificationAdapter.submitList(notificationRequests)
            }
            binding.rvCreditRequests.isVisible = !it.isLoading
            binding.progressBar.isVisible = it.isLoading
        }
    }

    private fun setupSmsAdapter() {
        smsAdapter = SmsRechargeRequestAdapter()
        binding.rvCreditRequests.adapter = smsAdapter
        smsAdapter.onPaymentClick = {
            val action = CreditRequestsFragmentDirections.actionCreditRequestsFragmentToPaymentFragment(
                smsRequest = it
            )
            navController?.safeNavigate(action)
        }
        smsAdapter.onAdminNotesClick = {
            if (it.adminNotes.isNotEmpty()) {
                val action = CreditRequestsFragmentDirections.actionCreditRequestsFragmentToAdminNotesFragment(
                    adminNote = it.adminNotes[0]
                )
                navController?.safeNavigate(action)
            }
        }
        smsAdapter.onApproveCreditsClick = {
            val action = CreditRequestsFragmentDirections.actionCreditRequestsFragmentToApproveCreditsFragment(
                smsRequest = it
            )
            navController?.safeNavigate(action)
        }
    }

    private fun setupNotificationAdapter() {
        notificationAdapter = NotificationRechargeRequestAdapter()
        binding.rvCreditRequests.adapter = notificationAdapter
        notificationAdapter.onPaymentClick = {
            val action = CreditRequestsFragmentDirections.actionCreditRequestsFragmentToPaymentFragment(
                notificationRequest = it
            )
            navController?.safeNavigate(action)
        }
        notificationAdapter.onAdminNotesClick = {
            if (it.adminNotes.isNotEmpty()) {
                val action = CreditRequestsFragmentDirections.actionCreditRequestsFragmentToAdminNotesFragment(
                    adminNote = it.adminNotes[0]
                )
                navController?.safeNavigate(action)
            }
        }
        notificationAdapter.onApproveCreditsClick = {
            val action = CreditRequestsFragmentDirections.actionCreditRequestsFragmentToApproveCreditsFragment(
                notificationRequest = it
            )
            navController?.safeNavigate(action)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                navController?.popBackStack()
                true
            }
            else -> false
        }
    }
}