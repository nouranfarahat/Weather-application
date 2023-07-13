package com.example.weather.alert.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.mvvm.allproducts.viewmodel.FavoriteViewModel
import com.example.mvvm.allproducts.viewmodel.FavoriteViewModelFactory
import com.example.mvvm.database.ConcreteLocalSource
import com.example.weather.R
import com.example.weather.alert.viewmodel.AlertViewModel
import com.example.weather.alert.viewmodel.AlertViewModelFactory
import com.example.weather.databinding.AlertDialogBinding
import com.example.weather.databinding.FragmentAlertsBinding
import com.example.weather.favorite.view.FavoriteFragmentDirections
import com.example.weather.model.AlertPojo
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.Repository
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.MONTHS

class AlertsFragment : Fragment(), OnAlertClickListener {
    lateinit var alertDialogBinding: AlertDialogBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var binding: FragmentAlertsBinding
    lateinit var alertAdapter: AlertAdapter
    lateinit var viewModel: AlertViewModel
    lateinit var alertViewModelFactory: AlertViewModelFactory
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)


    var flag=false
    var fromDate: Long = calendar.timeInMillis
    var toDate: Long = calendar.timeInMillis
    var fromTime: Long = 0
    var toTime: Long = 0
    lateinit var timezone: String

    lateinit var alertType: String
    var selectedDateAndTime1 = Calendar.getInstance()
    var selectedDateAndTime2 = Calendar.getInstance()
    val selectedDateAndTime = Calendar.getInstance()


    var startTime: Long = 0
    var endTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )
        editor = sharedPreferences.edit()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        binding.lifecycleOwner = this
        binding.action = this
        alertAdapter = AlertAdapter(this)
        binding.adapter = alertAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timezone = sharedPreferences.getString(Constants.TIMEZONE, "No Country").toString()


        alertViewModelFactory = AlertViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
        lifecycleScope.launch {
            viewModel.weatherAlertResponse.collect { result ->
                when (result) {
                    is AlertState.Success -> {
                        binding.alertEmpty.visibility = View.GONE
                        binding.alertRv.visibility = View.VISIBLE
                        //if(alertType.equals(Constants.ALARM))

                        alertAdapter.submitList(result.data)
                        Log.i("TAG", "onViewCreated: alert RV")
                    }
                    is AlertState.Loading -> {
                        binding.alertRv.visibility = View.GONE
                        binding.alertEmpty.visibility = View.VISIBLE

                    }

                    else -> {
                        Toast.makeText(context, "Check your Connection", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    private fun showAlertSettingsDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
        alertDialogBinding = AlertDialogBinding.bind(dialogView)
        alertDialogBinding.alertTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.notification_radio_button -> {
                    //Changables.windSpeedUnit = "metric"
                    editor.putString(Constants.ALERT, Constants.NOTIFICATION).apply()
                    //call gps function
                    println("Notify")
                    Log.i("TAG", "showInitialSetupDialog: Notify")

                    Toast.makeText(context, "Notify", Toast.LENGTH_SHORT).show()
                }
                R.id.alarm_radio_button -> {
                    //Changables.windSpeedUnit = "imperial"
                    editor.putString(Constants.ALERT, Constants.ALARM).apply()
                    //call map function
                    println("Alarm")
                    Log.i("TAG", "showInitialSetupDialog: Alarm")

                    Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show()

                }

            }
        }

        alertDialogBinding.cardFrom.setOnClickListener {
            editor.putString(Constants.ALERT_CARD_TYPE, Constants.FROM_CARD).apply()
            showDatePicker()
        }
        alertDialogBinding.cardTo.setOnClickListener {
            editor.putString(Constants.ALERT_CARD_TYPE, Constants.TO_CARD).apply()
            showDatePicker()
        }

        /*alertDialogBinding.chooseLocationCard.setOnClickListener{
            editor.putString(Constants.FRAGMENT_NAME, Constants.ALERT_FRAGMENT).apply()
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToMapsFragment()
            findNavController().navigate(action)
        }*/

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //dialog.

        alertDialogBinding.btnSaveAlert.setOnClickListener {
            alertType = sharedPreferences.getString(Constants.ALERT, Constants.ALARM).toString()


            val weatherAlert = AlertPojo(
                startDate = fromDate,
                endDate = toDate,
                startTime = fromTime,
                endTime = toTime,
                timezone = timezone,
                type = alertType,
                fullStartTime = startTime,
                fullEndTime = endTime
            )
            viewModel.insertAlertToList(weatherAlert)
            viewModel.getWeather()
            lifecycleScope.launch {
                viewModel.weatherCurrenrtResponse.collect { result ->
                    when (result) {
                        is WeatherState.Success -> {
                            val desc = result.data.get(0).alerts.get(0).description
                            if(desc.isNotEmpty()) {
                                weatherAlert.description = desc
                            }
                            val gson = Gson()
                            val alertObj = gson.toJson(weatherAlert)
                            // val alertType = sharedPreferences.getString(Constants.ALERT, Constants.ALARM).toString()
                            //result.data.get(0).alerts.get(0).start=startTime.timeInMillis
                            //result.data.get(0).alerts.get(0).end=endTime.timeInMillis


                            val requestData = Data.Builder()
                                .putString("alertobj", alertObj)
                                //.putString("desc",desc )
                                .putString("icon", R.drawable.alarm.toString())
                                //.putLong("startTimeOfAlert", startTime.timeInMillis)
                                //.putLong("endTimeOfAlert", endTime.timeInMillis)

                                //.putString("alertType" , alertType)
                                .build()
                            if(!flag){
                                Log.i("Nouran", "showAlertSettingsDialog: ${weatherAlert.fullStartTime - System.currentTimeMillis()}")
                            val request:WorkRequest = OneTimeWorkRequestBuilder<AlertWorker>()
                                .setInputData(requestData)
                                .setInitialDelay(
                                    weatherAlert.fullStartTime - System.currentTimeMillis(),
                                    TimeUnit.MILLISECONDS
                                )
                                .addTag(weatherAlert.startTime.toString())
                                .build()

                                WorkManager.getInstance(requireContext())
                                    .enqueue(request)
                            //dialog.dismiss()
                            flag=true
                            }

                        }

                        else -> {}
                    }

                }
            }



            Toast.makeText(context, "Save", Toast.LENGTH_LONG).show()
            Log.i("TAG", "onViewCreated: ADD to ROOM")
            requestOverlayPermission(this)
            dialog.dismiss()
        }
        alertDialogBinding.btnCancelAlert.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()


    }

    fun showDatePicker() {

        val datePicker = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, monthOfYear)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                selectedDateAndTime.set(Calendar.YEAR, year)
                selectedDateAndTime.set(Calendar.MONTH, monthOfYear)
                selectedDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val cardType =
                    sharedPreferences.getString(Constants.ALERT_CARD_TYPE, Constants.FROM_CARD)
                        .toString()
                if (cardType.equals(Constants.FROM_CARD)) {
                    //selectedDateAndTime1 = selectedDateAndTime
                    fromDate = selectedDate.timeInMillis
                    alertDialogBinding.fromDate.text = alertDateFormat(fromDate)
                    showTimePicker()
                } else if (cardType.equals(Constants.TO_CARD)) {
                    //selectedDateAndTime2 = selectedDateAndTime

                    toDate = selectedDate.timeInMillis
                    alertDialogBinding.toDate.text = alertDateFormat(toDate)
                    showTimePicker()
                }
            },
            year,
            month,
            day
        )
        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePicker.show()
    }

    fun showTimePicker() {
        //val calendar = Calendar.getInstance()

        val timePicker = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)

                selectedDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedDateAndTime.set(Calendar.MINUTE, minute)

                val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                val cardType =
                    sharedPreferences.getString(Constants.ALERT_CARD_TYPE, Constants.FROM_CARD)
                        .toString()
                val timeResult = timeFormat.format(selectedTime.time)

                if (cardType.equals(Constants.FROM_CARD)) {
                    //selectedDateAndTime1 = selectedDateAndTime

                    fromTime = alertTimeFormat(timeResult)

                    startTime =getTimeInMillis(hourOfDay,minute) //selectedDateAndTime.apply { set(Calendar.HOUR_OF_DAY, hourOfDay); set(Calendar.MINUTE, minute) }.timeInMillis
                    Log.i("Nouran", "showTimePicker: startTime ${startTime-System.currentTimeMillis()}")
                    alertDialogBinding.fromTime.text = (timeResult)

                } else if (cardType.equals(Constants.TO_CARD)) {
                    //selectedDateAndTime2 = selectedDateAndTime

                    toTime = alertTimeFormat(timeResult)
                    endTime = getTimeInMillis(hourOfDay,minute)//selectedDateAndTime.apply { set(Calendar.HOUR_OF_DAY, hourOfDay); set(Calendar.MINUTE, minute) }.timeInMillis
                    Log.i("Nouran", "showTimePicker: endTime ${getFullTime(endTime)}")

                    alertDialogBinding.toTime.text = (timeResult)

                }


            },
            hour,
            minute,
            false
        )

        timePicker.show()
    }

    override fun onDeleteClick(alert: AlertPojo) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setIcon(R.drawable.ic_baseline_delete_24)
            .setMessage("Are you sure delete this Alert?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteAlertFromList(alert)
                Toast.makeText(requireContext(), "Deleted this Alert", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onAddClick() {
        flag=false
        showAlertSettingsDialog()
    }
    fun requestOverlayPermission(fragment: Fragment): Boolean {
        if (!Settings.canDrawOverlays(fragment.requireContext())) {
            val builder = AlertDialog.Builder(fragment.requireContext())
            builder.setTitle(R.string.overlay_permission)
            builder.setMessage(R.string.overlay_message)
            builder.setPositiveButton(R.string.permission_grant) { _, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${fragment.requireContext().packageName}"))
                fragment.startActivityForResult(intent, 100)
            }
            builder.setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            builder.setCancelable(false)
            builder.show()
            return false
        } else {
            return true
        }
    }


}