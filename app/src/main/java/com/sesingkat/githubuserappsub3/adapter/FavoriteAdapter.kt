package com.sesingkat.githubuserappsub3.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sesingkat.githubuserappsub3.*
import com.sesingkat.githubuserappsub3.adapter.FavoriteAdapter.ListViewHolder
import kotlinx.android.synthetic.main.activity_detail.view.*

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<ListViewHolder>() {

    var listFavorite = ArrayList<FavData>()
    set(listFavorite) {
        if (listFavorite.size > 0) {
            this.listFavorite.clear()
        }
        this.listFavorite.addAll(listFavorite)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fav: FavData) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(fav.avatar)
                    .apply(RequestOptions().override(100,100))
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(itemView.iv_avatar_received)
                tv_username_received.text = fav.username
                tv_name_received.text = fav.name
                tv_location_received.text = fav.location.toString()
                tv_company_received.text = fav.company.toString()
                tv_repository_received.text = fav.repository.toString()
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(v: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                                intent.putExtra(DetailActivity.EXTRA_USER, fav)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )
            }
        }
    }
}