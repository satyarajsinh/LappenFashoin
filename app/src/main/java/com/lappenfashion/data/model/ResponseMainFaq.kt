package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainFaq(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("faq_question")
        var faqQuestion: List<FaqQuestion?>?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("title")
        var title: String?
    ) {
        data class FaqQuestion(
            @SerializedName("ans")
            var ans: String?,
            @SerializedName("faq_id")
            var faqId: Int?,
            @SerializedName("id")
            var id: Int?,
            @SerializedName("que")
            var que: String?
        )
    }
}