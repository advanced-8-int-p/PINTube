package com.example.pintube.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val onVideoClick = { item: VideoItemData ->
        findNavController().navigate(
            resId = R.id.action_navigation_home_to_navigation_detail,
            args = Bundle().apply {
                putString("video_id", item.id)
            })
    }
    //ddd
    private val categoryEditDialogAdapter = CategoryEditDialogAdapter { category ->
        viewModel.removeFromCategories(category)
    }
    private val homeAdapter = HomeAdapter(
        onCategorySettingClick = {
            binding.clHomeDialogCategoryBackground.isVisible = true
        },
        onVideoClick = onVideoClick
    )
    private val popularVideoAdapter = PopularVideoAdapter(
        onItemClick = onVideoClick,
        onBookmarkClick = { item ->
            if (item.isSaved.not()) {
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

        binding.ivHomeSearch.setOnClickListener {
            startActivity(Intent(requireContext(),SearchActivity::class.java))
        }

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

        b.rvDialogCategoryEditList.adapter = categoryEditDialogAdapter

        b.ivHomeSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        val imm = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // 레이아웃 뒤로 클릭 이벤트를 막는 코드
        b.clHomeDialogCategoryBackground.setOnTouchListener { _, _ -> true }
        b.btnTvDialogCategoryEditClose.setOnClickListener {
            b.clHomeDialogCategoryBackground.isVisible = false
        }
        b.btnTvDialogCategoryEditAdd.setOnClickListener {
            b.clHomeDialogCategoryEdit.isVisible = false
            b.clHomeDialogCategoryAdd.isVisible = true

            val et = b.etDialogCategoryAddInput
            et.post {
                et.isFocusableInTouchMode = true
                et.requestFocus()
                imm.showSoftInput(et, 0)
            }
        }
        b.btnTvDialogCategoryAddBack.setOnClickListener {
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            b.clHomeDialogCategoryAdd.isVisible = false
            b.clHomeDialogCategoryEdit.isVisible = true
        }

        val onClickListenerOfBtnTvDialogCategoryAddOk = View.OnClickListener {
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            b.clHomeDialogCategoryEdit.isVisible = true
            b.clHomeDialogCategoryAdd.isVisible = false

            val input = b.etDialogCategoryAddInput.text.toString()
            if (input.isNotBlank()) {
                if (viewModel.categories.value!!.contains(input)) {
                    Toast.makeText(
                        requireContext(), "이미 존재하는 카테고리입니다.", Toast.LENGTH_SHORT
                    ).show()
                } else viewModel.addToCategories(input)
            }
            b.etDialogCategoryAddInput.text = null
        }
        b.btnTvDialogCategoryAddOk.setOnClickListener(onClickListenerOfBtnTvDialogCategoryAddOk)
        b.etDialogCategoryAddInput.setOnEditorActionListener { v, _, _ ->
            onClickListenerOfBtnTvDialogCategoryAddOk.onClick(v)
            true
        }

        b.rvHomeMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                val lastPosition = recyclerView.adapter!!.itemCount - 1

                if (lastVisiblePosition == lastPosition) {
                    categoryAdapter.selectedItem?.let { viewModel.categoryNextSearch(it) }
                }
            }
        })
    }

    private fun initViewModel() = viewModel.also { vm ->

        vm.populars.observe(viewLifecycleOwner) {
            popularVideoAdapter.submitList(it)
            Log.d("pop", "$it")  //ddd
        }
        vm.categories.observe(viewLifecycleOwner) {
            categoryAdapter.submitList(it)
            homeAdapter.tvCategoryEmptyText?.isVisible = it.isEmpty()
            categoryEditDialogAdapter.submitList(it)
            binding.tvDialogCategoryEditEmptyText.isVisible = it.isEmpty()
        }
        vm.categoryVideos.observe(viewLifecycleOwner) {
            // TODO: 리스트 어댑터로 변경
            homeAdapter.sealedMultis = homeAdapter.sealedMultis.subList(0, 3).apply {
                addAll(it.map { v -> SealedMulti.Video(v) })
            }
            if (it != null) {
                homeAdapter.sealedMultis.add(SealedMulti.Loading)
            }
            homeAdapter.notifyDataSetChanged()
        }
    }
}
