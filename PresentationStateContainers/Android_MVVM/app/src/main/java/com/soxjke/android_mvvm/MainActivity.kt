package com.soxjke.android_mvvm

import android.content.Context
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.soxjke.android_mvvm.ui.main.LoginFragment
import com.soxjke.android_mvvm.ui.main.LoginViewModel
import com.squareup.seismic.ShakeDetector

class MainActivity : AppCompatActivity(), ShakeDetector.Listener {

    lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shakeDetector = ShakeDetector(this)
        shakeDetector.setSensitivity(10)
        shakeDetector.start(getSystemService(Context.SENSOR_SERVICE) as SensorManager)
        setContentView(R.layout.activity_main)
        start(isStorybook)
    }

    override fun onDestroy() {
        super.onDestroy()
        shakeDetector.stop()
    }

    override fun hearShake() {
        toggleStorybook()
    }

    fun onFragmentAttach(fragment: Fragment) {
        if (isStorybook) { return }
        when (fragment) {
            is LoginFragment -> fragment.setViewModel(ViewModelProvider(fragment)[LoginViewModel::class.java])
        }
    }

    private var isStorybook: Boolean = false
    private fun toggleStorybook() {
        isStorybook = !isStorybook
        start(isStorybook)
    }

    private fun start(isStorybook: Boolean) {
        when (isStorybook) {
            true -> {
                val host = NavHostFragment.create(R.navigation.nav_storybook)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_placeholder, host)
                    .setPrimaryNavigationFragment(host).commit()
            }
            false -> {
                val host = NavHostFragment.create(R.navigation.nav_main)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_placeholder,host)
                    .setPrimaryNavigationFragment(host).commit()
            }
        }
    }
}
