package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainProfile(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("birth_date")
        var birthDate: String?,
        @SerializedName("email")
        var email: String?,
        @SerializedName("gender")
        var gender: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("mobile_number")
        var mobileNumber: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("user_id")
        var userId: Int?
    )
}