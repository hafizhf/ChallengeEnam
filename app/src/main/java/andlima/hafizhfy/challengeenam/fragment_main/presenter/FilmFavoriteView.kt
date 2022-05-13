package andlima.hafizhfy.challengeenam.fragment_main.presenter

import andlima.hafizhfy.challengeenam.room.filmtable.FavFilm

interface FilmFavoriteView {
    fun onSuccess(msg: String, favFilm: List<FavFilm>)
    fun onError(msg: String)
}