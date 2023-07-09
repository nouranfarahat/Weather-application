package com.example.weather.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite_Weather")
data class FavoriteWeather(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    val lat: Double,
    val lon: Double,
    val country: String?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteWeather> {
        override fun createFromParcel(parcel: Parcel): FavoriteWeather {
            return FavoriteWeather(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteWeather?> {
            return arrayOfNulls(size)
        }
    }
}