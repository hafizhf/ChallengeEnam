package andlima.hafizhfy.challengeenam.fragment_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.datastore.UserManager
import andlima.hafizhfy.challengeenam.func.alertDialog
import andlima.hafizhfy.challengeenam.func.snackbarLong
import andlima.hafizhfy.challengeenam.func.toast
import andlima.hafizhfy.challengeenam.model.login.GetUserItem
import andlima.hafizhfy.challengeenam.model.login.PutUser
import andlima.hafizhfy.challengeenam.network.ApiClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class EditProfileFragment : Fragment() {

    // Get data store
    lateinit var userManager: UserManager
    lateinit var imageURI: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        imageURI = ""

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

                                    et_edit_name.setText(username)
                                    et_edit_email.setText(email)
                                    et_edit_avatar.setText(avatar)
                                    if (Patterns.WEB_URL.matcher(avatar).matches()) {
                                        Glide.with(this).load(avatar).into(iv_edit_image)
                                    } else {
                                        val uri = Uri.parse(avatar)
                                        iv_edit_image.setImageURI(uri)
                                    }

                                    if (completeName == "complete_name $id" || completeName == "") {
                                        et_edit_complete_name.setText("")
                                    } else {
                                        et_edit_complete_name.setText(completeName)
                                    }

                                    if (dateOfBirth == "dateofbirth $id" || dateOfBirth == "") {
                                        et_edit_dateofbirth.setText("")
                                    }else {
                                        et_edit_dateofbirth.setText(dateOfBirth)
                                    }

                                    if (address == "address $id" || address == "") {
                                        et_edit_address.setText("")
                                    } else {
                                        et_edit_address.setText(address)
                                    }

                                    btn_edit_image.setOnClickListener {
                                        pickImageFromGallery()
                                    }

                                    btn_save_profile.setOnClickListener {
                                        when {
                                            et_edit_name.text.toString() == "" -> {
                                                toast(requireContext(), "Username field cannot be empty")
                                            }
                                            et_edit_email.text.toString() == "" -> {
                                                toast(requireContext(), "Email field cannot be empty")
                                            }
                                            else -> {
//                                                alertDialog(requireContext(), "Image URI empty?", imageURI) {}
                                                updateProfile(
                                                    id!!.toInt(),
                                                    et_edit_dateofbirth.text.toString(),
                                                    et_edit_address.text.toString(),
                                                    et_edit_avatar.text.toString(),
                                                    et_edit_complete_name.text.toString(),
                                                    et_edit_email.text.toString(),
                                                    et_edit_name.text.toString(),
                                                    imageURI
                                                )
                                            }
                                        }
                                    }
                                })
                            })
                        })
                    })
                })
            })
        })
    }

    private fun updateProfile(
        id: Int,
        dateofbirth: String,
        address: String,
        avatar: String,
        complete_name: String,
        email: String,
        username: String,
        imageURI: String
    ) {
        ApiClient.instanceUser
            .updateUser(id, PutUser(dateofbirth, address, avatar, complete_name, email, username))
            .enqueue(object : retrofit2.Callback<GetUserItem>{
                override fun onResponse(
                    call: Call<GetUserItem>,
                    response: Response<GetUserItem>
                ) {
                    if (response.isSuccessful) {

                        // Get something from data store
                        userManager = UserManager(requireContext())

                        userManager.id.asLiveData().observe(this@EditProfileFragment, { userID ->
                            userManager.password.asLiveData().observe(this@EditProfileFragment, { pwd ->

                                GlobalScope.launch {
                                    userManager.clearData()

                                    if (imageURI != "") {
                                        userManager.loginUserData(
                                            response.body()!!.username,
                                            response.body()!!.email,
                                            imageURI,
                                            pwd,
                                            userID,
                                            response.body()!!.complete_name,
                                            response.body()!!.address,
                                            response.body()!!.dateofbirth
                                        )
                                    } else {
                                        userManager.loginUserData(
                                            response.body()!!.username,
                                            response.body()!!.email,
                                            response.body()!!.avatar,
                                            pwd,
                                            userID,
                                            response.body()!!.complete_name,
                                            response.body()!!.address,
                                            response.body()!!.dateofbirth
                                        )
                                    }
                                }
                            })
                        })

                        snackbarLong(requireView(), "Update saved")

                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_editProfileFragment_to_homeFragment)
                    } else {
                        alertDialog(
                            requireContext(),
                            "Update profile failed",
                            response.message() +"\n\nTry again"
                        ) {}
                    }
                }

                override fun onFailure(call: Call<GetUserItem>, t: Throwable) {
                    alertDialog(
                        requireContext(),
                        "Update profile error",
                        "${t.message}"
                    ) {}
                }

            })
    }

    private fun pickImageFromGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            iv_edit_image.setImageURI(data?.data)
            getImageURI(data?.data.toString())
//            alertDialog(requireContext(), "Image URI", Patterns.WEB_URL.matcher(data?.data.toString()).matches().toString()) {}
        }
    }

    private fun getImageURI(uri: String) {
        this.imageURI = uri
    }

}