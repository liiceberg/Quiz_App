package ru.kpfu.itis.gimaletdinova.quizapp.presentation.profile

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentProfileBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.hideKeyboard
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding(
        FragmentProfileBinding::bind
    )

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        profileViewModel.getUserInfo()

        with(binding) {

            backBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_startFragment)
            }

            logoutBtn.setOnClickListener {
                lifecycleScope.launch {
                    profileViewModel.logout()
                    findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
                }
            }

            usernameEditBtn.setOnClickListener {
                if (usernameEtLayout.visibility == View.GONE) {
                    usernameEtLayout.visibility = View.VISIBLE
                } else {
                    if (validateUserInput()) saveUsername()
                }
            }

            usernameEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validateUserInput()) saveUsername()
                }
                true
            }

            themeBtn.setOnClickListener {
                profileViewModel.changeTheme()
            }

            with(profileViewModel) {
                usernameFlow.observe(this@ProfileFragment) { username ->
                    usernameTv.text = username
                }

                scoresFlow.observe(this@ProfileFragment) {
                    it?.let {
                        userQuestionsTv.text =
                            getString(R.string.user_questions_number, it.correctNumber)
                        totalQuestionsTv.text =
                            getString(R.string.total_questions_number, it.totalNumber)
                    }
                }

                themeFlow.observe(this@ProfileFragment) { isNightMode ->
                    val img =
                        if (isNightMode) R.drawable.moon_svgrepo_com else R.drawable.sun_svgrepo_com
                    themeBtn.setImageResource(img)
                }

                errorsChannel.receiveAsFlow().observe(this@ProfileFragment) {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validateUserInput(): Boolean {
        with(binding) {
            val validationResult = profileViewModel.validateUsername(
                usernameEt.text.toString()
            )
            if (validationResult.isValid) {
                usernameEtLayout.error = null
                return true
            } else {
                usernameEtLayout.error = validationResult.error
                return false
            }
        }
    }

    private fun saveUsername() {
        with(binding) {
            profileViewModel.saveUsername(usernameEt.text.toString())
            usernameEtLayout.visibility = View.GONE
            hideKeyboard(context, view)
        }
    }

}