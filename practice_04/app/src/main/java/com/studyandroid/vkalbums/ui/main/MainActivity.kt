package com.studyandroid.vkalbums.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.studyandroid.vkalbums.R
import com.studyandroid.vkalbums.databinding.ActivityMainBinding
import com.studyandroid.vkalbums.ui.WelcomeActivity
import com.studyandroid.vkalbums.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.main_fragmentcontainerview) as NavHostFragment

        val sharedPref =
            getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean(getString(R.string.pref_is_first_time), true)
        if (isFirstTime) {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.getUserOriginalIdResponse.observe(this) { response ->
            if (navHost.navController.currentDestination?.id != R.id.main_empty_home_fragment) {
                navHost.navController.navigate(R.id.main_empty_home_fragment)
            }
            binding.progressBar.visibility = View.GONE
            binding.enterVkId.visibility = View.GONE
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data != null) {
                    if (body.data.isNotEmpty()) {
                        val user = body.data.first()
                        supportActionBar?.title =
                            String.format(
                                getString(R.string.currently_viewing_user),
                                "${user.firstName} ${user.lastName}"
                            )
                        if (!user.isClosed) {
                            navHost.navController.navigate(
                                R.id.action_main_empty_home_fragment_to_albums_fragment,
                                bundleOf(
                                    "user_id" to user.id
                                )
                            )
                        } else {
                            binding.profileIsClosed.visibility = View.VISIBLE
                        }
                    } else {
                        binding.cantFindThisUser.visibility = View.VISIBLE
                    }
                } else {
                    Log.d(
                        "VK_API_INTERNAL_ERR",
                        "code=${body?.error?.errorCode} msg=${body?.error?.errorMsg}"
                    )
                    binding.vkApiError.visibility = View.VISIBLE
                }
            } else {
                binding.enterVkId.visibility = View.VISIBLE
                if (response.code() == 997) {
                    Toast.makeText(this,
                        getString(R.string.internet_conn_issues),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.main_empty_home_fragment -> {
                    binding.enterVkId.visibility = View.VISIBLE
                    supportActionBar?.title =
                        String.format(
                            getString(R.string.currently_viewing_user),
                            getString(R.string.app_name)
                        )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        val search = menu.findItem(R.id.action_search)
        val view = search.actionView as SearchView

        view.queryHint = getString(R.string.vk_id)
        view.maxWidth = Int.MAX_VALUE
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(vkId: String?): Boolean {
                view.onActionViewCollapsed()

                binding.progressBar.visibility = View.VISIBLE
                binding.enterVkId.visibility = View.GONE
                binding.vkApiError.visibility = View.GONE
                binding.cantFindThisUser.visibility = View.GONE
                binding.profileIsClosed.visibility = View.GONE
                vkId?.let {
                    mainViewModel.getUserOriginalId(it)
                }

                return true
            }

            override fun onQueryTextChange(s: String?): Boolean = true
        })

        return true
    }
}
