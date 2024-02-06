package com.example.pintube.ui.Search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintube.data.model.VideoModel
import com.example.pintube.databinding.FragmentSearchResultBinding


class SearchResultFragment : Fragment() {
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_SEARCH_RESULT = "search_result"

//        fun newInstance(searchResult: ArrayList<VideoModel>): SearchResultFragment {
//            val fragment = SearchResultFragment()
//            val args = Bundle()
//            args.putParcelableArrayList(ARG_SEARCH_RESULT, searchResult)
//            fragment.arguments = args
//            return fragment
//        }
    }

    private var searchResult: ArrayList<VideoModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchAdapter = SearchResultAdapter(requireContext())

        // RecyclerView에 LinearLayoutManager 설정
        binding.rvResult.layoutManager = LinearLayoutManager(requireContext())

        // Adapter를 RecyclerView에 연결
        binding.rvResult.adapter = searchAdapter

        // 전달받은 검색 결과를 Adapter에 설정
//        arguments?.getParcelableArrayList<VideoModel>(ARG_SEARCH_RESULT)?.let { searchResults ->
//            searchAdapter.setSearchResults(searchResults)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}