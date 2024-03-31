package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentPrelaunchBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
@AndroidEntryPoint
class PrelaunchFragment : BaseFragment(R.layout.fragment_prelaunch) {

    private val binding: FragmentPrelaunchBinding by viewBinding(
        FragmentPrelaunchBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.playBtn.setOnClickListener { view ->
            view.findNavController()
                .navigate(R.id.action_prelaunchFragment_to_categoryChoiceFragment)
        }
    }


}