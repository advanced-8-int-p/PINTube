package com.example.pintube.ui.home

import android.os.Bundle
import android.util.Log
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
    private val homeAdapter by lazy { HomeAdapter() }
    private val popularVideoAdapter = PopularVideoAdapter(
        onItemClick = { view, position -> }
    )
    private val categoryAdapter = CategoryAdapter(
        onItemClick = { view, position -> }
    )
    private val categoryVideoAdapter = CategoryVideoAdapter(
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
        //ddd
        popularVideoAdapter.items.addAll(List(10) { VideoItemData() })
//        categoryAdapter.submitList(List(10) { "카테고리$it" })
        categoryVideoAdapter.items.addAll(List(10) { VideoItemData() })

        b.rvHomeMain.adapter = homeAdapter
        homeAdapter.sealedMultis.addAll(
            listOf(
                SealedMulti.Header,
                SealedMulti.Popular(popularVideoAdapter),
                SealedMulti.Category(categoryAdapter, categoryVideoAdapter),
            )
        )
    }

    private fun initViewModel() = with(viewModel){
        //ddd
        addAllToCategories(List(10) { "카테고리$it" } )
        // TODO: 터짐
        updatePopulars()

        // 이건 왜 안되지..
//        dddSearch("아이유shorts")

        categories.observe(viewLifecycleOwner) {
            categoryAdapter.submitList(it)
        }
        populars.observe(viewLifecycleOwner) {
            popularVideoAdapter.items = it
            Log.d("pop", "$it")
        }
    }
}
