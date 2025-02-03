package com.example.remoteandroid.screens.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.discovery.DiscoveryManager.PairingLevel
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.RemoteFragmentBinding
import com.example.remoteandroid.screens.remote.models.MouseEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RemoteFragment : Fragment(R.layout.remote_fragment) {

    private lateinit var binding: RemoteFragmentBinding
    private val viewModel: RemoteViewModel by viewModels()

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
        initDiscoveryManager()
        setUpRecycler()
        viewModel.onCreateView()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contentViewState.collect { uiState ->
                    remoteAdapter.submitData(uiState.content)
                }
            }
        }
    }

    private fun initDiscoveryManager() {
        DiscoveryManager.getInstance().apply {
            registerDefaultDeviceTypes()
            pairingLevel = PairingLevel.ON
            start()
        }
    }

    private val remoteAdapter by lazy {
        RemoteAdapter(
            onDeviceClicked = {
                viewModel.connectToDevice(it)
            },
            onMouseEvent = viewModel::handleMouseEvent
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