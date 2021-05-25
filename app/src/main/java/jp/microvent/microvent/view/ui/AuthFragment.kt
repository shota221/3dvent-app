package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import jp.microvent.microvent.R
import jp.microvent.microvent.databinding.FragmentAuthBinding
import jp.microvent.microvent.service.model.User
import jp.microvent.microvent.viewModel.AuthViewModel

class AuthFragment : Fragment(){

    private val viewModel by lazy {
        ViewModelProvider(this, AuthViewModel.Factory(requireActivity().application)).get(AuthViewModel::class.java)
    }

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_auth, container, false)

        binding.apply {
            authViewModel = viewModel
        }

        binding.btNotLogin.setOnClickListener{
            findNavController().navigate(R.id.action_auth_not_login_to_patient_setting)
        }

        binding.btLogin.setOnClickListener{
            findNavController().navigate(R.id.action_auth_login_to_patient_setting)
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}