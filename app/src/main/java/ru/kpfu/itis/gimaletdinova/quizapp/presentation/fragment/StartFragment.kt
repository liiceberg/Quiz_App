package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentStartBinding

class StartFragment : BaseFragment(R.layout.fragment_start) {

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