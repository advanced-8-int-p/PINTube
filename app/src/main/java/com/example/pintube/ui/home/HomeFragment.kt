package com.example.pintube.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pintube.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    //ddd
    val homeAdapter by lazy { HomeAdapter(requireContext()) }
    val popularVideoAdapter = PopularVideoAdapter(
        onItemClick = { view, position -> }
    )
    val categoryAdapter = CategoryAdapter(
        onItemClick = { view, position -> }
    )
    val categoryVideoAdapter = CategoryVideoAdapter(
        onItemClick = { view, position -> }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = binding.also { b ->
        b.rvHomeMain.adapter = homeAdapter
        homeAdapter.datas.addAll(
            listOf(
                MultiData(type = MULTI_POPULAR),
                MultiData(type = MULTI_CATEGORY),
                MultiData(),
            )
        )
    }
}
