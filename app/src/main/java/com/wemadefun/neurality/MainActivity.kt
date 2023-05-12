package com.wemadefun.neurality

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wemadefun.neurality.admob.EnergyRewardedAd
import com.wemadefun.neurality.admob.GameOverInterstitialAd
import com.wemadefun.neurality.data.iapdata.IapDataRepository
import com.wemadefun.neurality.data.userdata.modules.SaverModule
import com.wemadefun.neurality.di.ActivityModule
import com.wemadefun.neurality.di.ApplicationModule
import com.wemadefun.neurality.di.DaggerApplicationComponent
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksReceiver
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import javax.inject.Inject


class MainActivity : AppCompatActivity(){

    private lateinit var navView: BottomNavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    // Initialize AdMob Variables
    @Inject lateinit var energyRewardedAd: EnergyRewardedAd
    @Inject lateinit var gameOverInterstitialAd: GameOverInterstitialAd
    @Inject lateinit var saverModule: SaverModule
    @Inject lateinit var billing: IapDataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        FireCrashlytics.log("Activity Created")
        setupDependencyInjection()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        if (!this::navView.isInitialized)
            navView = findViewById(R.id.nav_view)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_stats,
            R.id.nav_train,
            R.id.nav_others
        ))

        navView.setupWithNavController(navController)
        intent?.let { receiveDeepLinks(it) }
        setupSystemLayout()
        setupBilling()
        setupFireRemote()
    }

    private fun setupDependencyInjection() {
        val applicationModule = ApplicationModule(application as BrainApplication)
        val activityModule = ActivityModule(this)
        // Inject at MainActivity
        (application as BrainApplication).appComponent = DaggerApplicationComponent.builder()
            .applicationModule(applicationModule)
            .activityModule(activityModule)
            .build()
        (application as BrainApplication).appComponent.inject(this)
    }

    private fun setupFireRemote() {
        FireRemote.initialize()
    }

    private fun receiveDeepLinks(intent: Intent) {
        DynamicLinksReceiver.receiveReferrerData(intent)
    }

    private fun setupBilling() {
        billing.billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {}
            override fun onBillingServiceDisconnected() {}
        })
    }

    private fun setupSystemLayout() {
        navController.addOnDestinationChangedListener {_, destination, _ ->
            when (destination.id) {
                R.id.nav_train -> {
                    navView.visibility = View.VISIBLE
                }
                R.id.nav_stats -> {
                    navView.visibility = View.VISIBLE
                }
                R.id.nav_others -> {
                    navView.visibility = View.VISIBLE
                }
                else -> {
                    navView.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { receiveDeepLinks(it) }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onStart() {
        super.onStart()
        FireCrashlytics.log("Activity Started")
    }

    override fun onResume() {
        super.onResume()
        FireCrashlytics.log("Activity Resumed")
    }

    override fun onPause() {
        super.onPause()
        FireCrashlytics.log("Activity Paused")
    }

    override fun onStop() {
        super.onStop()
        FireCrashlytics.log("Activity Stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        saverModule.saveLocalAndRemote()
        FireCrashlytics.log("Activity Destroyed")
    }
}
