package com.example.persistenz.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.persistenz.R
import com.example.persistenz.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    companion object {
        var SHARED_PREFERENCES_OVERVIEW = "fragment"
        var COUNTER_KEY = "counter"
        fun newInstance(): OverviewFragment {
            return OverviewFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        var preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        binding.teaPreferenceEditButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_host, TeaPreferenceFragment.newInstance())
                    .addToBackStack("OverviewFragment")
                    .commit();
        }

        binding.teaPreferenceDefaultButton.setOnClickListener {
            var editor = preferences.edit()
            editor.putString("teaSweeter", "special")
            editor.putBoolean("teaWithSugar", true)
            editor.putString("teaPreferred", "Cola")
            editor.apply()
            binding.teaPreferenceTextView.text = String.format(resources.getString(R.string.tea_preference_text), "Cola", "gesüsst")
        }

        var isSweet = preferences.getBoolean("teaWithSugar", false)
        var preferred = preferences.getString("teaPreferred", "")
        var sweet = ""
        if(isSweet) {
            sweet = "gesüsst"
        } else {
            sweet = "ungesüsst"
        }
        binding.teaPreferenceTextView.text = String.format(resources.getString(R.string.tea_preference_text), preferred, sweet)

        return binding.root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val preferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_OVERVIEW, Context.MODE_PRIVATE)
        val newResumeCount = preferences.getInt(COUNTER_KEY, 0) + 1
        val editor = preferences.edit()
        editor.putInt(COUNTER_KEY, newResumeCount)
        editor.apply()
        binding.privatePreferenceTextView.text = String.format(resources.getString(R.string.private_preference_text), newResumeCount)
    }
}