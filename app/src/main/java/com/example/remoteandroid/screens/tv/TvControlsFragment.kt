package com.example.remoteandroid.screens.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.discovery.DiscoveryManager.PairingLevel
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.TvControlsFragmentBinding
import com.example.remoteandroid.screens.tv.models.ButtonId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvControlsFragment : Fragment(R.layout.tv_controls_fragment) {

    private lateinit var binding: TvControlsFragmentBinding
    private val viewModel: TvControlsViewModel by viewModels()

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
        initDiscoveryManager()
        setUpRecycler()
    }

    private fun initDiscoveryManager() {

        DiscoveryManager.getInstance().apply {
            println()
        }
    }

    private val remoteAdapter by lazy {
        TvControlsAdapter(onButtonClicked = ::onButtonClicked)
    }

    private fun onButtonClicked(buttonId: ButtonId) {
        viewModel.onButtonClicked(buttonId)
    }

    private fun setUpRecycler() = with(binding.tvControlsRecycler) {
        layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically() = false
        }
        adapter = remoteAdapter
        itemAnimator = null
    }
}