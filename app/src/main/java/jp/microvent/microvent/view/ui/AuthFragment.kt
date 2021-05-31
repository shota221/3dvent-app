package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentAuthBinding
import jp.microvent.microvent.viewModel.AuthViewModel
import jp.microvent.microvent.viewModel.TestViewModel
import jp.microvent.microvent.viewModel.util.EventObserver

class AuthFragment : Fragment() {

//    private val authViewModel by lazy {
//        ViewModelProvider(this, AuthViewModel.Factory(requireActivity().application)).get(AuthViewModel::class.java)
//    }

    private val authViewModel by viewModels<AuthViewModel>()

    private lateinit var binding: FragmentAuthBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)

        val viewModel = authViewModel

        binding.apply {
            test = "test"
            authViewModel = viewModel
//            TODO("呼吸器読み取り時点で呼吸器と組織が紐付いている場合はその組織名を表示する")
        }


        binding.tvOrganizationRegistered.text = arguments?.getString("organization_name") ?: "未登録"

        authViewModel.onTransit.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(R.id.action_auth_login_to_patient_setting)
            }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        binding.btNotLogin.setOnClickListener{
//            findNavController().navigate(R.id.action_auth_not_login_to_patient_setting)
//        }

//        binding.btLogin.setOnClickListener{
//            var accountName:String? = binding.etAccountName.text.toString()
//            val password:String? = binding.etPassword.text.toString()
//            if (accountName == "") {
//                accountName = "テスト更新太郎@example"
//            }
//
//            val hasToken = authViewModel.checkUserToken(accountName)
//
//            Log.i("hasTokenTest",hasToken.toString())
////
////            val userToken = authViewModel.getUserToken(accountName,password)
//            findNavController().navigate(R.id.action_auth_login_to_patient_setting)
//        }


    }
}