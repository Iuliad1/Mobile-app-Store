package com.example.store2.fragments.logReg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.store2.R;
import com.example.store2.databinding.FragmentAccountOptionsBinding
import com.example.store2.databinding.FragmentIntroductionBinding

class AccountOptionsFragment: Fragment(R.layout.fragment_account_options) {
    private lateinit var binding: FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLoginOptions.setOnClickListener{
            findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment)
        }
        binding.buttonRegisterOptions.setOnClickListener{
            findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)
        }
    }
}
