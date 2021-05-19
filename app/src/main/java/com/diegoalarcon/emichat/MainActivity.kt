package com.diegoalarcon.emichat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.diegoalarcon.emichat.adapters.PagerAdapter
import com.diegoalarcon.emichat.fragments.ChatFragment
import com.diegoalarcon.emichat.fragments.InfoFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var prevBottomSelected: MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViewPager(getPageAdapter())
        setUpBottomNavigationBar()
    }
    private fun getPageAdapter(): PagerAdapter{
        var adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(InfoFragment())
        //adapter.addFragment(RatesFragment())
        adapter.addFragment(ChatFragment())
        return adapter
    }
    private fun setUpViewPager(adapter:PagerAdapter){
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2 //adapter.count //solo si no son muchos adapters de fragment, porque consumira muchos recursos
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(prevBottomSelected == null) {
                    bottomNavigation.menu.getItem(0).isChecked = false
                } else {
                    prevBottomSelected!!.isChecked = false
                }
                bottomNavigation.menu.getItem(position).isChecked = true
                prevBottomSelected = bottomNavigation.menu.getItem(position)
            }

        })
    }
    private fun setUpBottomNavigationBar(){
        bottomNavigation.setOnNavigationItemSelectedListener {item ->
            when (item.itemId){
                R.id.bottom_nav_info -> {
                    viewPager.currentItem = 0; true
                }
                /*R.id.bottom_nav_rates ->{
                    viewPager.currentItem = 1; true
                }*/
                R.id.bottom_nav_chat ->{
                    viewPager.currentItem = 1; true
                }
               else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.general_options_menu, menu)

        return true//super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_new_message -> {

            }
            R.id.menu_log_out -> {
                mAuth.signOut()
                val goToLoginActivity = Intent(this, LoginActivity::class.java)
                goToLoginActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(goToLoginActivity)


            }
        }
        return super.onOptionsItemSelected(item)
    }
}
