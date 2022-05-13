package andlima.hafizhfy.challengeenam.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetAllFilmResponseItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("director")
    val director: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("synopsis")
    val synopsis: String,
    @SerializedName("title")
    val title: String

//    @SerializedName("release_date")
//    val releaseDate: String,
//    @SerializedName("title")
//    val title: String,
//    @SerializedName("image")
//    val image: String,
//    @SerializedName("director")
//    val director: String,
//    @SerializedName("synopsis")
//    val synopsis: String,
//    @SerializedName("id")
//    val id: String
) : Parcelable