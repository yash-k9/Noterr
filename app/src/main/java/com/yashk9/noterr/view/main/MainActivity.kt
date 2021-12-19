package com.yashk9.noterr.view.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.yashk9.noterr.R
import com.yashk9.noterr.databinding.ActivityMainBinding
import com.yashk9.noterr.repo.NoteRepo
import com.yashk9.noterr.view.viewModel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var repo: NoteRepo

    private lateinit var navHostFragment: NavHostFragment

    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val toolbar = binding.topAppBar
        setSupportActionBar(toolbar)

        viewModel
        initUI()
        intiViews()
    }

    private fun initUI() {
        val nightMode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when(nightMode){
            Configuration.UI_MODE_NIGHT_YES -> viewModel.setUiMode(true)
            Configuration.UI_MODE_NIGHT_NO  -> viewModel.setUiMode(false)
        }
    }

    private fun intiViews() {
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        with(navController){
            val appBarConfiguration = AppBarConfiguration(graph)
            setupActionBarWithNavController(this, appBarConfiguration)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navHostFragment.navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}