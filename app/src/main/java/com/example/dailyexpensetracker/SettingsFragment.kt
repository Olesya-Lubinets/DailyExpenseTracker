package com.example.dailyexpensetracker

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels


class SettingsFragment : Fragment() {

    val notificationsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(
                    context,
                    "Notification permission granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)


        val settingsViewModel: SettingsViewModel by activityViewModels()

        val currencySpinner = view.findViewById<Spinner>(R.id.currency_spinner)
        val arrayOfCurrency = Currency.entries.map { it.name }.toTypedArray()


        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOfCurrency)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = adapter

        settingsViewModel.currentCurrency.observe(viewLifecycleOwner) { newCurrency ->
            val position =
                (currencySpinner.adapter as ArrayAdapter<String>).getPosition(newCurrency.name)
            currencySpinner.setSelection(position)
        }


        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val currency = Currency.valueOf(arrayOfCurrency[position])
                Toast.makeText(requireContext(), "Current currency: $currency", Toast.LENGTH_SHORT)
                    .show()
                settingsViewModel.setCurrentCurrency(requireContext(), currency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val notificationSwitcher = view.findViewById<Switch>(R.id.notification_switcher)

        val isEnabled = AppPreferences.getDataFromPreferences(requireContext(),"notifications", false)
        notificationSwitcher.isChecked = isEnabled

        notificationSwitcher.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Notifications are on" else "Notifications are off"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            settingsViewModel.setNotificationStatus(requireContext(), isChecked)

            if (isChecked) {
                checkNotificationPermission()
                if (ContextCompat.checkSelfPermission(requireContext(), POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                    ReminderScheduler.scheduleDailyReminder(
                        requireContext(),
                        ReminderConfig.HOUR,
                        ReminderConfig.MIN
                    )
                }
            } else {
                ReminderScheduler.cancelReminder(requireContext())
            }
        }
    }

    fun  checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_DENIED
        ) {
            when {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    POST_NOTIFICATIONS
                ) -> {
                    AlertDialog.Builder(context)
                        .setTitle("Notifications permission required")
                        .setMessage("This app needs permission to send you notifications.")
                        .setPositiveButton("Grant") { _, _ ->
                            notificationsPermissionLauncher
                                .launch(POST_NOTIFICATIONS)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()

                }

                else -> {
                    notificationsPermissionLauncher.launch(POST_NOTIFICATIONS)
                }
            }
        }
    }
}