package ru.ppr.pprtest.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.ppr.pprtest.NumbersFragment

class PagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position ) {
            0 -> NumbersFragment.newInstance(NumbersFragment.PRIME_NUMBERS_TYPE)
            1 -> NumbersFragment.newInstance(NumbersFragment.FIBONACCI_NUMBERS_TYPE)
            else -> Fragment()
        }
    }
}