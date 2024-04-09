package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentStartBinding
@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    private val binding: FragmentStartBinding by viewBinding(
        FragmentStartBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            profileBtn.setOnClickListener { view ->
                view.findNavController().navigate(R.id.action_startFragment_to_profileFragment)
            }
            playBtn.setOnClickListener { view ->
                view.findNavController().navigate(R.id.action_startFragment_to_categoriesFragment)
            }
            playWithFriendsBtn.setOnClickListener { view ->
                view.findNavController()
                    .navigate(R.id.action_startFragment_to_multiplayerOptionsFragment)
            }
        }
    }
}