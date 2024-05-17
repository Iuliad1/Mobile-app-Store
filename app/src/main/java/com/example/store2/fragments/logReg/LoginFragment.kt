package com.example.store2.fragments.logReg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.store2.R;
import com.example.store2.activites.ShoppingActivity
import com.example.store2.databinding.FragmentLoginBinding
import com.example.store2.dialog.setupBottomDialog
import com.example.store2.util.Resource
import com.example.store2.viewmodel.LogViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {
    private  lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LogViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.regRedirectText.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            binding.loginButton.setOnClickListener{
                val email = loginEmail.text.toString().trim()
                val password = loginPassword.text.toString()
                viewModel.login(email,password)
            }
        }

        binding.logForgotText.setOnClickListener{
        setupBottomDialog {email ->
            viewModel.resetPass(email)
          }
        }

        lifecycleScope.launch {
            viewModel.resetPass.collect{
                when(it){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Snackbar.make(requireView(),"Reset link was sent to your email", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(),"Error: ${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launch {
            viewModel.log.collect{
                when(it){
                    is Resource.Loading -> {
                       binding.loginButton.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.loginButton.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        binding.loginButton.revertAnimation()
                    }
                    else -> Unit
                    }
                }
            }
        }
    }
Folosirea FirebaseAuth pentru a crea un cont nou cu adresa de email și parola introduse.
@AndroidEntryPoint
class AddProductFragment: Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addProductButton.setOnClickListener {
                val product = Product(
                    productName.text.toString().trim(),
                    productDescription.text.toString().trim(),
                    productPrice.text.toString().toDouble()
// Adaugă și alte detalii produs
                )
                viewModel.addProductToDatabase(product)
            }
