package com.example.remoteandroid.screens.tv

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
import com.example.remoteandroid.databinding.TvControlsFragmentBinding
import com.example.remoteandroid.screens.main.MainViewModel
import com.example.remoteandroid.screens.tv.models.ButtonId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvControlsFragment : Fragment(R.layout.tv_controls_fragment) {

    private lateinit var binding: TvControlsFragmentBinding
    private val viewModel: TvControlsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = TvControlsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        viewModel.onViewCreated()
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contentViewState.collect { uiState ->
                    tvControlsAdapter.submitData(uiState.content)
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
    }

    private val tvControlsAdapter by lazy {
        TvControlsAdapter(
            onButtonClicked = ::onButtonClicked,
            onMouseEvent = viewModel::onMouseEvent
        )
    }

    private fun onButtonClicked(buttonId: ButtonId) {
        viewModel.onButtonClicked(buttonId)
    }

    private fun setUpRecycler() = with(binding.tvControlsRecycler) {
        layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically() = false
        }
        adapter = tvControlsAdapter
        itemAnimator = null
    }
}