package andlima.hafizhfy.challengeenam.model.login


import com.google.gson.annotations.SerializedName

data class GetUserItem(
    @SerializedName("complete_name")
    val complete_name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("dateofbirth")
    val dateofbirth: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)