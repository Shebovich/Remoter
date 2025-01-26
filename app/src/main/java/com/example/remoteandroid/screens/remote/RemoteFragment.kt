package com.example.remoteandroid.screens.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.RemoteFragmentBinding

class RemoteFragment : Fragment(R.layout.remote_fragment) {

    private lateinit var binding: RemoteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RemoteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()
    }

    private val remoteAdapter by lazy {
        RemoteAdapter()
    }

    private fun setUpRecycler() = with(binding.remoteRecycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = remoteAdapter
        itemAnimator = null
    }
}