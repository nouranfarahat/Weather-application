package com.example.mvvm.allproducts.view

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.weather.R
import com.example.weather.utilities.*
import com.squareup.picasso.Picasso

@BindingAdapter("date")
fun convertDate(view:TextView,date: Long)
{
    view.text= alertDate(date).toString()
}
@BindingAdapter("time")
fun convertTime(view:TextView,date: Long)
{
    view.text= alertTime(date).toString()
}
@BindingAdapter("icon")
fun convertIcon(view:ImageView,type: String)
{
    val drawable:Drawable?
    if(type.equals(Constants.ALARM))
    {
        drawable= ContextCompat.getDrawable(view.context, R.drawable.alarm)

    }
    else {
        drawable = ContextCompat.getDrawable(view.context, R.drawable.notificationtwo)
    }

    view.setImageDrawable(drawable)
}
//@BindingAdapter("time")
//fun loadImage(view:ImageView,url: String)
//{
//    Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(view)
//}