package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainZipCode(
    @SerializedName("delivery_codes")
    var deliveryCodes: List<DeliveryCode?>?
) {
    data class DeliveryCode(
        @SerializedName("postal_code")
        var postalCode: PostalCode?
    ) {
        data class PostalCode(
            @SerializedName("cash")
            var cash: String?,
            @SerializedName("center")
            var center: List<Center?>?,
            @SerializedName("cod")
            var cod: String?,
            @SerializedName("country_code")
            var countryCode: String?,
            @SerializedName("covid_zone")
            var covidZone: Any?,
            @SerializedName("district")
            var district: String?,
            @SerializedName("inc")
            var inc: String?,
            @SerializedName("is_oda")
            var isOda: String?,
            @SerializedName("max_amount")
            var maxAmount: Double?,
            @SerializedName("max_weight")
            var maxWeight: Double?,
            @SerializedName("pickup")
            var pickup: String?,
            @SerializedName("pin")
            var pin: Int?,
            @SerializedName("pre_paid")
            var prePaid: String?,
            @SerializedName("repl")
            var repl: String?,
            @SerializedName("sort_code")
            var sortCode: String?,
            @SerializedName("state_code")
            var stateCode: String?
        ) {
            data class Center(
                @SerializedName("cn")
                var cn: String?,
                @SerializedName("code")
                var code: String?,
                @SerializedName("e")
                var e: String?,
                @SerializedName("s")
                var s: String?,
                @SerializedName("sort_code")
                var sortCode: Any?,
                @SerializedName("u")
                var u: String?,
                @SerializedName("ud")
                var ud: String?
            )
        }
    }
}