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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.dailyexpensetracker.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val notificationsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) return@registerForActivityResult
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_granted),
                Toast.LENGTH_SHORT
            ).show()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsViewModel: SettingsViewModel by activityViewModels()

        setupCurrencySpinner(view, settingsViewModel)
        setupNotificationSwitcher(view, settingsViewModel)
    }

    private fun setupCurrencySpinner(view: View, settingsViewModel: SettingsViewModel) {
        val currencyNames = Currency.entries.map { it.name }.toTypedArray()

        binding.currencySpinner.adapter =getSpinnerAdapter(currencyNames)

        settingsViewModel.currentCurrency.observe(viewLifecycleOwner) { newCurrency ->
            val position = ( binding.currencySpinner.adapter as ArrayAdapter<String>)
                .getPosition(newCurrency.name)
            binding.currencySpinner.setSelection(position)
        }

        binding.currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                handleCurrencySelected(position, currencyNames, settingsViewModel)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSpinnerAdapter(arrayOfItems: Array<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOfItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun handleCurrencySelected(position: Int, arrayOfCurrency: Array<String>, settingsViewModel: SettingsViewModel) {
        val currency = Currency.valueOf(arrayOfCurrency[position])
        Toast.makeText(requireContext(), getString(R.string.current_currency, currency), Toast.LENGTH_SHORT).show()
        settingsViewModel.setCurrentCurrency(requireContext(), currency)
    }

    private fun setupNotificationSwitcher(view: View, settingsViewModel: SettingsViewModel) {
        val isEnabled = settingsViewModel.getNotificationPreference(requireContext())
        binding.notificationSwitcher.isChecked = isEnabled

        binding.notificationSwitcher.setOnCheckedChangeListener { _, isChecked ->
            handleNotificationToggle(isChecked, settingsViewModel)
        }
    }

    private fun handleNotificationToggle(isChecked: Boolean, settingsViewModel: SettingsViewModel) {
        val message = if (isChecked) getString(R.string.notificationsON) else getString(R.string.notificationsOFF)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        settingsViewModel.setNotificationStatus(requireContext(), isChecked)

        if (!isChecked) {
            ReminderScheduler.cancelReminder(requireContext())
            return
        }

        checkAndAskNotificationPermission()
        if (isNotificationPermissionGranted()) {
            scheduleReminderForExactTime()
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun checkAndAskNotificationPermission() {
        if (!isNotificationPermissionGranted()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), POST_NOTIFICATIONS)) {
                notificationsPermissionLauncher.launch(POST_NOTIFICATIONS)
                return
            }
            showRequestPermissionRationale()
        }
    }

    private fun showRequestPermissionRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.permission_reason))
            .setPositiveButton(getString(R.string.grant)) { _, _ ->
                notificationsPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun scheduleReminderForExactTime() {
        ReminderScheduler.scheduleDailyReminder(
            requireContext(),
            ReminderConfig.HOUR,
            ReminderConfig.MIN
        )
    }
}
