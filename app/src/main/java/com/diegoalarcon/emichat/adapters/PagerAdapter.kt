package com.diegoalarcon.emichat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

internal class PagerAdapter (fm: FragmentManager?) : FragmentPagerAdapter(fm!!){
   private val fragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int) = fragmentList[position]

    override fun getCount() = fragmentList.size


    fun addFragment(fragment: Fragment) = fragmentList.add(fragment)
}