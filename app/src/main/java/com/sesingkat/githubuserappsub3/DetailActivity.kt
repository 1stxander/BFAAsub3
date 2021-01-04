package com.sesingkat.githubuserappsub3

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sesingkat.githubuserappsub3.adapter.SectionsPagerAdapter
import com.sesingkat.githubuserappsub3.db.UserContract
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.COLUMN_NAME_COMPANY
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.COLUMN_NAME_FAVORITE
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.COLUMN_NAME_LOCATION
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.COLUMN_NAME_NAME
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.COLUMN_NAME_REPOSITORY
import com.sesingkat.githubuserappsub3.db.UserHelper
import cz.msebera.android.httpclient.client.methods.RequestBuilder.put
import kotlinx.android.synthetic.main.activity_detail.*
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.CONTENT_URI
import kotlinx.android.synthetic.main.item_user.*
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.coroutines.DEBUG_PROPERTY_NAME

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_POSITION = "extra_position"
    }

    private var statusFavorite = false
    private var favorites: FavData? = null
    private lateinit var favHelper: UserHelper
    private lateinit var imgAvatar: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favHelper = UserHelper.getInstance(applicationContext)
        favHelper.open()

        favorites = intent.getParcelableExtra(EXTRA_DATA)
        if (favorites != null) {
            statusFavorite = true
            setUserObject()
            val fav: Int = R.drawable.ic_baseline_favorite_24
            btn_fav.setImageResource(fav)
        } else {
            getUser()
        }

        viewPagerForm()
        btn_fav.setOnClickListener(this)
    }

    private fun viewPagerForm() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    private fun getUser() {
        val user = intent.getParcelableExtra(EXTRA_USER) as User
        user.name?.let { setActionBarTitle(it) }
        Glide.with(this)
            .load(user.avatar)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(iv_avatar_received)
        tv_username_received.text = user.username
        tv_name_received.text = user.name
        tv_location_received.text = getString(R.string.location, user.location)
        tv_company_received.text = getString(R.string.company, user.company)
        tv_repository_received.text = getString(R.string.repository, user.repository)
    }

    @SuppressLint("SetTextI18n")
    private fun setUserObject() {
        val favoriteUser = intent.getParcelableExtra(EXTRA_DATA) as FavData
        favoriteUser.name?.let { setActionBarTitle(it) }
        tv_username_received.text = favoriteUser.username
        tv_name_received.text = favoriteUser.name
        tv_location_received.text = favoriteUser.location
        tv_company_received.text = favoriteUser.company
        tv_repository_received.text = favoriteUser.repository
        Glide.with(this)
            .load(favoriteUser.avatar)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(iv_avatar_received)
        imgAvatar = favoriteUser.avatar.toString()
    }

    override fun onClick(view: View) {
        val fav: Int = R.drawable.ic_baseline_favorite_24
        val unFav: Int = R.drawable.ic_baseline_favorite_border_24
        if (view.id == R.id.btn_fav) {
            if (statusFavorite) {
                favHelper.deleteById(favorites?.username.toString())
                Toast.makeText(this, getString(R.string.remove_fav), Toast.LENGTH_SHORT).show()
                btn_fav.setImageResource(unFav)
                statusFavorite = false
            } else {
                val ivAvatar = imgAvatar
                val tvUsername = tv_username_received.text.toString()
                val tvName = tv_name_received.text.toString()
                val tvLocation = tv_location_received.text.toString()
                val tvCompany = tv_company_received.text.toString()
                val tvRepository = tv_repository_received.text.toString()
                val dataFavorite = "1"

                val values = ContentValues()
                values.put(COLUMN_NAME_AVATAR, ivAvatar)
                values.put(COLUMN_NAME_USERNAME, tvUsername)
                values.put(COLUMN_NAME_NAME, tvName)
                values.put(COLUMN_NAME_LOCATION, tvLocation)
                values.put(COLUMN_NAME_COMPANY, tvCompany)
                values.put(COLUMN_NAME_REPOSITORY, tvRepository)
                values.put(COLUMN_NAME_FAVORITE, dataFavorite)

                statusFavorite = true
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.add_fav), Toast.LENGTH_SHORT).show()
                btn_fav.setImageResource(fav)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (statusFavorite) {
            menuInflater.inflate(R.menu.option_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_fav -> {
                val mIntent = Intent(this, FavActivity::class.java)
                startActivity(mIntent)
            }
            R.id.menu_lang -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.menu_setting -> {
                val mIntent = Intent(this, SettingsActivity::class.java)
                startActivity(mIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}