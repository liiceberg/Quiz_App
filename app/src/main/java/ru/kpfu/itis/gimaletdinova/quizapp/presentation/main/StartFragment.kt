package ru.kpfu.itis.gimaletdinova.quizapp.presentation.main

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentStartBinding
@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    private val binding: FragmentStartBinding by viewBinding(
        FragmentStartBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            profileBtn.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_profileFragment)
            }
            playBtn.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_categoriesFragment)
            }
            playWithFriendsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_multiplayerOptionsFragment)
            }
            playOnlineBtn.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_roomsListFragment)
            }
        }
    }
}