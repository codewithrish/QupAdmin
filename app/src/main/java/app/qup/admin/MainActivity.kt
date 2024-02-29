package app.qup.admin

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import app.qup.admin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>()

    // private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        appBarConfiguration = AppBarConfiguration(
//            setOf()
//        )
        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.navHostFragmentContainer.id
        ) as NavHostFragment
        navController = navHostFragment.navController

        val inflater = navHostFragment.navController.navInflater
        val graph = if (mainViewModel.getAccessToken().isNullOrEmpty()) {
            inflater.inflate(app.qup.authentication.R.navigation.auth_graph)
        } else {
            inflater.inflate(app.qup.home.R.navigation.home_graph)
        }
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navController.setGraph(graph, intent.extras)

    }
}