package com.example.pintube.ui.mypage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pintube.databinding.FragmentMypageBinding
import com.example.pintube.ui.Search.SearchActivity
import com.example.pintube.ui.home.VideoItemData
import com.example.pintube.ui.main.MainActivity
import com.example.pintube.ui.mypage.adapter.MypageAdapter
import com.example.pintube.ui.mypage.adapter.RecyclerviewPinnedGroupAdapter
import com.example.pintube.ui.mypage.adapter.RecyclerviewRecentVideoAdapter
import com.example.pintube.ui.mypage.viewtype.MypageVideoItem
import com.example.pintube.ui.mypage.viewtype.MypageViewType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var myProfileData = MypageViewType.MyPageProfile()

    private val onVideoClick = { item: MypageVideoItem ->
        item.id?.let { (activity as MainActivity).initDetailFragment(it) }
    }

    private var isLoggedIn: Boolean = false

    private val recyclerviewRecentVideoAdapter = RecyclerviewRecentVideoAdapter(emptyList(),
        onVideoClick = onVideoClick)
    private val recyclerviewPinnedGroupAdapter = RecyclerviewPinnedGroupAdapter(emptyList(),
        onVideoClick = onVideoClick)

    private val mItems: MutableList<MypageViewType>
        get() {
            return mutableListOf<MypageViewType>(
                MypageViewType.TopHeader,
                MypageViewType.MyPageProfile(
                ),
                MypageViewType.Header("최근 시청 영상", true),
                MypageViewType.RecentItems(recyclerviewRecentVideoAdapter),
                MypageViewType.Header("저장한 동영상", false),
                MypageViewType.PinItems(recyclerviewPinnedGroupAdapter)
            )
        }

    private val viewModel: MypageViewModel by viewModels()

    private val adapter by lazy {
        MypageAdapter(requireContext(),
            mItems,
            onClickLogin = {
                signIn()
            },
            onClickLogOut = {
                signOut()
            })
    }

//    private val recentAdapter by lazy { RecyclerviewRecentVideoAdapter() }

//    private var pinGroup: MutableList<String> = mutableListOf()

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.let {
            isLoggedIn = true
            Snackbar.make(binding.mypageFragment, "로그인 상태입니다", Snackbar.LENGTH_SHORT).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        setupListeners()
        setResultSignUp()
    }

    override fun onResume() {
        viewModel.getRecentView()
        viewModel.getFavorite()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.rvMypageList.adapter = adapter
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestId()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        getCurrentUserProfile()
    }
    private fun initViewModel() = with(viewModel){
        recentView.observe(viewLifecycleOwner) {
            recyclerviewRecentVideoAdapter.updateItem(it)
        }

        favorite.observe(viewLifecycleOwner) {
            recyclerviewPinnedGroupAdapter.updateItem(it)
        }

        currentUser.observe(viewLifecycleOwner) {
            adapter.updateProfile(it)
        }
    }

    private fun setupListeners() {
        binding.ivMypageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivMypageSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setResultSignUp() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode) {
                    Activity.RESULT_OK -> {
                        val task: Task<GoogleSignInAccount> =
                            GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        handleSignInResult(task)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.e("my_page_result_canceled", "Intent extras: ${result.data?.extras}")
                    }
                }

            }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            myProfileData.channelName = account?.displayName.toString()
            myProfileData.channelThumbnail = account?.photoUrl.toString()
        } catch (e: ApiException) {
            Log.w("failed", "SignInResult:FailedCode = ${e.statusCode}")
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
        viewModel.getCurrentUser(myProfileData)
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
    }

    //현재 유저 정보
    private fun getCurrentUserProfile() {
        val curUser = GoogleSignIn.getLastSignedInAccount(requireContext())
        curUser?.let {
            val id = curUser.id.toString()
            val displayName = curUser.displayName.toString()
            val photoUrl = curUser.photoUrl.toString()

            myProfileData.channelId = id
            myProfileData.channelName = displayName
            myProfileData.channelThumbnail = photoUrl

            Log.d("login", id)
            Log.d("login", myProfileData.channelId.toString())
        }
        viewModel.getCurrentUser(myProfileData)
    }

}