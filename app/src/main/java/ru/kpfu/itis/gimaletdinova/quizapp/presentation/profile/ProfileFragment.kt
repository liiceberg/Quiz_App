package ru.kpfu.itis.gimaletdinova.quizapp.presentation.profile

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentProfileBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.hideKeyboard
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe
import ru.kpfu.itis.gimaletdinova.quizapp.util.showErrorMessage

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding(
        FragmentProfileBinding::bind
    )
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding) {

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            logoutBtn.setOnClickListener {
                profileViewModel.logout()
            }

            usernameEditBtn.setOnClickListener {
                if (usernameEtLayout.visibility == View.GONE) {
                    usernameEtLayout.visibility = View.VISIBLE
                } else {
                    if (validateUserInput()) {
                        saveUsername()
                    }
                }
            }

            usernameEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validateUserInput()) {
                        saveUsername()
                    }
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

                loggedOutFlow.observe(this@ProfileFragment) { isLoggedOut ->
                    if (isLoggedOut) {
                        findNavController().navigate(
                            ProfileFragmentDirections.actionProfileFragmentToSignInFragment()
                        )
                    }
                }

                errorsChannel.receiveAsFlow().observe(this@ProfileFragment) {
                    activity?.showErrorMessage(it.message)
                }
            }
        }
    }

    private fun validateUserInput(): Boolean {
        with(binding) {
            val validationResult = profileViewModel.validateUsername(
                usernameEt.text.toString()
            )
            usernameEtLayout.error = validationResult.error
            return validationResult.isValid
        }
    }

    private fun saveUsername() {
        with(binding) {
            profileViewModel.saveUsername(usernameEt.text.toString())
            usernameEtLayout.visibility = View.GONE
            view?.hideKeyboard()
        }
    }

}