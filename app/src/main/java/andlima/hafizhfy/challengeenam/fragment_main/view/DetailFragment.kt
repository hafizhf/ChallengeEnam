package andlima.hafizhfy.challengeenam.fragment_main.view

import andlima.hafizhfy.challengeenam.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.SplashActivity
import andlima.hafizhfy.challengeenam.datastore.UserManager
import andlima.hafizhfy.challengeenam.fragment_main.presenter.FilmDetailPresenter
import andlima.hafizhfy.challengeenam.fragment_main.presenter.FilmDetailView
import andlima.hafizhfy.challengeenam.fragment_main.presenter.FilmPresenter
import andlima.hafizhfy.challengeenam.func.latestUserData
import andlima.hafizhfy.challengeenam.func.snackbarCustom
import andlima.hafizhfy.challengeenam.func.snackbarLong
import andlima.hafizhfy.challengeenam.func.toast
import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem
import andlima.hafizhfy.challengeenam.room.FavoriteFilmDatabase
import andlima.hafizhfy.challengeenam.room.filmtable.FavFilm
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailFragment : Fragment(), FilmDetailView {

    // Get data store
    lateinit var userManager: UserManager

    // Get presenter
    private lateinit var presenter: FilmDetailPresenter

    // Get local room database
    private var mDb : FavoriteFilmDatabase? = null

    // Init floating action button clicked
    private var fabClicked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get room database instance
        mDb = FavoriteFilmDatabase.getInstance(requireContext())

        // Get data from recyclerview selected item
        val selectedData = arguments?.getParcelable<GetAllFilmResponseItem>("SELECTED_DATA") as GetAllFilmResponseItem

        presenter = FilmDetailPresenter(this, selectedData)
        presenter.getFilmDetail()

        userManager.id.asLiveData().observe(this, { userID ->
            Log.d("USER_ID", "$userID")
            val data = mDb?.favFilmDao()?.checkFilmAddedByUser(selectedData.title, userID.toInt())

            fabClicked = data?.size != 0

            if (fabClicked) {
                fab_add_to_favorite.setImageResource(R.drawable.ic_faved)
            } else {
                fab_add_to_favorite.setImageResource(R.drawable.ic_fav)
            }

            fab_add_to_favorite.setOnClickListener {
                if (!fabClicked) {
                    // Add film to favorite
                    val selectedFilm = FavFilm(null,
                        selectedData.title,
                        selectedData.synopsis,
                        selectedData.releaseDate,
                        selectedData.image,
                        selectedData.director,
                        userID.toInt())

                    val addFavorite = mDb?.favFilmDao()?.insertNewFavorite(selectedFilm)

                    if (addFavorite != 0.toLong()) {
                        fab_add_to_favorite.setImageResource(R.drawable.ic_faved)
                        snackbarCustom(
                            requireView(),
                            "Added to favorite",
                            "See Favorite",
                        ) {
                            Navigation.findNavController(view)
                                .navigate(R.id.action_detailFragment_to_favoriteFragment)
                        }
                        fabClicked = true
                    } else {
                        toast(requireContext(), "Failed to add to favorite")
                    }

                } else {

                    GlobalScope.launch {

                        val filmID = mDb?.favFilmDao()?.getFavoriteFilmID(
                            selectedData.title,
                            userID.toInt()
                        )

                        val removeFromFavorite = mDb?.favFilmDao()?.removeFromFavorite(filmID!!)

                        requireActivity().runOnUiThread {
                            if (removeFromFavorite != 0) {
                                fab_add_to_favorite.setImageResource(R.drawable.ic_fav)
                                snackbarLong(requireView(), "Removed from favorite")
                                fabClicked = false
                            } else {
                                toast(requireContext(), "Failed to remove favorite")
                            }
                        }
                    }
                }
            }
        })

        // Try to get latest user data -------------------------------------------------------------
//        (requireContext() as MainActivity).runOnUiThread {
//            GlobalScope.async {
//                latestUserData(requireContext(), this@DetailFragment) {
//                    startActivity(Intent((requireContext() as MainActivity), MainActivity::class.java))
//                    (requireContext() as MainActivity).finish()
//                }
//            }
//        }
    }

    override fun onProcessed(detail: GetAllFilmResponseItem) {
        Glide.with(this).load(detail.image)
            .into(iv_thumbnail_detail)
        tv_title_film.text = detail.title
        tv_director.append(detail.director)
        tv_release_date.append(detail.releaseDate)
        tv_synopsis.text = detail.synopsis
    }

    override fun onError(msg: String) {
        toast(requireContext(), msg)
    }
}