package com.example.weather.utilities

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.LocaleList
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.LocaleListCompat
import com.example.weather.R
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.System.currentTimeMillis
import java.lang.ref.Cleaner.create
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern


fun convertUnixToDate(unixFormat: Long): Date {
    return Date(unixFormat * 1000L)
}

fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("EEE, d MMMM", Locale.getDefault())
    return dateFormat.format(date)
}

fun formatTime(date: Date): String {
    val timeFormat = SimpleDateFormat("h a", Locale.getDefault())
    return timeFormat.format(date)
}

fun formatDayOfWeek(date: Date): String {
    val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return dayOfWeekFormat.format(date)
}

fun getDate(unixFormat: Long): String {
    val date = convertUnixToDate(unixFormat)
    return formatDate(date)
}
fun alertTime(time:Long):String
{
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    return timeFormat.format(time)

}
fun alertDate(date:Long):String
{
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    return dateFormat.format(date)

}
fun alertDateFormat(date:Long): String {

    val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return dateFormat.format(date)
}
fun alertTimeFormat(time:String): Long {
    val timeFormat = SimpleDateFormat("h:mm a")
    return timeFormat.parse(time).time

}
fun getDay(unixFormat: Long,language: String): String {
    val day = convertUnixToDate(unixFormat)
    /*Log.i("Nouran", "getDay: $language  ${formatDayOfWeek(day)}")
    if(language.equals(Constants.ARABIC)) {
        Log.i("Nouran", "getDay: InFunction")
        Log.i("Nouran", "getDay: map +${formatDayOfWeek(day)}  ${mapDays(formatDayOfWeek(day))}")

        return mapDays(formatDayOfWeek(day))
    }
*/
    return formatDayOfWeek(day)
}

fun getTime(unixFormat: Long): String {
    val time = convertUnixToDate(unixFormat)
    return formatTime(time)
}

fun tempFormat(temp: Double, unit: String,language: String): String {
    val unitDegree = if (unit.equals("metric")) "°C" else if (unit.equals("imperial")) "°F" else " K"
    if(language.equals(Constants.ARABIC))
        return mapNumber(temp.toInt().toString()) + unitDegree

    return temp.toInt().toString()+ unitDegree

}

fun getFullTempFormat(minTemp: Double, maxTemp: Double, unit: String,language: String): String {
    val unitDegree = if (unit.equals("metric")) "°C" else if (unit.equals("imperial")) "°F" else " K"
    if(language.equals(Constants.ARABIC))
        return mapNumber( maxTemp.toInt().toString()) + "/" + mapNumber(minTemp.toInt().toString()) + unitDegree
    return maxTemp.toInt().toString() + "/" + minTemp.toInt().toString() + unitDegree

}

fun setHumidity(humidity: Long): String {
    return humidity.toString() + "%"
}

fun setClouds(clouds: Long): String {
    return clouds.toString() + "%"
}

fun setVisibility(visiblility: Long,context: Context): String {
    return visiblility.toString() + context.getString(R.string.m)
}

fun setPressure(pressure: Long,context: Context): String {
    return pressure.toString() + context.getString(R.string.hpa)
}

fun setWind(wind: Double, windSpeed:String,unit: String,context: Context): String {
    var wind_speed = wind
    if (windSpeed.equals(Constants.IMPERIAL) && !unit.equals(Constants.IMPERIAL)) {
        wind_speed =convertFromMeterPerSecToMilePerHour(wind)

    } else if (!windSpeed.equals(Constants.IMPERIAL) && unit.equals(Constants.IMPERIAL)) {
        wind_speed =convertFromMilePerHourToMeterPerSec(wind)
    }
    val unitDegree = if (windSpeed.equals(Constants.IMPERIAL)) context.getString(R.string.mile_hour) else context.getString(R.string.meter_sec)
    var result= wind_speed.toString()
    /*if(Changables.language==Constants.ARABIC)
    {
        result=convertEnglishToArabicNumbers(result)
    }*/
    return result+unitDegree
}

fun convertFromMeterPerSecToMilePerHour(value: Double): Double {
    return value * 2.2369362912
}

fun convertFromMilePerHourToMeterPerSec(value: Double): Double {
    return value * 0.447
}

fun changeAppLanguage(language: String)
{
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
    AppCompatDelegate.setApplicationLocales(appLocale)
    /*val locale = Locale(language)
    val appLocale = LocaleList(locale)

    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    Locale.setDefault(locale)
    Configuration().apply {
        setLocale(locale)
        Resources.getSystem().updateConfiguration(this, null)
    }
    Resources.getSystem().configuration.setLocales(appLocale)
    Resources.getSystem().updateConfiguration(Resources.getSystem().configuration, null)*/
}
fun  setLanguage(context: Context, language:String) {
    val locale = Locale(language)
    val config = context.resources.configuration
    config.locale= Locale(language)
    Locale.setDefault(locale)
    config.setLayoutDirection(Locale(language))
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
    context.createConfigurationContext(config)
}
fun convertEnglishToArabicNumbers(input: String): String {
    val arabicLocale = Locale("ar")
    val englishNumbers = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    val arabicNumbers = listOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
    val pattern = Pattern.compile("\\d")
    val matcher = pattern.matcher(input)
    var output = input
    while (matcher.find()) {
        val englishNumber = matcher.group()
        val index = englishNumbers.indexOf(englishNumber)
        if (index != -1) {
            val arabicNumber = arabicNumbers[index]
            output = output.replace(englishNumber, arabicNumber)
        }
    }
    return output
}

/*fun translate(strValue:String):String
{
    val translate = TranslateOptions.newBuilder()
        .setApiKey(Constants.GOOGLE_API_KEY)
        .build().service
    val translation = translate.translate(strValue,
        Translate.TranslateOption.sourceLanguage("en"),
        Translate.TranslateOption.targetLanguage("ar"))
    return translation.translatedText
}*/

/*fun setCountry(country:String):String
{
    var countryName=country
    if(Changables.language.equals("ar"))
    {
        countryName= translate(country)
    }
    return countryName
}*/
fun isConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo

    if (networkInfo != null && networkInfo.isConnected == true) {

        return true
    }
/*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            }
        }
    }
    else
    {
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
    }
*/
    return false
}

/*suspend fun translateText(context: Context,strValue:String): String {
    return withContext(Dispatchers.IO) {
        try {
            val credentials = GoogleCredentials.fromStream(context.resources.openRawResource(R.raw.my_credential))
            val translateOptions = TranslateOptions.newBuilder().setCredentials(credentials).build()
            val translate = translateOptions.service
            val targetLanguage = "ar"

            val translation = translate.translate(strValue, Translate.TranslateOption.targetLanguage(targetLanguage))

            translation.translatedText
        } catch (e: Exception) {
            throw Exception("Translation failed", e)
        }
    }
    /*val credentials = GoogleCredentials.fromStream(context.resources.openRawResource(R.raw.weather_json))
    val translateOptions = TranslateOptions.newBuilder().setCredentials(credentials).build()
    val translate = translateOptions.service

    val textToTranslate = strValue
    val targetLanguage = "ar"
    val translation = translate.translate(textToTranslate, Translate.TranslateOption.targetLanguage(targetLanguage))

    Log.d("Translation", "Translated text: ${translation.translatedText}")
    return translation.translatedText*/
}*/

fun translateString(text: String, sourceLanguage: String, targetLanguage: String, listener: (String?, Exception?) -> Unit) {
    runBlocking { val options = TranslatorOptions.Builder()
        .setSourceLanguage(sourceLanguage)
        .setTargetLanguage(targetLanguage)
        .build()
        val translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        listener(translatedText, null)
                    }
                    .addOnFailureListener { exception ->
                        listener(null, exception)
                    }
            }
            .addOnFailureListener { exception ->
                listener(null, exception)
            } }

}
suspend fun translateText(text: String, sourceLanguage: String, targetLanguage: String): String {
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(sourceLanguage)
        .setTargetLanguage(targetLanguage)
        .build()
    val translator = Translation.getClient(options)
    val conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()


    return withContext(Dispatchers.IO) {
        val res=async {
            translator.downloadModelIfNeeded(conditions)
            translator.translate(text).toString() }
        res.await()
    }
    /*return withContext(Dispatchers.IO) {
        val downloadJob = async {
            translator.downloadModelIfNeeded(conditions)
        }
        downloadJob.await()
        translator.translate(text).await()
    }*/
}
/*private fun translate(textValue: String, sourceLanguage: String, targetLanguage: String): Task<String> {
    val text = textValue
    val source = sourceLanguage
    val target = targetLanguage

    val sourceLangCode = TranslateLanguage.fromLanguageTag(source)
    val targetLangCode = TranslateLanguage.fromLanguageTag(target)

    val options = TranslatorOptions.Builder()
        .setSourceLanguage(sourceLanguage)
        .setTargetLanguage(targetLanguage)
        .build()
    val translator = translators[options]
    modelDownloading.setValue(true)

    // Register watchdog to unblock long running downloads
    Handler().postDelayed({ modelDownloading.setValue(false) }, 15000)
    modelDownloadTask = translator.downloadModelIfNeeded().addOnCompleteListener {
        modelDownloading.setValue(false)
    }
    translating.value = true
    return modelDownloadTask.onSuccessTask {
        translator.translate(text)
    }.addOnCompleteListener {
        translating.value = false
    }
}*/
//private val translators =
//    object : LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS) {
//        override fun create(options: TranslatorOptions): Translator {
//            return Translation.getClient(options)
//        }
//
//        override fun entryRemoved(
//            evicted: Boolean,
//            key: TranslatorOptions,
//            oldValue: Translator,
//            newValue: Translator?
//        ) {
//            oldValue.close()
//        }
//    }

fun mapDays(weekday: String): String {
    val weekdayMapping = mapOf(
        "Monday" to "الإثنين",
        "Tuesday" to "الثلاثاء",
        "Wednesday" to "الأربعاء",
        "Thursday" to "الخميس",
        "Friday" to "الجمعة",
        "Saturday" to "السبت",
        "Sunday" to "الأحد"
    )
    return weekdayMapping.getOrDefault(weekday, "Invalid weekday")
}
fun mapNumber(number: String): String {
    val numberMapping = mapOf(
        "0" to "٠",
        "1" to "١",
        "2" to "٢",
        "3" to "٣",
        "4" to "٤",
        "5" to "٥",
        "6" to "٦",
        "7" to "٧",
        "8" to "٨",
        "9" to "٩"
    )
    return number.map { numberMapping.getOrDefault(it.toString(), it.toString()) }.joinToString("")
}

fun setTextViewValue(text:String,language:String):String
{
    var strValue=text
    if(language.equals(Constants.ARABIC))
    {

        translateString(text, Constants.ENGLISH, Constants.ARABIC) { translatedText, exception ->
            if (exception != null) {
                Log.e("Translation", "Translation failed: ${exception.message}")
            } else {
                if (translatedText != null) {
                    strValue=translatedText
                    Log.d("Translation", "Translated text: $translatedText")

                }
            }
        }
    }
    Log.d("Translation", "Here text: $strValue")
    return strValue

}

fun getFullTime(date:Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    return formatter.format(date)
}
fun getTimeInMillis(hours: Int, minutes: Int): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY,hours)
    cal.set(Calendar.MINUTE, minutes)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return cal.timeInMillis
}
fun mapIcons(icon: String): Int {
    val iconMapping = mapOf(
        "01d" to R.drawable.sunny,
        "01n" to R.drawable.moon,
        "02d" to R.drawable.few_clouds,
        "02n" to R.drawable.few_clouds_n,
        "03d" to R.drawable.scattered_clouds,
        "03n" to R.drawable.scattered_clouds,

    )
    return iconMapping.getOrDefault(icon, R.drawable.sunny)
}
fun getAddress(context: Context,lat: Double, lng: Double,language: String): Pair<String?, String?> {
    val addresses = Geocoder(context, Locale.getDefault()).getFromLocation(lat, lng, 1)

    val address = addresses?.get(0)?.getAddressLine(0)

    val city = addresses?.get(0)?.locality
    val state = addresses?.get(0)?.adminArea
    val country = addresses?.get(0)?.countryName
    val postalCode = addresses?.get(0)?.postalCode
    val knownName = addresses?.get(0)?.featureName
    return Pair(state,city)
}

fun showSnackBar(view: View, msg:String)
{
    Snackbar.make(
        view,
        msg,
        Snackbar.LENGTH_LONG
    ).show()
}