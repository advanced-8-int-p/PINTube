package com.example.pintube.ui.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintube.databinding.FragmentMypageBinding
import com.example.pintube.ui.Search.SearchActivity
import com.example.pintube.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.handleCoroutineException

class MypageFragment : Fragment() {

    private lateinit var mContext: Context

    private var _binding: FragmentMypageBinding? = null

    private val binding get() = _binding!!

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var adapter: MypageAdapter

//    private var pinGroup: MutableList<String> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mypageViewModel =
            ViewModelProvider(this).get(MypageViewModel::class.java)

        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        mypageViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }


        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupListeners()
        setResultSignUp()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        val mItems = mutableListOf<MypageViewType>()
        adapter = MypageAdapter(mContext, mItems)
        binding.rvMypageList.adapter = adapter
        binding.rvMypageList.layoutManager = LinearLayoutManager(mContext)
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
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            }

        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val displayName = account?.displayName.toString()
            val photoUrl = account?.photoUrl.toString()
        } catch (e : ApiException) {
            Log.w("failed", "SignInResult:FailedCode = ${e.statusCode}")
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(MainActivity()) {

            }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(MainActivity()) {

            }
    }

    private fun getCurrentUserProfile() {
        val curUser = GoogleSignIn.getLastSignedInAccount(requireContext())
        curUser?.let {
            val email = curUser.email.toString()
            val displayName = curUser.displayName.toString()
            val photoUrl = curUser.photoUrl.toString()
        }
    }

}