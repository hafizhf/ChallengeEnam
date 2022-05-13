package andlima.hafizhfy.challengeenam.room.filmtable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavFilmDao {
    @Insert
    fun insertNewFavorite(favFilm: FavFilm) : Long

    @Query("SELECT * FROM FavFilm ORDER BY id DESC")
    fun getAllFavoriteFilm() : List<FavFilm>

    @Query("SELECT * FROM FavFilm WHERE ownerId = :ownerId ORDER BY id DESC")
    fun getUsersFavoriteFilm(ownerId : Int) : List<FavFilm>

    @Query("SELECT * FROM FavFilm WHERE title = :title AND ownerId = :ownerId")
    fun checkFilmAddedByUser(title: String, ownerId: Int) : List<FavFilm>

    @Query("SELECT id FROM FavFilm WHERE title = :title AND ownerId = :ownerId")
    fun getFavoriteFilmID(title: String, ownerId: Int) : Int

    @Delete
    fun deleteFilm(favFilm: FavFilm) : Int

    @Query("DELETE FROM FavFilm WHERE id = :id")
    fun removeFromFavorite(id: Int) : Int

    @Query("DELETE FROM FavFilm WHERE title = :title")
    fun deleteSpecificFilm(title: String) : Int
}