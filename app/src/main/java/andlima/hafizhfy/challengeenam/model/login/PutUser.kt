package andlima.hafizhfy.challengeenam.model.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PutUser(
    @SerializedName("dateofbirth")
    val dateofbirth: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("complete_name")
    val completeName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String
) : Parcelable
