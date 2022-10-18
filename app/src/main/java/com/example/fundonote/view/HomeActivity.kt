package com.example.fundonote.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.fundonote.R
import com.example.fundonote.HomeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView

class HomeActivity : AppCompatActivity() {
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val mainDrawer:DrawerLayout = findViewById(R.id.MainDrawer)
        var toolBar:Toolbar = findViewById(R.id.TOOLBAR)
        setSupportActionBar(toolBar)
     //   toolBar.showOverflowMenu()
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, mainDrawer,toolBar,
            R.string.open,
            R.string.close
        )
        mainDrawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.syncState()

        replaceFragment(HomeFragment())


    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.search_view,menu)
//        var menuItem : MenuItem = menu!!.findItem(R.id.account_Profile)
//        var view : View = MenuItemCompat.getActionView(menuItem)
//        var profileImage:CircleImageView = view.findViewById(R.id.toolBar_profile_image)
//
//        Glide
//            .with(this)
//            .load("https://images.pexels.com/photos/771742/pexels-photo-771742.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
//            .into(profileImage)
//
//        profileImage.setOnClickListener(View.OnClickListener{
//            Toast.makeText(this,"profile click",Toast.LENGTH_LONG).show()
//            var dialog = ProfileFragmet()
//            dialog.show(supportFragmentManager,"customDialog")
//        })
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
           R.id.account_Profile ->{
               Toast.makeText(this,"profile click",Toast.LENGTH_LONG).show()
           }
        }
        return super.onOptionsItemSelected(item)
    }
    fun replaceFragment(fragment: Fragment){
        val supportFragment = supportFragmentManager
        val fragment_Transaction = supportFragment.beginTransaction()
        fragment_Transaction.replace(R.id.fragmaintContainer, fragment)
        fragment_Transaction.commit()
    }

}