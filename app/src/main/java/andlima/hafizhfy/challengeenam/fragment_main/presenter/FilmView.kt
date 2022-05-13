package andlima.hafizhfy.challengeenam.fragment_main.presenter

import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem

interface FilmView {
    fun onSuccess(msg: String, film: List<GetAllFilmResponseItem>)
    fun onError(msg: String)
}