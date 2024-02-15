package com.example.pintube.ui.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.R
import com.example.pintube.databinding.FragmentSearchResultBinding
import com.example.pintube.domain.entitiy.VideoEntity
import com.example.pintube.ui.detailpage.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModels()

    companion object {
        fun newInstance(searchResults: List<SearchData>?): SearchResultFragment {
            val fragment = SearchResultFragment()
            val args = Bundle().apply {
                putParcelableArrayList("searchResults", ArrayList(searchResults))
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private var searchAdapter: SearchResultAdapter? = null
    private val items = ArrayList<SearchData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchResults = arguments?.getParcelableArrayList<SearchData>("searchResults")
        val contentResult = arguments?.getParcelableArrayList<VideoEntity>("contentResults")

        var viewSelect = resources.getStringArray(R.array.view)
        var viewAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            viewSelect
        )
        binding.searchResultSppiner.adapter = viewAdapter
        binding.searchResultSppiner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val selectedItem = viewSelect[position]

                    when (position) {
                        //
                        0 -> {
                        }
                        //조회수 순
                        1 -> {
                            searchAdapter?.sortByViewCount()
                        }
                        //최근 업로드 순
                        2 -> {
                            searchAdapter?.sortByDescending()
                        }
                        //오래된 순
                        3 -> {
                            searchAdapter?.sortByAscending()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        if (searchResults != null) {
            items.addAll(searchResults)
            searchAdapter?.notifyDataSetChanged() // Notify adapter that data set has changed
        }
        searchAdapter = SearchResultAdapter(items)
        binding.rvResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResult.adapter = searchAdapter
//        DyLHWjU7UBA
        searchAdapter?.itemClick = object : SearchResultAdapter.ItemClick {
            override fun onClick(position: Int) {
                val getItem = items[position]
                val bundle = Bundle().apply {
                    putString("video_id", getItem.channelId)
                }
                Toast.makeText(requireContext(), "클릭", Toast.LENGTH_SHORT).show()
                val detailFragment = DetailFragment()
                detailFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_top, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.rvResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                val lastPosition = recyclerView.adapter!!.itemCount - 1

                if (lastVisiblePosition == lastPosition) {
                    viewModel.nextSearch()
//                    categoryAdapter.selectedItem?.let { viewModel.categoryNextSearch(it) }
                }
            }
        })

        viewModel.searchDataList.observe(viewLifecycleOwner) {
            items.clear()
            items.addAll(it)
            searchAdapter?.notifyDataSetChanged()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}