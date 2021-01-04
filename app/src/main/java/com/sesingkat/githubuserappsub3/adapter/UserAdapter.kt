package com.sesingkat.githubuserappsub3.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sesingkat.githubuserappsub3.DetailActivity
import com.sesingkat.githubuserappsub3.R
import com.sesingkat.githubuserappsub3.User
import kotlinx.android.synthetic.main.item_user.view.*
import java.util.*
import kotlin.collections.ArrayList

var listData = ArrayList<User>()
lateinit var mcontext: Context

class UserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>(), Filterable {
    init {
        listData = listUser
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        val v = ListViewHolder(view)
        mcontext = parent.context
        return v
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            val data = listData[position]
            Glide.with(holder.itemView.context)
                .load(data.avatar)
                .apply(RequestOptions().override(100, 100))
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(holder.imgAvatar)
            holder.txtUsername.text = data.username
        holder.itemView.setOnClickListener {
            val user = User(
                data.avatar,
                data.username,
                data.name,
                data.location,
                data.company,
                data.repository,
                data.followers,
                data.following
            )
            val intentDetail = Intent(mcontext, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_USER, user)
            mcontext.startActivity(intentDetail)
        }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: ImageView = itemView.img_avatar
        var txtUsername: TextView = itemView.txt_username
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val bySearch = constraint.toString()
                listData = if (bySearch.isEmpty()) {
                    listUser
                } else {
                    val resultList = ArrayList<User>()
                    for (row in listData) {
                        if ((row.username.toString().toLowerCase(Locale.ROOT)
                                .contains(bySearch.toLowerCase(Locale.ROOT)))) {
                            resultList.add(
                                User(
                                    row.avatar,
                                    row.username,
                                    row.name,
                                    row.company,
                                    row.location,
                                    row.repository,
                                    row.followers,
                                    row.following
                                )
                            )
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listData
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                listData = results.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }
}


