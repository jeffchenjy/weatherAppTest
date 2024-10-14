package com.example.fragmentmanager

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherapp.FragmentShift
import com.example.weatherapp.FragmentShift.Companion.LOCALE_FRAGMENT_SHIFT_TAG
import com.example.weatherapp.LocaleHelper
import com.example.weatherapp.R

class LocaleFragment: Fragment() {
    private lateinit var toolbar: Toolbar
    private lateinit var localeRadioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_locale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findView(view)
        setUpToolbar()
        setDefaultLanguageSelection(view)
        radioGroupCheckedChangeListener(view)
    }
    private fun findView(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        localeRadioGroup = view.findViewById(R.id.localeRadioGroup)
    }
    private fun setUpToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.languageSettings)
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_navigation_back)
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setNavigationOnClickListener {
                FragmentShift.getInstance().returnBackStackFragment(
                    requireActivity(),
                    LOCALE_FRAGMENT_SHIFT_TAG
                )
        }
    }
    private fun setDefaultLanguageSelection(view: View) {
        val currentLanguage = LocaleHelper.getCurrentLanguage(requireContext())
        when (currentLanguage) {
            "" -> {
                localeRadioGroup.check(R.id.localeEnglish)
                val selectedRadioButton: RadioButton = view.findViewById(R.id.localeEnglish)
                selectedRadioButton.setBackgroundColor(requireContext().getColor(R.color.radioButtonBackground))
            }
            "zh" -> {
                localeRadioGroup.check(R.id.localeChinese)
                val selectedRadioButton: RadioButton = view.findViewById(R.id.localeChinese)
                selectedRadioButton.setBackgroundColor(requireContext().getColor(R.color.radioButtonBackground))
            }
            "ja" -> {
                localeRadioGroup.check(R.id.localeJapanese)
                val selectedRadioButton: RadioButton = view.findViewById(R.id.localeJapanese)
                selectedRadioButton.setBackgroundColor(requireContext().getColor(R.color.radioButtonBackground))
            }
        }
    }
    private fun radioGroupCheckedChangeListener(view: View) {
        val radioGroupChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (group.id) {
                R.id.localeRadioGroup -> {
                    val languageName = view.findViewById<RadioButton>(checkedId).text.toString()
                    when(languageName) {
                        getString(R.string.english) -> LocaleHelper.setLocale(requireContext(), "")
                        getString(R.string.chinese) -> LocaleHelper.setLocale(requireContext(), "zh")
                        getString(R.string.japanese) -> LocaleHelper.setLocale(requireContext(), "ja")
                    }
                }

            }
            // 清除所有 RadioButton 的背景
            for (i in 0 until group.childCount) {
                val radioButton = group.getChildAt(i) as RadioButton
                radioButton.setBackgroundColor(Color.TRANSPARENT)
            }
            // 設置 RadioButton 的背景颜色
            val selectedRadioButton: RadioButton = view.findViewById(checkedId)
            selectedRadioButton.setBackgroundColor(requireContext().getColor(R.color.radioButtonBackground))
            restartActivity()
        }
        localeRadioGroup.setOnCheckedChangeListener(radioGroupChangeListener)
    }
    private fun restartActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }
}