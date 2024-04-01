package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentProfileBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.viewmodel.ProfileViewModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.ValidationUtil.validateName
import ru.kpfu.itis.gimaletdinova.quizapp.util.hideKeyboard
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding(
        FragmentProfileBinding::bind
    )

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {

            backBtn.setOnClickListener { view ->
                view.findNavController()
                    .navigate(R.id.action_profileFragment_to_startFragment)
            }

            profileViewModel.usernameFlow.observe(this@ProfileFragment) { username ->
                usernameTv.text = username
            }

            usernameEditBtn.setOnClickListener {
                if (usernameEtLayout.visibility == View.GONE) {
                    usernameEtLayout.visibility = View.VISIBLE
                } else {
                    editUsername()
                }
            }

            usernameEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editUsername()
                }
                true
            }

            themeBtn.setOnClickListener {
                profileViewModel.changeTheme()
            }

            profileViewModel.themeFlow.observe(this@ProfileFragment) { isNightMode ->
                val img =
                    if (isNightMode) R.drawable.moon_svgrepo_com else R.drawable.sun_svgrepo_com
                themeBtn.setImageResource(img)
            }

            userQuestionsTv.text =
                getString(R.string.user_questions_number, profileViewModel.getUserQuestionsNumber())

            totalQuestionsTv.text =
                getString(R.string.total_questions_number, profileViewModel.getTotalQuestionsNumber())
        }
    }

    private fun editUsername() {
        with(binding) {
            if (validateName(requireContext(), usernameEt)) {
                profileViewModel.saveUsername(usernameEt.text.toString())
                usernameEtLayout.visibility = View.GONE
                hideKeyboard(context, view)
            }
        }
    }

}