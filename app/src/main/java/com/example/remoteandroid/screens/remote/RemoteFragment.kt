package com.example.remoteandroid.screens.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.RemoteFragmentBinding
import com.example.remoteandroid.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RemoteFragment : Fragment(R.layout.remote_fragment) {

    private lateinit var binding: RemoteFragmentBinding
    private val viewModel: RemoteViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = RemoteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        binding.tvScreenButton.setOnClickListener {
            val action = RemoteFragmentDirections.actionRemoteFragmentToTvControlsFragment()
            findNavController().navigate(action)
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
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
                mainViewModel.discoveryState.collectLatest { discoveryState ->
                    viewModel.onDiscoveryStateChanged(discoveryState)
                }
            }
        }
    }

    private val remoteAdapter by lazy {
        RemoteAdapter(
            onDeviceClicked = {
                println("onDeviceClicked")
                mainViewModel.connectToDevice(it)
            },
            onSearchingTvClicked = {
                mainViewModel.onStart()
            }
        )
    }

    private fun setUpRecycler() = with(binding.remoteRecycler) {
        layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically() = false
        }
        adapter = remoteAdapter
        itemAnimator = null
    }
}