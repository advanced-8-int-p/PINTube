package com.example.pintube.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.room.util.query
import com.example.pintube.R
import com.example.pintube.databinding.FragmentHomeBinding
import com.example.pintube.ui.Search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    //ddd
    private val homeAdapter by lazy { HomeAdapter() }
    private val popularVideoAdapter = PopularVideoAdapter(
        onItemClick = { item ->
            findNavController().navigate(
                resId = R.id.action_navigation_home_to_navigation_detail,
                //args = null,
                args = Bundle().apply {
                    putString("video_id", item.id)
                }
            )
//            mainMotion.transitionToStart()
        },
        onBookmarkClick = { item ->
            if (item.isSaved.not()){
                viewModel.addBookmark(item)
            } else {
                viewModel.removeBookmark(item)
            }
        }
    )
    private val categoryAdapter = CategoryAdapter(
        onItemClick = { query -> viewModel.searchCategory(query) }
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
        initViewModel()
    }

    private fun initView() = binding.also { b ->
        homeAdapter.sealedMultis.addAll(
            listOf(
                SealedMulti.Header,
                SealedMulti.Popular(popularVideoAdapter),
                SealedMulti.Category(categoryAdapter),
            )
        )
        b.rvHomeMain.adapter = homeAdapter
        b.rvHomeMain.layoutManager = GridLayoutManager(requireContext(), 2).also { manager ->
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (homeAdapter.sealedMultis[position]) {
                        is SealedMulti.Video -> 1
                        else -> manager.spanCount
                    }
                }
            }
        }

        b.ivHomeSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
    }

    private fun initViewModel() = viewModel.also { vm ->

        vm.populars.observe(viewLifecycleOwner) {
            popularVideoAdapter.submitList(it)
            Log.d("pop", "$it")  //ddd
        }
        vm.categories.observe(viewLifecycleOwner) {
            categoryAdapter.submitList(it)
        }
        vm.categoryVideos.observe(viewLifecycleOwner) {
            // TODO: 리스트 어댑터로 변경
            homeAdapter.sealedMultis = homeAdapter.sealedMultis.subList(0, 3).apply {
                addAll(it.map { v -> SealedMulti.Video(v) })
            }
            if(it != null) {
                homeAdapter.sealedMultis.add(SealedMulti.Loading)
            }
            homeAdapter.notifyDataSetChanged()
        }
    }
}
