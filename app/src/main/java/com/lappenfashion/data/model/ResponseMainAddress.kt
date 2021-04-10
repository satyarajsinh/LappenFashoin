package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainAddress(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("address")
        var address: String?,
        @SerializedName("address_id")
        var addressId: Int?,
        @SerializedName("city")
        var city: String?,
        @SerializedName("locality_town")
        var localityTown: String?,
        @SerializedName("mobile_number")
        var mobileNumber: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("pincode")
        var pincode: String?,
        @SerializedName("state")
        var state: String?,
        @SerializedName("type")
        var type: String?
    )
}