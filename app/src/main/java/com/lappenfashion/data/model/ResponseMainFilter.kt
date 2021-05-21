package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainFilter(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("color")
        var color: List<Color?>?,
        @SerializedName("size")
        var size: List<Size?>?
    ) {
        data class Color(
            @SerializedName("color")
            var color: String?,
            @SerializedName("color_code")
            var colorCode: String?
        )

        data class Size(
            @SerializedName("size")
            var size: String?,
            var flag : Int = 0
        )
    }
}