package fr.isen.knackisen.androidprojet.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.isen.knackisen.androidprojet.fragment.LeftFragment
import fr.isen.knackisen.androidprojet.fragment.MidFragment
import fr.isen.knackisen.androidprojet.fragment.RightFragment

private const val NUM_TABS = 3

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return LeftFragment()
            1 -> return MidFragment()
        }
        return RightFragment()
    }
}