package andlima.hafizhfy.challengeenam.fragment_main.presenter

import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem

interface FilmDetailView {
    fun onProcessed(detail: GetAllFilmResponseItem)
    fun onError(msg: String)
}