package com.example.pintube.ui.shorts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pintube.databinding.FragmentShortsBinding
import com.example.pintube.ui.shorts.adapter.ShortsAdapter
import com.example.pintube.ui.shorts.model.ShortsItem
import com.example.pintube.ui.shorts.model.ShortsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShortsFragment : Fragment() {

    private var _binding: FragmentShortsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: ShortsViewModel by viewModels()

    private val adapter: ShortsAdapter by lazy {
        ShortsAdapter(
            onBookmarkChecked = { item ->
                item
            },
            onSharedChecked = { item ->
                item
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShortsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        setPlayer()
    }

    private fun initViewModel() {

    }

    private fun setPlayer() = with(binding) {
        vpShortsViewpager.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}