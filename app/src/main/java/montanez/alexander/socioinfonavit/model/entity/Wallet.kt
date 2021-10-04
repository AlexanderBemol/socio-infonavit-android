package montanez.alexander.socioinfonavit.model.entity

import com.google.gson.annotations.SerializedName

data class Wallet(
    val id : Int,
    @SerializedName("display_index") val displayIndex : Int,
    @SerializedName("display_text") val displayText : String,
    var benevits: List<Benevit>? = listOf()
)
