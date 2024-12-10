package com.melendez.paulo.frontend_proyecto.network

import android.os.Parcel
import android.os.Parcelable

data class Restaurant(
    val restaurantId: Int,
    val nombre: String?,
    val descripcion: String?,
    val ubicacion: String?,
    val tipo: String?,
    val img: String?,
    val horaApertura: String?,
    val horaCierre: String?,
    val distancia: String?,
    val tiempo: String?,
    val calificacion: Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(restaurantId)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeString(ubicacion)
        parcel.writeString(tipo)
        parcel.writeString(img)
        parcel.writeString(horaApertura)
        parcel.writeString(horaCierre)
        parcel.writeString(distancia)
        parcel.writeString(tiempo)
        parcel.writeFloat(calificacion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Restaurant> {
        override fun createFromParcel(parcel: Parcel): Restaurant {
            return Restaurant(parcel)
        }

        override fun newArray(size: Int): Array<Restaurant?> {
            return arrayOfNulls(size)
        }
    }
}