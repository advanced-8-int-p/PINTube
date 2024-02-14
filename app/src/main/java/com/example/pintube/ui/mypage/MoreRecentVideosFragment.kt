package com.example.pintube.ui.mypage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pintube.R
import com.example.pintube.databinding.FragmentMoreRecentVideosBinding
import com.example.pintube.databinding.FragmentMypageBinding

//private const val ARG_PARAM3 = "param1"
//private const val ARG_PARAM4 = "param2"

class MoreRecentVideosFragment : Fragment() {
//    private var param1: String? = null
//    private var param2: String? = null

    private lateinit var mContext: Context

    private var _binding: FragmentMoreRecentVideosBinding? = null

    private val binding get() = _binding!!

    private val mItems: MutableList<RecentItem>
        get() {
            return mutableListOf()
        }


    private val adapter by lazy { MoreRecentVideoAdapter(mContext, mItems) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM3)
//            param2 = it.getString(ARG_PARAM4)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoreRecentVideosBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        binding.rvMoreRecentList.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MoreRecentVideosFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM3, param1)
//                    putString(ARG_PARAM4, param2)
                }
            }
    }
}