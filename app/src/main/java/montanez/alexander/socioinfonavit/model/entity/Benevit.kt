package montanez.alexander.socioinfonavit.model.entity

import com.google.gson.annotations.SerializedName

data class Benevit(
    val id: Int,
    val name: String,
    val description: String,
    val title: String,
    val active: Boolean,
    @SerializedName("primary_color") val color: String,
    @SerializedName("vector_full_path") val vector: String,
    val ally: Ally,
    val wallet: Wallet,
    var locked: Boolean?
)
