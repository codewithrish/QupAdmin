package app.qup.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import app.qup.home.databinding.FragmentHomeBinding
import app.qup.ui.common.safeNavigate


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController? by lazy { view?.findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFunctions()
    }

    private fun callFunctions() {
        navigate()
    }

    private fun navigate() {
        binding.btnDoctor.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDoctorGraph()
            navController?.safeNavigate(action)
        }
        binding.btnReception.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToReceptionGraph()
            navController?.safeNavigate(action)
        }
        binding.btnSrkManager.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSrkGraph()
            navController?.safeNavigate(action)
        }
        binding.btnEntityManager.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEntityGraph()
            navController?.safeNavigate(action)
        }
        binding.btnQueueSummary.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSummaryGraph()
            navController?.safeNavigate(action)
        }
        binding.btnCommCredits.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCommGraph()
            navController?.safeNavigate(action)
        }
    }
}