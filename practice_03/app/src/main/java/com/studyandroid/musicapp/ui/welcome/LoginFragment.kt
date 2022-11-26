package com.studyandroid.musicapp.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.model.User
import com.studyandroid.musicapp.ui.main.MainActivity
import com.studyandroid.musicapp.viewmodel.UsersViewModel

class LoginFragment : Fragment() {

    private lateinit var usersViewModel: UsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        usersViewModel = ViewModelProvider(this)[UsersViewModel::class.java]

        view.findViewById<TextView>(R.id.login_frag_no_account).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        view.findViewById<Button>(R.id.login_frag_go).setOnClickListener {
            usersViewModel.isUserExists.observe(viewLifecycleOwner) {
                it?.let {
                    usersViewModel.isUserExists.postValue(null)
                    if (it) {
                        val currentUser =
                            view?.findViewById<TextView>(R.id.login_frag_email_edittext)?.text.toString()
                        val sharedPref = activity?.getSharedPreferences(
                            getString(R.string.pref_current_user), Context.MODE_PRIVATE
                        )
                        with(sharedPref?.edit()) {
                            this?.putString(getString(R.string.pref_current_user), currentUser)
                            this?.apply()
                        }

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finishAffinity()
                    } else {
                        Toast.makeText(
                            activity,
                            getString(R.string.err_wrong_email_password),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            checkData()
        }

        return view
    }

    private fun checkData() {
        val email = view?.findViewById<TextView>(R.id.login_frag_email_edittext)
        val password = view?.findViewById<TextView>(R.id.login_frag_password_edittext)

        val emailString = email?.text.toString()
        val passwordString = password?.text.toString()

        if (emailString.isBlank() || passwordString.isBlank()) {
            if (emailString.isBlank()) {
                email?.error = getString(R.string.err_field_cant_be_blank)
            }
            if (passwordString.isBlank()) {
                password?.error = getString(R.string.err_field_cant_be_blank)
            }

            return
        }

        val user = User(email = emailString, password = passwordString)
        usersViewModel.isUserExists(user)
    }
}
