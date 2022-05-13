/*
*
* ERROR:
* isDataUserSame() function in PublicFunction.kt cannot run livedata,
* it said "Cannot invoke observe on a background thread
*
*/

package andlima.hafizhfy.challengeenam.fragment_start

import andlima.hafizhfy.challengeenam.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.datastore.UserManager
import andlima.hafizhfy.challengeenam.func.*
import andlima.hafizhfy.challengeenam.model.login.GetUserItem
import andlima.hafizhfy.challengeenam.network.ApiClient
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {

    // Used for double back to exit app
    private var doubleBackToExit = false

    // Get shared preferences
    private val sharedPrefFile = "logininfo"

    // Get data store
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get something from shared preference
        val sharedPreferences : SharedPreferences =
            requireContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        // Check if user already logged in
        isLoggedIn(this)

        // Check if user click back button twice
        doubleBackExit()

        // Show password
        btn_show_pwd.setOnClickListener {
            showPassword(et_password, btn_show_pwd)
        }

        // Action for login button
        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            hideAllPopUp(cv_email_popup, cv_password_popup)

            if (email != "" && password != "") {
                loading_login.visibility = View.VISIBLE
                login(email, password, sharedPreferences)
            } else {
                toast(requireContext(), "Please input all field")
            }
        }

        btn_goto_register.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    fun login(email: String, password: String, sharedPreferences: SharedPreferences) {
        ApiClient.instanceUser.getUser(email)
            .enqueue(object : retrofit2.Callback<List<GetUserItem>>{
                override fun onResponse(
                    call: Call<List<GetUserItem>>,
                    response: Response<List<GetUserItem>>
                ) {
                    loading_login.visibility = View.GONE
                    if (response.isSuccessful) {
                        if (response.body()?.isEmpty() == true) {
                            toast(requireContext(), "Unknown user")
                        } else {
                            when {
                                response.body()?.size!! > 1 -> {
                                    toast(requireContext(), "Please input your data correctly")
                                }
                                email != response.body()!![0].email -> {
//                                    toast(requireContext(), "Email not registered")
                                    showPopUp(cv_email_popup, tv_email_popup, "Email not registered")
                                }
                                password != response.body()!![0].password -> {
//                                    toast(requireContext(), "Wrong password")
                                    showPopUp(cv_password_popup, tv_password_popup, "Wrong password")
                                } else -> {

                                // SHARED PREFERENCES BLOCK ----------------------------------------
//                                    val editor : SharedPreferences.Editor = sharedPreferences.edit()
//
//                                    editor.putString("username_key", response.body()!![0].username)
//                                    editor.putString("email_key", response.body()!![0].email)
//                                    editor.putString("avatar_key", response.body()!![0].avatar)
//                                    editor.putString("password_key", response.body()!![0].password)
//
//                                    editor.putString("id_key", response.body()!![0].id)
//                                    editor.putString("complete_name_key", response.body()!![0].complete_name)
//                                    editor.putString("address_key", response.body()!![0].address)
//                                    editor.putString("dateofbirth_key", response.body()!![0].dateofbirth)
//                                    editor.apply()
                                // END OF SHARED PREFERENCES ---------------------------------------

                                    GlobalScope.launch {
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

                                    Navigation.findNavController(view!!)
                                        .navigate(R.id.action_loginFragment_to_homeFragment)
                                }
                            }
                        }
                    } else {
                        alertDialog(requireContext(), "Login failed", response.message()
                                +"\n\nTry again") {}
                    }
                }

                override fun onFailure(call: Call<List<GetUserItem>>, t: Throwable) {
                    loading_login.visibility = View.GONE
                    alertDialog(requireContext(), "Login error", "${t.message}") {}
                }

            })
    }

    // Function to exit app with double click on back button----------------------------------------
    private fun doubleBackExit() {
        activity?.onBackPressedDispatcher
            ?.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (doubleBackToExit) {
                    activity!!.finish()
                } else {
                    doubleBackToExit = true
                    toast(requireContext(), "Press again to exit")

                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        kotlin.run {
                            doubleBackToExit = false
                        }
                    }, 2000)
                }
            }
        })
    }

    // Check if user already logged in -------------------------------------------------------------
    @DelicateCoroutinesApi
    private fun isLoggedIn(lifecycleOwner: LifecycleOwner) {
        var sharedEmail = ""
        var sharedPassword = ""

        userManager.email.asLiveData().observe(lifecycleOwner, { email ->
            sharedEmail = email

            userManager.password.asLiveData().observe(lifecycleOwner, { pwd ->
                sharedPassword = pwd

                var isUserDataFine : Boolean

                loading_check_user_loggedin.visibility = View.VISIBLE
                if (sharedEmail != "" && sharedPassword != "") {
                    GlobalScope.launch {
                        isUserDataFine = isDataUserSame(requireContext(), this@LoginFragment, (requireContext() as MainActivity))
                        Handler(Looper.getMainLooper()).postDelayed({
                            if (isUserDataFine) {
                                Navigation.findNavController(view!!).navigate(R.id.action_loginFragment_to_homeFragment)
                                loading_check_user_loggedin.visibility = View.GONE
                                toast(requireContext(), "Welcome back")
                            } else {
                                loading_check_user_loggedin.visibility = View.GONE
                            }

//                            temp_email.text = sharedEmail
//                            temp_password.text = sharedPassword
                        }, 200)
                    }
                } else {
                    loading_check_user_loggedin.visibility = View.GONE
                }
            })
        })
    }
}