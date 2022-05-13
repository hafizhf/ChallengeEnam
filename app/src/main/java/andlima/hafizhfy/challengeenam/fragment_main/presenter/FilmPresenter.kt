package andlima.hafizhfy.challengeenam.fragment_main.presenter

import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem
import andlima.hafizhfy.challengeenam.network.ApiClient
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import retrofit2.Call
import retrofit2.Response

class FilmPresenter(val filmView: FilmView, val successAction : Any.()->Unit) {
    fun getFilmData() {
        ApiClient.instance.getAllFilm()
            .enqueue(object : retrofit2.Callback<List<GetAllFilmResponseItem>>{
                override fun onResponse(
                    call: Call<List<GetAllFilmResponseItem>>,
                    response: Response<List<GetAllFilmResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        successAction(true)
                        filmView.onSuccess(response.message(), response.body()!!)
                    } else {
                        filmView.onError(response.message())
                    }
                }

                override fun onFailure(call: Call<List<GetAllFilmResponseItem>>, t: Throwable) {
                    filmView.onError(t.message!!)
                }

            })
    }

    fun navigateToDetail(view: View, bundle: Bundle) {
        Navigation.findNavController(view)
            .navigate(R.id.action_homeFragment_to_detailFragment, bundle)

    }
}