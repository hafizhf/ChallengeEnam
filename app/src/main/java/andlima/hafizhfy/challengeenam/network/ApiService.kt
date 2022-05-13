package andlima.hafizhfy.challengeenam.network

import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem
import andlima.hafizhfy.challengeenam.model.login.GetUserItem
import andlima.hafizhfy.challengeenam.model.login.PutUser
import andlima.hafizhfy.challengeenam.model.login.RequestUser
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("apifilm.php")
    fun getAllFilm() : Call<List<GetAllFilmResponseItem>>

    // Login service
    @GET("users")
    fun getUser(@Query("email") email : String) : Call<List<GetUserItem>>

    // Register service
    @POST("users")
    fun postUser(@Body request: RequestUser) : Call<GetUserItem>

    // Update profile
    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Body request: PutUser
    ) : Call<GetUserItem>
}