package com.sesingkat.githubuserappsub3

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sesingkat.githubuserappsub3.adapter.FavoriteAdapter
import com.sesingkat.githubuserappsub3.db.MappingHelper
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.CONTENT_URI
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        setACtionBarTitle()

        rvFav.layoutManager = LinearLayoutManager(this)
        rvFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        rvFav.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavData>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbarFav.visibility = View.VISIBLE
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
            MappingHelper.mapCursorToArrayList(cursor)
            }
            val favUser = deferredUser.await()
            progressbarFav.visibility = View.INVISIBLE
            if (favUser.size > 0) {
                adapter.listFavorite = favUser
            } else {
                adapter.listFavorite = ArrayList()
                val toast = Toast.makeText(this@FavActivity, getString(R.string.no_data), Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

//    private fun showSnackBarMessage() {
//        Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_SHORT).show()
//    }

    private fun setACtionBarTitle() {
        if (supportActionBar != null) {
            supportActionBar?.title = "Favorite's List"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    override fun onResume() {
        super.onResume()
        loadUserAsync()
    }

}