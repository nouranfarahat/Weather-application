package com.example.mvvm.allproducts.view

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.weather.utilities.getDate
import com.example.weather.utilities.getTime
import com.squareup.picasso.Picasso

@BindingAdapter("date")
fun convertDate(view:TextView,date: Long)
{
    view.text= getDate(date).toString()
}
@BindingAdapter("time")
fun convertTime(view:TextView,date: Long)
{
    view.text= getTime(date).toString()
}
//@BindingAdapter("time")
//fun loadImage(view:ImageView,url: String)
//{
//    Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(view)
//}