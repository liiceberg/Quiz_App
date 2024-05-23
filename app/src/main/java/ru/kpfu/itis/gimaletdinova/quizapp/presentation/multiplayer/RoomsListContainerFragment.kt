package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentRoomsListContainerBinding

class RoomsListContainerFragment : Fragment(R.layout.fragment_rooms_list_container) {

    private val binding: FragmentRoomsListContainerBinding by viewBinding(FragmentRoomsListContainerBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTabs()

        with(binding) {
            createBtn.setOnClickListener {
                findNavController().navigate(R.id.action_roomsListFragmentContainer_to_createRoomFragment)
            }
        }
    }

    private fun initTabs() {
        with(binding) {

            fragmentsVp.apply {

                adapter = RoomsListFragmentAdapter(childFragmentManager, lifecycle)

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        fragmentsTl.getTabAt(position)?.select()
                    }
                })
            }

            fragmentsTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        fragmentsVp.currentItem = it.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

}