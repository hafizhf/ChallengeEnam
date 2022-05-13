package andlima.hafizhfy.challengeenam.model


import com.google.gson.annotations.SerializedName

data class TestItem(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("favorite")
    val favorite: List<Any>,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)