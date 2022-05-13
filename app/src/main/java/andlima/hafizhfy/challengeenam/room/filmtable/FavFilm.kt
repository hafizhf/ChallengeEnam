package andlima.hafizhfy.challengeenam.room.filmtable

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class FavFilm(
    @PrimaryKey(autoGenerate = false)
    var id: Int?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "synopsis")
    val synopsis: String?,

    @ColumnInfo(name = "release_date")
    val release_date: String?,

    @ColumnInfo(name = "image")
    val image: String?,

    @ColumnInfo(name = "director")
    val director: String?,

    @ColumnInfo(name = "ownerId")
    val ownerId: Int?,
) : Parcelable
