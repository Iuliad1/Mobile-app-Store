package com.example.store2.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.store2.R
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomDialog(onSendClick: (String)-> Unit){
    val dialog = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.reset_pass_window,null)
    dialog.setContentView(view)
    dialog.show()

   val editEmail = view.findViewById<EditText>(R.id.ResetEdit)
   val bSend = view.findViewById<Button>(R.id.ResetSendButton)
   val bCancel = view.findViewById<Button>(R.id.ResetCancelButton)

    bSend.setOnClickListener{
        val email = editEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    bCancel.setOnClickListener{
        dialog.dismiss()
    }

}