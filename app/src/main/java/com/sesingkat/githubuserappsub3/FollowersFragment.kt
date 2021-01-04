package com.sesingkat.githubuserappsub3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpClient.log
import com.loopj.android.http.AsyncHttpResponseHandler
import com.sesingkat.githubuserappsub3.adapter.FollowersAdapter
import com.sesingkat.githubuserappsub3.adapter.followersList
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception


class FollowersFragment : Fragment() {

    companion object {
        private val TAG = FollowersFragment::class.java.simpleName
        const val EXTRA_USER = "extra_user"
    }

    private var list: ArrayList<User> = ArrayList()
    private lateinit var adapter: FollowersAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowersAdapter(list)
        list.clear()
        val user = activity?.intent?.getParcelableExtra(EXTRA_USER) as User
        getUserFollowers(user.username.toString())
    }

    private fun getUserFollowers(input: String) {
        pbFollowers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$input/followers"
        client.addHeader("Authorization", "token e00a86d2f9725ab9590c3cdffb1fbe2feafcdc6f")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                pbFollowers.visibility = View.INVISIBLE
                val result = String(responseBody)
                log.d(TAG, result)
                try {
                    val items = JSONArray(result)
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username: String = item.getString("login")
                        getDetailUser(username)
                    }
                }catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                pbFollowers.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDetailUser(input: String) {
        pbFollowers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$input"
        client.addHeader("Authorization", "token e00a86d2f9725ab9590c3cdffb1fbe2feafcdc6f")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                pbFollowers.visibility = View.INVISIBLE
                val result = String(responseBody)
                log.d(TAG, result)
                try {
                    val item = JSONObject(result)

                    val avatar: String? = item.getString("avatar_url")
                    val username: String? = item.getString("login")
                    val name: String? = item.getString("name")
                    val location: String? = item.getString("location")
                    val company: String? = item.getString("company")
                    val repository: String? = item.getString("public_repos")
                    val follwers: String? = item.getString("followers")
                    val following: String? = item.getString("following")
                    list.add(
                        User(
                            avatar,
                            username,
                            name,
                            location,
                            company,
                            repository,
                            follwers,
                            following
                        )
                    )
                    showRecyclerList()
                }catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                pbFollowers.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerList() {
        rv_followers.layoutManager = LinearLayoutManager(activity)
        val listAdapter = FollowersAdapter(followersList)
        rv_followers.adapter = adapter

        listAdapter.setOnItemClickCallback(object : FollowersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {

            }
        })
    }
}