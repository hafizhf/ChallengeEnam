package andlima.hafizhfy.challengeenam.adapter

import andlima.hafizhfy.challengeenam.R
import andlima.hafizhfy.challengeenam.model.GetAllFilmResponseItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_film.view.*

class AdapterFilm(
    private var dataFilm : List<GetAllFilmResponseItem>,
    private var onClick : (GetAllFilmResponseItem)-> Unit
) : RecyclerView.Adapter<AdapterFilm.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_film, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.let {
            Glide.with(holder.itemView.context).load(dataFilm[position].image)
                .into(holder.itemView.iv_thumbnail_film)
        }
        holder.itemView.tv_judul_film.text = dataFilm[position].title
        holder.itemView.tv_tanggal.text = dataFilm[position].releaseDate
        holder.itemView.tv_sutradara.text = dataFilm[position].director

        holder.itemView.item.setOnClickListener {
            onClick(dataFilm[position])
        }
    }

    override fun getItemCount(): Int {
        return dataFilm.size
    }
}