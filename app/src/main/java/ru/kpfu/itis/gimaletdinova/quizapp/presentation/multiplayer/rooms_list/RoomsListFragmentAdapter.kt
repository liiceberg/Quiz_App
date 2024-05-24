package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class RoomsListFragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RoomsListFragment.newInstance(true)
            1 -> RoomsListFragment.newInstance(false)
            else -> throw RuntimeException()
        }
    }

}