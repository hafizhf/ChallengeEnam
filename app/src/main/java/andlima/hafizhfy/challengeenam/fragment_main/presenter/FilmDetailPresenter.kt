package andlima.hafizhfy.challengeenam.fragment_main.presenter

import andlima.hafizhfy.challengeenam.datastore.UserManager
import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem
import andlima.hafizhfy.challengeenam.room.FavoriteFilmDatabase
import andlima.hafizhfy.challengeenam.room.filmtable.FavFilm
import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FilmDetailPresenter(val detailView: FilmDetailView, val detailFilm: GetAllFilmResponseItem) {

    // Get data store
    lateinit var userManager: UserManager

    // Get local room database
    private var mDb : FavoriteFilmDatabase? = null

    fun getFilmDetail() {
        detailView.onProcessed(detailFilm)
    }

//    @DelicateCoroutinesApi
//    fun isFilmAddedToFavorite(
//        context: Context,
//        lifecycleOwner: LifecycleOwner,
//        activity: Activity,
//        title: String
//    ) {
//        // Get something from data store
//        userManager = UserManager(context)
//
//        // Get room database instance
//        mDb = FavoriteFilmDatabase.getInstance(context)
//
//        var userId = 0
//
//        GlobalScope.launch {
//            activity.runOnUiThread {
//                userManager.id.asLiveData().observe(lifecycleOwner, { owner ->
//                    userId = owner.toInt()
//
//                    val data = mDb?.favFilmDao()?.checkFilmAddedByUser(title, userId)
//
//                    if (data != null) {
//                        if (data.isNotEmpty()) {
////                            detailView.onFilmAddedToFavorite(true)
//                            detailView.onFilmAddedToFavorite(data.size.toString())
//                        } else {
////                            detailView.onFilmAddedToFavorite(false)
//                            detailView.onFilmAddedToFavorite(data.size.toString())
//                        }
//                    } else {
//                        detailView.onError("data null")
//                    }
//                })
//            }
//        }
//    }

//    @DelicateCoroutinesApi
//    suspend fun getFavoriteFilmId(
//        context: Context,
//        lifecycleOwner: LifecycleOwner,
//        activity: Activity,
//        title: String
//    ) : Int {
//        // Get something from data store
//        userManager = UserManager(context)
//
//        var userId = 0
//        var filmId = 0
//
//        GlobalScope.launch {
//            activity.runOnUiThread {
//                userManager.id.asLiveData().observe(lifecycleOwner, { owner ->
//                    userId = owner.toInt()
//                    val data = mDb?.favFilmDao()?.checkFilmAddedByUser(title, userId)
//                    data.let {
//                        if (it != null) {
//                            filmId = it[0].id!!.toInt()
//                        } else {
//                            detailView.onError("data null")
//                        }
//                    }
//                })
//            }
//        }
//
//        delay(100)
//        return filmId
//    }
}