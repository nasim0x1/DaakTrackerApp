package bd.smartpost.tracker.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import bd.smartpost.tracker.R
import bd.smartpost.tracker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private var doubleBackToExitPressedOnce:Boolean = false

    private val forShowBottomBar = listOf(
        R.id.homeFragment,
        R.id.settingsFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.menu.getItem(1).isEnabled = false

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val appbarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.settingsFragment))

        setupActionBarWithNavController(navController,appbarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_,currentDestination,_->
            when(currentDestination.id){
                R.id.homeFragment -> {
                    binding.apply {
                        bottomNavigationView.visibility = View.VISIBLE
                        bottomNavigationView.animate().translationY(0f)
                        customAppBar()
                    }
                }
                R.id.settingsFragment -> {
                    binding.apply {
                        bottomNavigationView.visibility = View.VISIBLE
                        bottomNavigationView.animate().translationY(0f)
                        customAppBar()
                    }
                }
                R.id.domesticDetailsFragment -> {
                    binding.apply {
                        bottomNavigationView.visibility = View.GONE
                        bottomNavigationView.animate().translationY(200f)
                        customAppBar(false)
                    }
                }
                R.id.internationalDetailsFragment -> {
                    binding.apply {
                        bottomNavigationView.visibility = View.GONE
                        bottomNavigationView.animate().translationY(200f)
                        customAppBar(false)
                    }
                }
            }
        }

        binding.apply {
            addParcelFab.setOnClickListener {
                navController.navigate(R.id.addParcelFragment)
            }
        }

    }

    override fun onBackPressed() {
        if (!(navController.navigateUp()||super.onSupportNavigateUp())){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()||super.onSupportNavigateUp()
    }

    private fun customAppBar(show:Boolean = true){
        if (show) supportActionBar?.elevation = 0F;
        supportActionBar!!.setDisplayShowCustomEnabled(show)
        supportActionBar!!.setCustomView(R.layout.home_app_bar)
    }

}