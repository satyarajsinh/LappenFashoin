package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainVerifyOtp(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("access_token")
        var accessToken: String?,
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
        @SerializedName("token_type")
        var tokenType: String?,
        @SerializedName("is_profile_setup")
        var is_profile_setup: Int?,
        @SerializedName("user_id")
        var userId: Int?
    )
}