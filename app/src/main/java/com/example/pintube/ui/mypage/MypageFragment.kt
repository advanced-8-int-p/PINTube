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
import androidx.navigation.fragment.findNavController
import com.example.pintube.databinding.FragmentMypageBinding
import com.example.pintube.ui.Search.SearchActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var myProfileData: MypageProfileData = MypageProfileData(
        myAccountProfileUri = null,
        myAccountName = null,
        myAccountId = null,
    )
//        get() {
//            return MypageProfileData(
//                myAccountProfileUri = null,
//                myAccountName = null,
//                myAccountId = null,
//            )
//        }

    private var isLoggedIn: Boolean = false

    private val mItems: MutableList<MypageViewType>
        get() {
            return mutableListOf<MypageViewType>(
                MypageViewType.TopHeader,
                MypageViewType.MyPageProfile(
                    channelName = "aaa",
                    channelId = "bbb"
                ),
                MypageViewType.Header("최근 시청 영상", true),
                MypageViewType.RecentItems(RecyclerviewRecentVideoAdapter()),
                MypageViewType.Header("저장한 동영상", false),
                MypageViewType.PinItems(RecyclerviewPinnedGroupAdapter())
            )
        }

    private val adapter by lazy { MypageAdapter(requireContext(), mItems) }

//    private val recentAdapter by lazy { RecyclerviewRecentVideoAdapter() }

//    private var pinGroup: MutableList<String> = mutableListOf()

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.let {
            isLoggedIn = true
            Snackbar.make(binding.mypageFragment, "logged in", Snackbar.LENGTH_SHORT).show()
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
        setupListeners()
        setResultSignUp()


    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.rvMypageList.adapter = adapter

//        myProfileData.myAccountProfileUri = null
//        myProfileData.myAccountName = null
//        myProfileData.myAccountId = null


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestId()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        getCurrentUserProfile()

//        binding.also { b ->
//            val textViews = listOf(b.tvMypageChannelName, b.tvMypageChannelId)
//
//            b.ivMypageProfile.load(myProfileData.myAccountProfileUri)
//            //배경..... 유튜브 채널 헤더를 어디서 가져오지 아니 근데 구글 계정마다 다 채널 있지는 않지 않나 음...
//            b.mypageFragment.setBackgroundResource(R.drawable.hamster)
//            b.tvMypageChannelName.text = myProfileData.myAccountName
//            b.tvMypageChannelId.text = myProfileData.myAccountId.toString()
//
////            isLoggedIn = (myProfileData.myAccountId != null)
//
//            Log.d("login", "status= ${isLoggedIn}")
//            if (isLoggedIn) {
//                b.sibtnMypageChannelLogin.isVisible = false
//                b.tvMypageChannelLogout.isVisible = true
//                b.ivMypageProfile.setImageResource(R.drawable.ic_account_empty)
//                textViews.forEach {
//                    it.isVisible = true
//                }
//
//            } else {
//                b.sibtnMypageChannelLogin.isVisible = true
//                b.tvMypageChannelLogout.isVisible = false
//                textViews.forEach {
//                    it.isVisible = false
//                }
//            }
//
//            b.sibtnMypageChannelLogin.setOnClickListener {
//
//                val signInIntent = mGoogleSignInClient.signInIntent
//                startActivityForResult(signInIntent, 0)
//
//                signIn()
//                getCurrentUserProfile()
//
//            }
//
//            b.tvMypageChannelLogout.setOnClickListener {
//                isLoggedIn = false
//                myProfileData.myAccountProfileUri = null
//                myProfileData.myAccountName = null
//                myProfileData.myAccountName = null
//                signOut()
//                revokeAccess()
//            }
//
//            getCurrentUserProfile()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                isLoggedIn = true
                val account = task.getResult(ApiException::class.java)
                myProfileData.myAccountProfileUri = account.photoUrl.toString()
                myProfileData.myAccountName = account.displayName
                myProfileData.myAccountId = account.id
            } catch (e: ApiException) {
                Log.d("googleLogin", e.toString())
            }
        }
    }


    private fun initViewModel() {

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

    private fun setList() {

    }

    private fun setResultSignUp() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(result.data)

                }

            }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val displayName = account?.displayName.toString()
            val photoUrl = account?.photoUrl.toString()
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
//            .addOnCompleteListener(MainActivity()) {
//
//            }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
//            .addOnCompleteListener(MainActivity()) {
//
//            }
    }

    private fun getCurrentUserProfile() {
        val curUser = GoogleSignIn.getLastSignedInAccount(requireContext())
        curUser?.let {
            val id = curUser.id.toString()
            val displayName = curUser.displayName.toString()
            val photoUrl = curUser.photoUrl.toString()

            myProfileData.myAccountId = id
            myProfileData.myAccountName = displayName
            myProfileData.myAccountProfileUri = photoUrl

            Log.d("login", id)
            Log.d("login", myProfileData.myAccountId.toString())
            Log.d("login", displayName)
            Log.d("login", myProfileData.myAccountName.toString())
            Log.d("login", photoUrl)
            Log.d("login", myProfileData.myAccountProfileUri.toString())
            //whyyyyyyyyyyyy.......
        }
    }

}