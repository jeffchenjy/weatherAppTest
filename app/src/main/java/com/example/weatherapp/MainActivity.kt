package com.example.weatherapp

import android.app.ActivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.fragmentmanager.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_exit)
                .setTitle(resources.getString(R.string.escTitle))
                .setMessage(resources.getString(R.string.esc))
                .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                    val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
                    val tasks = activityManager.appTasks
                    for (task in tasks) {
                        task.finishAndRemoveTask()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}