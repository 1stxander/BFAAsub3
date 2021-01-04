package com.sesingkat.githubuserappsub3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sesingkat.githubuserappsub3.R
import com.sesingkat.githubuserappsub3.User
import kotlinx.android.synthetic.main.item_user.view.*

var followingList = ArrayList<User>()
class FollowingAdapter(listUser: ArrayList<User>) : RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {
    init {
        followingList = listUser
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

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = followingList[position]
        Glide.with(holder.itemView.context)
            .load(data.avatar)
            .apply(RequestOptions().override(100, 100))
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(holder.imgAvatar)
        holder.txtUsername.text = data.username
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = followingList.size


    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imgAvatar: ImageView = itemView.img_avatar
        var txtUsername: TextView = itemView.txt_username
//        var txtName: TextView = itemView.tv_name_received

    }
}