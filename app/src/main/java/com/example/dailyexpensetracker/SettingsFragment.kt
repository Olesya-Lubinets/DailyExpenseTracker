package com.example.dailyexpensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast


class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var currency: Currency

        super.onViewCreated(view, savedInstanceState)

        val currencySpinner = view.findViewById<Spinner>(R.id.currency_spinner)
        val arrayOfCurrency = Currency.entries.map { it.name }.toTypedArray()


        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOfCurrency)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = adapter

        val position =
            (currencySpinner.adapter as ArrayAdapter<String>).getPosition(CurrentSettings.currentCurrency.name)
        currencySpinner.setSelection(position)

        val notificationSwitcher = view.findViewById<Switch>(R.id.notification_switcher)
        notificationSwitcher.isChecked = CurrentSettings.notificationStatus


        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currency = Currency.valueOf(arrayOfCurrency[position])
                CurrentSettings.currentCurrency = currency
                Toast.makeText(requireContext(), "Current currency: $currency", Toast.LENGTH_SHORT)
                    .show()
                AppPreferences.savePreference(requireContext(), "currency", currency.name)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        notificationSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Notifications are on", Toast.LENGTH_SHORT).show()
                CurrentSettings.notificationStatus = true
                AppPreferences.savePreference(requireContext(), "notifications", true)
            } else {
                CurrentSettings.notificationStatus = false
                Toast.makeText(requireContext(), "Notifications are off", Toast.LENGTH_SHORT).show()
                AppPreferences.savePreference(requireContext(), "notifications", false)
            }

        }
    }
}