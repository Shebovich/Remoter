package com.example.remoteandroid.screens.applications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.ApplicationsFragmentBinding
import com.example.remoteandroid.screens.applications.models.ApplicationsNavigationState
import com.example.remoteandroid.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApplicationsFragment : Fragment(R.layout.applications_fragment) {

    private lateinit var binding: ApplicationsFragmentBinding
    private val viewModel: ApplicationsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ApplicationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
        setUpRecycler()
        viewModel.onViewCreated()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contentViewState.collect { uiState ->
                    remoteAdapter.submitData(uiState.content)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.connectionState.collectLatest { connectionState ->
                    viewModel.onConnectionStateChanged(connectionState)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationState.collectLatest { navigationState ->
                    when(navigationState) {
                        ApplicationsNavigationState.TV_CONTROLS -> navigateToTvScreen()
                        ApplicationsNavigationState.DEFAULT -> Unit
                    }
                }
            }
        }
    }

    private fun navigateToTvScreen() {
        println("Navigated to TV screen")
    }

    private val remoteAdapter by lazy {
        ApplicationsAdapter(
            onApplicationClicked = {
               viewModel.onApplicationClicked(it)
            },
        )
    }

    private fun setUpRecycler() = with(binding.applicationsRecycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = remoteAdapter
        itemAnimator = null
    }
}