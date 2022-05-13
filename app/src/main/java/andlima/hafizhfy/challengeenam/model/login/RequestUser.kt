package andlima.hafizhfy.challengeenam.model.login

import com.google.gson.annotations.SerializedName

data class RequestUser(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)
