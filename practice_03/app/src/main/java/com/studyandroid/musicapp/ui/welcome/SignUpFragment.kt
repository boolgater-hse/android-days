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
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.model.User
import com.studyandroid.musicapp.ui.main.MainActivity
import com.studyandroid.musicapp.viewmodel.UsersViewModel

class SignUpFragment : Fragment() {

    private lateinit var usersViewModel: UsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        usersViewModel = ViewModelProvider(this)[UsersViewModel::class.java]

        view.findViewById<Button>(R.id.sign_up_frag_go).setOnClickListener {
            usersViewModel.userInsertionStatus.observe(viewLifecycleOwner) {
                it?.let {
                    usersViewModel.userInsertionStatus.postValue(null)
                    if (it) {
                        val currentUser =
                            view?.findViewById<TextView>(R.id.sign_up_frag_email_edittext)?.text.toString()
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
                            getString(R.string.err_user_already_exists),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            checkDataAndInsert()
        }

        return view
    }

    private fun checkDataAndInsert() {
        val email = view?.findViewById<TextView>(R.id.sign_up_frag_email_edittext)
        val username = view?.findViewById<TextView>(R.id.sign_up_frag_username_edittext)
        val password = view?.findViewById<TextView>(R.id.sign_up_frag_password_edittext)

        val emailString = email?.text.toString()
        val usernameString = username?.text.toString()
        val passwordString = password?.text.toString()

        if (emailString.isBlank() || usernameString.isBlank() || passwordString.isBlank()) {
            if (emailString.isBlank()) {
                email?.error = getString(R.string.err_field_cant_be_blank)
            }
            if (usernameString.isBlank()) {
                username?.error = getString(R.string.err_field_cant_be_blank)
            }
            if (passwordString.isBlank()) {
                password?.error = getString(R.string.err_field_cant_be_blank)
            }

            return
        }

        // Regex matches only emails like example@example.com
        if (!emailString.matches("^[0-9?A-Za-z{|}~](\\.?[0-9?A-Za-z{|}~])*@[a-zA-Z](-?[a-zA-Z0-9])*(\\.[a-zA-Z](-?[a-zA-Z0-9])*)+\$".toRegex())) {
            email?.error = getString(R.string.err_email_is_invalid)
            return
        }

        val user = User(name = usernameString, email = emailString, password = passwordString)
        usersViewModel.addUser(user)
    }
}
