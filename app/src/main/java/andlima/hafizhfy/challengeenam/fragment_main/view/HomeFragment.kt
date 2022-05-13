package andlima.hafizhfy.challengeenam.fragment_main.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.adapter.AdapterFilm
import andlima.hafizhfy.challengeenam.datastore.UserManager
import andlima.hafizhfy.challengeenam.fragment_main.presenter.FilmPresenter
import andlima.hafizhfy.challengeenam.fragment_main.presenter.FilmView
import andlima.hafizhfy.challengeenam.func.toast
import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem
import andlima.hafizhfy.challengeenam.viewmodel.ViewModelFilm
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.DelicateCoroutinesApi

class HomeFragment : Fragment(), FilmView {

//    lateinit var adapterFilm: AdapterFilm
//    lateinit var dataFilm: List<GetAllFilmResponseItem>

    // Get data store
    lateinit var userManager: UserManager

    // Used for double back to exit app
    private var doubleBackToExit = false

    // Get presenter
    private lateinit var presenter: FilmPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get all film data from presenter
        presenter = FilmPresenter(this) {
            // Hide loading when
            nothing_handler.visibility = View.GONE
            loading_content.visibility = View.GONE
        }
        presenter.getFilmData()

        // Show username from data store
        userManager.username.asLiveData().observe(this, {
            tv_username.text = it.toString()
        })

        // Check if user click back button twice
        doubleBackExit()

//        initRecycler()
//        getDataFilm()

        btn_goto_profile.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }

        btn_goto_favorite.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_favoriteFragment)
        }

        // Try to get latest user data -------------------------------------------------------------
//        (requireContext() as MainActivity).runOnUiThread {
//            GlobalScope.async {
//                latestUserData(requireContext(), this@HomeFragment, (requireContext() as MainActivity)) {
//                    startActivity(Intent((requireContext() as MainActivity), MainActivity::class.java))
//                    (requireContext() as MainActivity).finish()
//                }
//            }
//        }


    }

//    fun initRecycler() {
//        rv_film_list.layoutManager = LinearLayoutManager(requireContext())
//
//        adapterFilm = AdapterFilm {
//            // Kode buat masuk ke detail selected film
//            val selectedData = bundleOf("SELECTED_DATA" to it)
//            Navigation.findNavController(view!!)
//                .navigate(R.id.action_homeFragment_to_detailFragment, selectedData)
//        }
//
//        rv_film_list.adapter = adapterFilm
//    }

    override fun onSuccess(msg: String, film: List<GetAllFilmResponseItem>) {
        rv_film_list.layoutManager = LinearLayoutManager(requireContext())

        rv_film_list.adapter = AdapterFilm(film) {
            // Kode buat masuk ke detail selected film
            val selectedData = bundleOf("SELECTED_DATA" to it)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_detailFragment, selectedData)
        }
    }

    override fun onError(msg: String) {
        toast(requireContext(), msg)
    }

//    @DelicateCoroutinesApi
//    override fun onResume() {
//        super.onResume()
//        (requireContext() as MainActivity).runOnUiThread {
//            latestUserData(requireContext(), this, (requireContext() as MainActivity)) {
//                activity?.fragmentManager?.popBackStack()
//                startActivity(Intent((requireContext() as MainActivity), MainActivity::class.java))
//                (requireContext() as MainActivity).finish()
//            }
//        }
//    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun getDataFilm() {
//        val viewModel = ViewModelProvider(this).get(ViewModelFilm::class.java)
//        viewModel.liveDataFilm.observe(this, Observer {
//            if (it != null) {
//                adapterFilm.setDataFilm(it)
//                adapterFilm.notifyDataSetChanged()
//            } else {
//                toast(requireContext(), "it null")
//            }
//        })
//        viewModel.makeApiFilm(requireContext()) {
//            nothing_handler.visibility = View.GONE
//            loading_content.visibility = View.GONE
//        }
//    }

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
}