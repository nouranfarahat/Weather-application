package com.example.weather.alert.view

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipDescription
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.mvvm.database.ConcreteLocalSource
import com.example.weather.R
import com.example.weather.alert.viewmodel.AlertViewModel
import com.example.weather.alert.viewmodel.AlertViewModelFactory
import com.example.weather.databinding.AlarmCardBinding
import com.example.weather.databinding.AlertDialogBinding
import com.example.weather.model.AlertPojo
import com.example.weather.model.Repository
import com.example.weather.model.WeatherResponse
import com.example.weather.network.RetrofitHelper
import com.example.weather.network.WeatherClient
import com.example.weather.network.WeatherService
import com.example.weather.utilities.Constants
import com.example.weather.utilities.getFullTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AlertWorker(private var context: Context, private var workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    var repo= Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource(context))

    var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE)

    var language = sharedPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH).toString()
    var latitude = sharedPreferences.getFloat(Constants.LATITUDE, 0.0F).toDouble()
    var longitude = sharedPreferences.getFloat(Constants.LONGITUDE, 0.0F).toDouble()
    var unit =
        sharedPreferences.getString(Constants.TEMPERATURE_UNIT, Constants.STANDARD).toString()
    lateinit var resultProducts: Response<WeatherResponse>

    /*val alertType = inputData.getString("alertType")
    val starTime=inputData.getLong("startTimeOfAlert",0)
    val endTime=inputData.getLong("endTimeOfAlert",0)*/

    override suspend fun doWork(): Result {
        val alert = inputData.getString("alertobj")?.let { convertFromStringToAlert(it) }!!
        val icon = inputData.getString("icon")
        val startTime = alert.fullStartTime
        val endTime = alert.fullEndTime
        val alertType = alert.type
        val alertTag = alert.startTime.toString()
        Log.i("Nouran", "doWork: Type= ${alert.type}")

        Log.i("Nouran", "doWork: system time= ${getFullTime(System.currentTimeMillis())}")
        Log.i("Nouran", "doWork: start time= ${getFullTime(startTime)}")
        Log.i("Nouran", "doWork: end time= ${getFullTime(endTime)}")
        try {
            //val workInfos = WorkManager.getInstance(applicationContext)
            //  .getWorkInfosByTag("Worker")
            Log.i("TAG", "doWork: system time= ${getFullTime(System.currentTimeMillis())}")
            Log.i("TAG", "doWork: start time= ${getFullTime(startTime)}")
            Log.i("TAG", "doWork: end time= ${getFullTime(endTime)}")

            if (System.currentTimeMillis() >= startTime && System.currentTimeMillis() <= endTime) {
                Log.i("Nouran", "doWork: In if")

                if (alertType.equals(Constants.NOTIFICATION)) {
                    Log.i("Nouran", "doWork: In if Notification")
                    //showNotification(context,alert.description)
                    //createNotification(alert.description)
                    fireNotification(alert.description)
                } else if (alertType.equals(Constants.ALARM)) {
                    Log.i("TAG", "doWork: before Alarm")
                    withContext(Dispatchers.Main) {
                        showAlarmDialog(context, alert.description, alertTag,alert)
                    }
                }

            }
            if (System.currentTimeMillis() >= endTime) {
                cancelWorker(alertTag,alert)
            }


        } catch (e: Exception) {

            withContext(Dispatchers.Main)
            {
                Log.i("TAG", "doWork: Catch+ ${e.message}")
                Toast.makeText(context, "Worker fail", Toast.LENGTH_LONG).show()

            }

        }

        return Result.success()


    }

    fun convertFromStringToAlert(str: String): AlertPojo {
        val type = object : TypeToken<AlertPojo>() {}.type
        return Gson().fromJson<AlertPojo>(str, type)

    }

    fun showAlarmDialog(context: Context, description: String, alertTag: String,alertPojo: AlertPojo) {
        Log.i("TAG", "doWork: In Alarm")

        val alarmCardBinding = AlarmCardBinding.inflate(LayoutInflater.from(applicationContext))
        val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.alarm)

        val builder = AlertDialog.Builder(applicationContext).create().apply {
            setView(alarmCardBinding.root)

            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.TOP)

            mediaPlayer.isLooping = true
            mediaPlayer.start()

            alarmCardBinding.alarmDescription.text = description
            alarmCardBinding.dismissBtn.setOnClickListener {
                dismiss()

                cancelWorker(alertTag, alertPojo)
                //removefromworker
            }
            setOnDismissListener {
                mediaPlayer.release()
            }

            show()
        }

    }

    private fun fireNotification(desc: String) {
        Log.i("Nouran", "showNotification: In fire notification func")

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = Constants.CHANNEL_ID
        val channelName = "Notification_weather_app"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.sunny)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(1, notificationBuilder.build())
    }

    fun cancelWorker(alertTag: String, alertPojo: AlertPojo) {
        val worker = WorkManager.getInstance(applicationContext)
        worker.cancelAllWorkByTag(alertTag)
        CoroutineScope(Dispatchers.IO).launch{
            repo.localSource.removeWeatherAlert(alertPojo)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Deleted this Alert", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

/*fun createNotification(description: String) {
    Log.i("Nouran", "showNotification: In notification func")
    val name = "Notification weather app"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            Constants.CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            getSystemService(applicationContext, NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID)
        .setSmallIcon(R.drawable.notification)
        .setContentTitle("Weather Today")
        .setContentText(description)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager =
        getSystemService(applicationContext, NotificationManager::class.java)
    notificationManager!!.notify(1, builder.build())

}
}*/

/*private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Notification weather app"
        val description: String = "channel_description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance)
        channel.description = description
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}

fun showNotification(context: Context, description: String) {
    Log.i("Nouran", "showNotification: In notification func")
    //val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    createNotificationChannel(context)
    val notification = Notification.Builder(
        context,
        Constants.CHANNEL_ID
    )
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(description)
        .setPriority(Notification.PRIORITY_DEFAULT)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setChannelId(Constants.CHANNEL_ID)
        //.setNumber(10)
        .build()
}*/


