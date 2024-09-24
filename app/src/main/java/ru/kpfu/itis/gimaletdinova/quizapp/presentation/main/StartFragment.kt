package ru.kpfu.itis.gimaletdinova.quizapp.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentStartBinding

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    private val binding: FragmentStartBinding by viewBinding(
        FragmentStartBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            profileBtn.setOnClickListener {
                findNavController().navigate(
                    StartFragmentDirections.actionStartFragmentToProfileFragment()
                )
            }
            playBtn.setOnClickListener {
                findNavController().navigate(
                    StartFragmentDirections.actionStartFragmentToCategoriesFragment()
                )
            }
            playWithFriendsBtn.setOnClickListener {
                findNavController().navigate(
                    StartFragmentDirections.actionStartFragmentToMultiplayerOptionsFragment()
                )
            }
            playOnlineBtn.setOnClickListener {
                findNavController().navigate(
                    StartFragmentDirections.actionStartFragmentToRoomsListFragmentContainer()
                )
            }
        }
    }
}