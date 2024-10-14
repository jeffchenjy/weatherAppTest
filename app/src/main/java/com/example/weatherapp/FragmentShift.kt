package com.example.weatherapp


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager


class FragmentShift {
    companion object {
        @Volatile
        private var instance: FragmentShift? = null
        fun getInstance(): FragmentShift {
            return instance ?: synchronized(this) {
                instance ?: FragmentShift().also { instance = it }
            }
        }

        const val LOCALE_FRAGMENT_SHIFT_TAG = "localeFragment"
        const val SELECT_CITY_FRAGMENT_SHIFT_TAG = "selectCityFragment"
    }
    fun goToNextFragment(fragment: Fragment, activity: FragmentActivity, tag: String, backStackName: String) {
        activity.supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out_right  // popExit
            )
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(backStackName)
            .commit()
    }
    fun goToPreviousFragment(fragment: Fragment, activity: FragmentActivity, tag: String, backStackName: String) {
        activity.supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,  // enter
                R.anim.slide_out_right,  // exit
                R.anim.fade_out,   // popEnter
                R.anim.slide_in_right  // popExit
            )
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(backStackName)
            .commit()
    }
    fun returnBackStackFragment(activity: FragmentActivity, popBackStackName: String) {
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentManager.popBackStack(popBackStackName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentTransaction.commit()
    }

}