package com.himatifunpad.imazine.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.himatifunpad.imazine.BuildConfig
import com.himatifunpad.imazine.R
import com.himatifunpad.imazine.core.data.local.DataStoreManager
import com.himatifunpad.imazine.ui.ext.toggleAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  companion object {
    private const val SPLASH_SCREEN_DURATION = 1000L
  }

  @Inject
  lateinit var prefs: DataStoreManager
  private lateinit var firebaseAnalytics: FirebaseAnalytics

  override fun onCreate(savedInstanceState: Bundle?) {
    Thread.sleep(SPLASH_SCREEN_DURATION)
    setTheme(R.style.Theme_Imazine)
    // inflate
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    // setting up nav component
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
    val inflater = navHostFragment.navController.navInflater
    val graph = inflater.inflate(R.navigation.graph_main)
    // run with lifecylce scope
    lifecycleScope.launchWhenCreated {
      toggleAppTheme(prefs.appTheme.first())
      // set start destination
      if (isLoggedIn()) {
        graph.setStartDestination(R.id.homeScreen)
      } else {
        graph.setStartDestination(R.id.authScreen)
      }
      // bind navGraph to fragment
      val navController = navHostFragment.navController
      navController.setGraph(graph, intent.extras)
    }
    // init analytics
    firebaseAnalytics = Firebase.analytics
  }

  private suspend fun isLoggedIn(): Boolean = prefs.user.first().isEmpty().not()

  override fun onResume() {
    super.onResume()
    if (BuildConfig.DEBUG) {
      window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
  }
}
