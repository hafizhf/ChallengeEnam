package andlima.hafizhfy.challengeenam.room

import andlima.hafizhfy.challengeenam.room.filmtable.FavFilm
import andlima.hafizhfy.challengeenam.room.filmtable.FavFilmDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavFilm::class],
    version = 1
)
abstract class FavoriteFilmDatabase : RoomDatabase() {

    abstract fun favFilmDao() : FavFilmDao

    companion object {
        private var INSTANCE : FavoriteFilmDatabase? = null
        fun getInstance(context: Context) : FavoriteFilmDatabase? {
            synchronized(FavoriteFilmDatabase::class) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                FavoriteFilmDatabase::class.java, "FavoriteFilm.db")
                    .allowMainThreadQueries().build()
            }
            return INSTANCE
        }
    }

    fun destroyInstance() {
        INSTANCE = null
    }
}