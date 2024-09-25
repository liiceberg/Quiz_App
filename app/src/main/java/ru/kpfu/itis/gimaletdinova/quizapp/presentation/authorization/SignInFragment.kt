package ru.kpfu.itis.gimaletdinova.quizapp.presentation.authorization

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentSignInBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment


@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val binding: FragmentSignInBinding by viewBinding(FragmentSignInBinding::bind)
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding) {

            signUpBtn.setOnClickListener {
                findNavController().navigate(
                    SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                )
            }

            signInBtn.setOnClickListener {
                if (validate()) {
                    signInViewModel.login(
                        emailEt.text.toString(),
                        passwordEt.text.toString()
                    )
                }
            }

            with(signInViewModel) {
                loadingFlow.observe { isLoad ->
                    progressBar.apply {
                        visibility = if (isLoad) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                    signInBtn.isEnabled = isLoad.not()
                }
                loggedInFlow.observe { isLoggedIn ->
                    if (isLoggedIn) {
                        findNavController().navigate(
                            SignInFragmentDirections.actionSignInFragmentToStartFragment()
                        )
                    }
                }
                errorsChannel.receiveAsFlow().observe {
                    showError(it.message)
                }
            }
        }
    }

    private fun validate(): Boolean {
        with(binding) {
            val validationResult = signInViewModel.validateEmail(
                emailEt.text.toString().trim()
            )
            emailTil.error = validationResult.error
            return validationResult.isValid
        }
    }

}