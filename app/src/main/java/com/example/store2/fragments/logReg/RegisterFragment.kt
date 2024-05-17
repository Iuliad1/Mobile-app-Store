package com.example.store2.fragments.logReg

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.store2.R
import com.example.store2.data.User
import com.example.store2.databinding.FragmentRegBinding
import com.example.store2.util.RegValidation
import com.example.store2.util.Resource
import com.example.store2.viewmodel.RegViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegBinding
    private val viewModel by viewModels<RegViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginRedirectText.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

        binding.apply {
            signButton.setOnClickListener {
                val user = User(
                    signupName.text.toString().trim(),
                    signupUsername.text.toString().trim(),
                    signupEmail.text.toString().trim()
                )
                val password = signupPassword.text.toString()
                viewModel.createAccWithEmailAndPass(user, password)
            }
        }

        lifecycleScope.launch{
            viewModel.reg.collect {
                when(it) {
                    is Resource.Loading -> {
                        binding.signButton.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.signButton.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.signButton.revertAnimation()
                    }
                    else -> Unit

                }
            }
        }

        lifecycleScope.launch {
            viewModel.valid.collect{ valid ->
                if (valid.email is RegValidation.Failed){
                    withContext(Dispatchers.Main) {
                        binding.signupEmail.apply {
                            requestFocus()
                            error = valid.email.message
                        }
                    }
                }

                if (valid.password is RegValidation.Failed){
                    withContext(Dispatchers.Main) {
                        binding.signupPassword.apply {
                            requestFocus()
                            error = valid.password.message
                        }
                    }
                }
            }
        }
    }
}