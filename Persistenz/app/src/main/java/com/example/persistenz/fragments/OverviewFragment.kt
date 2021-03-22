package com.example.persistenz.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.persistenz.R
import com.example.persistenz.databinding.FragmentOverviewBinding
import com.example.persistenz.models.StorageModel


class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    companion object {
        var SHARED_PREFERENCES_OVERVIEW = "fragment"
        var COUNTER_KEY = "counter"
        var TEA_DEFAULT_SWEETER_KEY = "teaSweeter"
        var TEA_DEFAULT_SWEETER_VALUE = "natural"
        var TEA_DEFAULT_SUGAR_KEY = "teaWithSugar"
        var TEA_DEFAULT_SUGAR_VALUE = true
        var TEA_DEFAULT_PREFERRED_KEY = "teaPreferred"
        var TEA_DEFAULT_PREFERRED_VALUE = "Lipton/Pfefferminztee"
        var TEA_DEFAULT_IS_SWEET = "gesüsst"
        var TEA_DEFAULT_IS_NOT_SWEET = "ungesüsst"
        fun newInstance(): OverviewFragment {
            return OverviewFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        var preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        binding.preferenceTeaEditButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_host, TeaPreferenceFragment.newInstance())
                    .addToBackStack("OverviewFragment")
                    .commit();
        }

        binding.preferenceTeaDefaultButton.setOnClickListener {
            var editor = preferences.edit()
            editor.putString(TEA_DEFAULT_SWEETER_KEY, TEA_DEFAULT_SWEETER_VALUE)
            editor.putBoolean(TEA_DEFAULT_SUGAR_KEY, TEA_DEFAULT_SUGAR_VALUE)
            editor.putString(TEA_DEFAULT_PREFERRED_KEY, TEA_DEFAULT_PREFERRED_VALUE)
            editor.apply()
            binding.preferenceTeaMessageTextView.text = String.format(resources.getString(R.string.preference_tea_message_text), TEA_DEFAULT_PREFERRED_VALUE, TEA_DEFAULT_IS_SWEET)
        }

        binding.storageWriteButton.setOnClickListener {
            var storageModel = ViewModelProvider(requireActivity())[StorageModel::class.java]
            if(binding.storageExternalCheckbox.isChecked) {
                storageModel.writeExternalStorage(binding.storageEditMultiLine.text.toString())
            } else {
                storageModel.writeInternalStorage(binding.storageEditMultiLine.text.toString())
            }
        }

        binding.storageReadButton.setOnClickListener {
            var storageModel = ViewModelProvider(requireActivity())[StorageModel::class.java]
            var result = ""
            result = if(binding.storageExternalCheckbox.isChecked) {
                storageModel.readExternalStorage()
            } else {
                storageModel.readInternalStorage()
            }
            binding.storageEditMultiLine.setText(result)
        }

        binding.contentProviderSmsShowButton.setOnClickListener {
            val grant = checkSelfPermission(requireActivity(), Manifest.permission.READ_SMS)
            if (grant != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_SMS),25)
            } else {
                var cursor = requireActivity().contentResolver.query(
                        Telephony.Sms.Inbox.CONTENT_URI,
                        arrayOf(Telephony.Sms.Inbox._ID, Telephony.Sms.Inbox.BODY),
                        null, null, null)
                AlertDialog.Builder(requireActivity())
                        .setTitle("SMS Inbox")
                        .setCursor(cursor, null, Telephony.Sms.Inbox.BODY)
                        .setNeutralButton("OK", null)
                        .create()
                        .show()
            }
        }

        var isSweet = preferences.getBoolean(TEA_DEFAULT_SUGAR_KEY, false)
        var preferred = preferences.getString(TEA_DEFAULT_PREFERRED_KEY, "")
        var sweet = ""
        if(isSweet) {
            sweet = TEA_DEFAULT_IS_SWEET
        } else {
            sweet = TEA_DEFAULT_IS_NOT_SWEET
        }
        binding.preferenceTeaMessageTextView.text = String.format(resources.getString(R.string.preference_tea_message_text), preferred, sweet)

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
        binding.preferenceFragmentMessageTextView.text = String.format(resources.getString(R.string.preference_fragment_message_text), newResumeCount)
    }
}