package andlima.hafizhfy.challengeenam.func

import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.datastore.UserManager
import andlima.hafizhfy.challengeenam.model.login.GetUserItem
import andlima.hafizhfy.challengeenam.network.ApiClient
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import java.util.logging.Handler
import kotlin.concurrent.schedule

// Function to easy making Toast -------------------------------------------------------------------
fun toast(context: Context, message : String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
fun snackbarLong(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Funtion to easy making Snackbar with custom action ----------------------------------------------
fun snackbarCustom(view: View, message: String, buttonText: String, action: Any.() -> Unit) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snack.setAction(buttonText) {
        action(true)
    }
    snack.show()
}

// Function to easy making SnackBar ----------------------------------------------------------------
fun snackbarIndefinite(view: View, message: String) {
    val snack = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
    snack.setAction("Ok") {
        snack.dismiss()
    }
    snack.show()
}

// Function to easy making AlertDialog -------------------------------------------------------------
fun alertDialog(context: Context, title: String, message: String, action: Any.()->Unit) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            action(true)
        }
        .setCancelable(false)
        .show()
}

// Function to hide all error pop up ---------------------------------------------------------------
fun hideAllPopUp(cardView1: CardView, cardView2: CardView) {
    cardView1.visibility = View.GONE
    cardView2.visibility = View.GONE
}

// Function to show error pop up -------------------------------------------------------------------
fun showPopUp(cardViewID: CardView, textViewID: TextView, message: String) {
    cardViewID.visibility = View.VISIBLE
    textViewID.text = message
}

// Function to hide error pop up -------------------------------------------------------------------
fun hidePopUp(cardViewID: CardView) {
    cardViewID.visibility = View.GONE
}

// Function to show password on password EditText --------------------------------------------------
fun showPassword(editText: EditText, imageView: ImageView) {
    val hidden = PasswordTransformationMethod.getInstance()
    val show = HideReturnsTransformationMethod.getInstance()

    if (editText.transformationMethod == hidden) {
        editText.transformationMethod = show
        imageView.setImageResource(R.drawable.ic_eye_off)
    } else {
        editText.transformationMethod = hidden
        imageView.setImageResource(R.drawable.ic_eye)
    }
}

// Function to check match data between SharedPreferences and live database from rest api ----------
suspend fun isDataUserSame(context: Context, lifecycleOwner: LifecycleOwner, activity: Activity) : Boolean {
    // Result code for user data
    // True = Still same, at least email and password
    // False = Password has been changed, or account disappear
    var result = false

    // Get something from data store
    val userManager = UserManager(context)

    // Internal function to clear Data Store
    fun clearDataStore() {
        GlobalScope.launch {
            userManager.clearData()
        }
    }

    // Init variable
    var avatar = ""
    var username = ""
    var email = ""
    var password = ""
    var completeName = ""
    var address = ""
    var dateOfBirth = ""

    activity.runOnUiThread {
        userManager.avatar.asLiveData().observe(lifecycleOwner, { a ->
            avatar = a.toString()

            userManager.username.asLiveData().observe(lifecycleOwner, { b ->
                username = b.toString()

                userManager.email.asLiveData().observe(lifecycleOwner, { c ->
                    email = c.toString()

                    userManager.password.asLiveData().observe(lifecycleOwner, { d ->
                        password = d.toString()

                        userManager.completeName.asLiveData().observe(lifecycleOwner, { e ->
                            completeName = e.toString()

                            userManager.address.asLiveData().observe(lifecycleOwner, { f ->
                                address = f.toString()

                                userManager.dateOfBirth.asLiveData().observe(lifecycleOwner, { g ->
                                    dateOfBirth = g.toString()

                                    // Get latest data from API
                                    activity.runOnUiThread {
                                        ApiClient.instanceUser.getUser(email)
                                            .enqueue(object : retrofit2.Callback<List<GetUserItem>>{
                                                override fun onResponse(
                                                    call: Call<List<GetUserItem>>,
                                                    response: Response<List<GetUserItem>>
                                                ) {
                                                    if (response.isSuccessful) {

                                                        Log.d("a", response.body().toString())

                                                        when {
                                                            response.body()!!.isEmpty() -> {
                                                                toast(context, "Logged out")
                                                                clearDataStore()
                                                                result = false
                                                            }
                                                            response.body()!!.size > 1 -> {
                                                                toast(context, "Redundant account detected\nLogged out")
                                                                clearDataStore()
                                                                result = false
                                                            }
                                                            password != response.body()!![0].password -> {
                                                                toast(context, "Logged out due to password changed")
                                                                clearDataStore()
                                                                result = false
                                                            }
                                                            email != response.body()!![0].email -> {
                                                                toast(context, "Logged out due to email changed")
                                                                clearDataStore()
                                                                result = false
                                                            }
                                                            username != response.body()!![0].username
                                                                    || avatar != response.body()!![0].avatar
                                                                    || completeName != response.body()!![0].complete_name
                                                                    || address != response.body()!![0].address
                                                                    || dateOfBirth != response.body()!![0].dateofbirth -> {


                                                                GlobalScope.launch {
                                                                    // Clear previous user preferences
                                                                    clearDataStore()

                                                                    userManager.loginUserData(
                                                                        response.body()!![0].username,
                                                                        response.body()!![0].email,
                                                                        response.body()!![0].avatar,
                                                                        response.body()!![0].password,
                                                                        response.body()!![0].id,
                                                                        response.body()!![0].complete_name,
                                                                        response.body()!![0].address,
                                                                        response.body()!![0].dateofbirth
                                                                    )
                                                                }

                                                                result = true
                                                            }
                                                            else -> {
                                                                // Nothing changed, let user enjoy app
                                                                result = true
                                                            }
                                                        }
                                                    } else {
                                                        alertDialog(context, "Something went wrong",
                                                            "${response.message()}\n\nPlease restart app or @developer") {}
                                                        result = false
                                                    }
                                                }

                                                override fun onFailure(call: Call<List<GetUserItem>>, t: Throwable) {
                                                    alertDialog(context, "Something went wrong",
                                                        "${t.message}\n\nPlease restart app or @developer") {}
                                                    result = false
                                                }
                                            })
                                    }
                                })
                            })
                        })
                    })
                })
            })
        })
    }

    // Give delay because function can give return faster than the API connection, and it's not good
    delay(3000)
    return result
}

// Function to match user latest data while app is running -------------------------------------
@DelicateCoroutinesApi
//suspend fun latestUserData(context: Context, lifecycleOwner: LifecycleOwner, activity: Activity, dataNotMatchAction : Any.()->Unit) {
//    // Make it as infinity loop but slower hehe
//    var z = 3
//    var isDataMatch : Boolean
//
//    for (i in 1..z) {
//        GlobalScope.launch {
//            isDataMatch = isDataUserSame(context, lifecycleOwner, activity)
//            (delay(10000))
//            if (!isDataMatch) {
//                dataNotMatchAction(true)
//            }
//            z++
//        }
//    }
//}

fun latestUserData(context: Context, lifecycleOwner: LifecycleOwner, activity: Activity, dataNotMatchAction : Any.()->Unit) {
    GlobalScope.async {
        var result = isDataUserSame(context, lifecycleOwner, activity)

        Timer("A", false).schedule(3000) {
            if (!result) {
                dataNotMatchAction(true)
            }
//            latestUserData(context, lifecycleOwner, activity, dataNotMatchAction)
        }
    }

}
