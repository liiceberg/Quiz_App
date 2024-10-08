package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentRoomsListContainerBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment

class RoomsListContainerFragment : BaseFragment(R.layout.fragment_rooms_list_container) {

    private val binding: FragmentRoomsListContainerBinding by viewBinding(
        FragmentRoomsListContainerBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initTabs()

        with(binding) {
            createBtn.setOnClickListener {
                findNavController().navigate(
                    RoomsListContainerFragmentDirections.actionRoomsListContainerFragmentToCreateRoomFragment()
                )
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