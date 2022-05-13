package andlima.hafizhfy.challengeenam.fragment_main.presenter

import andlima.hafizhfy.challengeenam.MainActivity
import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.datastore.UserManager
import andlima.hafizhfy.challengeenam.func.toast
import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem
import andlima.hafizhfy.challengeenam.network.ApiClient
import andlima.hafizhfy.challengeenam.room.FavoriteFilmDatabase
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class FilmFavoritePresenter(val favFilmView: FilmFavoriteView) {

    // Get data store
    lateinit var userManager: UserManager

    // Get local room database
    private var mDb : FavoriteFilmDatabase? = null

    fun getUserId(context: Context, lifecycleOwner: LifecycleOwner, onIdFound: (Int)->Unit) : Int {
        // Get something from data store
        userManager = UserManager(context)

        var id = 0
        userManager.id.asLiveData().observe(lifecycleOwner, {
           id = it.toInt()
        })

        return id
    }

    fun getUsersFavoriteFilms(context: Context, lifecycleOwner: LifecycleOwner, activity: Activity) {
        // Get something from data store
        userManager = UserManager(context)

        // Get room database instance
        mDb = FavoriteFilmDatabase.getInstance(context)

        GlobalScope.launch {
            activity.runOnUiThread {
                userManager.id.asLiveData().observe(lifecycleOwner, { userId ->
    //                mDb?.favFilmDao()?.deleteSpecificFilm("Star Wars : End game")
                    val listData = mDb?.favFilmDao()?.getUsersFavoriteFilm(userId.toInt())
//                    val listData = mDb?.favFilmDao()?.getAllFavoriteFilm()

                    if (listData != null) {
                        if (listData.isNotEmpty()) {
                            favFilmView.onSuccess("WE DID IT", listData)
                        } else {
                            favFilmView.onError("You have not add any favorite film")
                        }
                    } else {
                        favFilmView.onError("data null")
                    }
                })
            }
        }
    }
}