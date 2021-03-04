package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

class ResponseMain : ArrayList<ResponseMain.ResponseMainItem>(){
    data class ResponseMainItem(
        @SerializedName("destination")
        var destination: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("imageurl")
        var imageurl: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("propellant")
        var propellant: String?,
        @SerializedName("technologyexists")
        var technologyexists: String?
    )
}