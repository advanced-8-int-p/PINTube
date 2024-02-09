package com.example.pintube.ui.home

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
import com.example.pintube.R
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
    private val homeAdapter by lazy { HomeAdapter() }
    private val popularVideoAdapter = PopularVideoAdapter(
        onItemClick = { item ->
            findNavController().navigate(
                resId = R.id.navigation_detail,
                //args = null,
                args = Bundle().apply {
                    putString("video_id", item.id)
                    putString("channel_id", "bbb")
                    putParcelable("videoItemData", VideoItemData())
                },
                navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, true)
                    .build(),
            )
//            mainMotion.transitionToStart()
        }
    )
    private val categoryAdapter = CategoryAdapter(
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
                    if (position < 3) return manager.spanCount
                    return 1
                }
            }
        }
    }

    private fun initViewModel() = viewModel.also { vm ->
        vm.addAllToCategories(List(10) { "카테고리$it" })  //ddd
        vm.updatePopulars()
//        vm.searchCategory("요리")  //ddd

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
            homeAdapter.notifyDataSetChanged()
        }
    }

}
