package andlima.hafizhfy.challengeenam.fragment_main

import andlima.hafizhfy.challengeenam.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.SplashActivity
import andlima.hafizhfy.challengeenam.datastore.UserManager
import andlima.hafizhfy.challengeenam.func.alertDialog
import andlima.hafizhfy.challengeenam.func.latestUserData
import andlima.hafizhfy.challengeenam.func.toast
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    // Get shared preferences
    private val sharedPrefFile = "logininfo"

    // Get data store
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from shared preference
        val sharedPreferences : SharedPreferences =
            requireContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        // Get something from data store
        userManager = UserManager(requireContext())

//        val id = sharedPreferences.getString("id_key", "id_key")
//        val avatar = sharedPreferences.getString("avatar_key", "avatar_key")
//        val username = sharedPreferences.getString("username_key", "username_key")
//        val email = sharedPreferences.getString("email_key", "email_key")
//
//        val completeName = sharedPreferences.getString("complete_name_key", "complete_name_key")
//        val address = sharedPreferences.getString("address_key", "address_key")
//        val dateOfBirth = sharedPreferences.getString("dateofbirth_key", "dateofbirth_key")

        var id = ""
        var avatar = ""
        var username = ""
        var email = ""

        var completeName = ""
        var address = ""
        var dateOfBirth = ""

        userManager.id.asLiveData().observe(this, { a ->
            id = a.toString()

            userManager.avatar.asLiveData().observe(this, { b ->
                avatar = b.toString()

                userManager.username.asLiveData().observe(this, { c ->
                    username = c.toString()

                    userManager.email.asLiveData().observe(this, { d ->
                        email = d.toString()

                        userManager.completeName.asLiveData().observe(this, { e ->
                            completeName = e.toString()

                            userManager.address.asLiveData().observe(this, { f ->
                                address = f.toString()

                                userManager.dateOfBirth.asLiveData().observe(this, { g ->
                                    dateOfBirth = g.toString()

                                    // Show data from SharedPreferences into things in profile layout
                                    if (Patterns.WEB_URL.matcher(avatar).matches()) {
                                        Glide.with(this).load(avatar).into(iv_image_detail)
                                    } else {
                                        val uri = Uri.parse(avatar)
                                        iv_image_detail.setImageURI(uri)
                                    }
                                    tv_username_detail.text = username
                                    tv_email_detail.text = email

                                    if (completeName == "complete_name $id" || completeName == "") {
                                        tv_complete_name.visibility = View.GONE
                                    } else {
                                        tv_complete_name.text = completeName
                                    }

                                    if (address == "address $id" || address == "") {
                                        tv_address.visibility = View.GONE
                                    } else {
                                        tv_address.text = address
                                    }

                                    if (dateOfBirth == "dateofbirth $id" || dateOfBirth == "") {
                                        tv_date_of_birth.visibility = View.GONE
                                    } else {
                                        tv_date_of_birth.text = dateOfBirth
                                    }
                                })
                            })
                        })
                    })
                })
            })
        })

        btn_goto_edit_profile.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        btn_logout.setOnClickListener {
            alertDialog(requireContext(), "Logout", "Are you sure want to logout?") {
                GlobalScope.launch {
                    userManager.clearData()
                }

                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment)
                toast(requireContext(), "You're logged out")
            }

        }

        // Try to get latest user data -------------------------------------------------------------
//        (requireContext() as MainActivity).runOnUiThread {
//            GlobalScope.async {
//                latestUserData(requireContext(), this@ProfileFragment) {
//                    startActivity(Intent((requireContext() as MainActivity), MainActivity::class.java))
//                    (requireContext() as MainActivity).finish()
//                }
//            }
//        }
    }
}