package ru.kpfu.itis.gimaletdinova.quizapp.presentation.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.AppException
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentSignUpBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment


@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {

    private val binding: FragmentSignUpBinding by viewBinding(FragmentSignUpBinding::bind)
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {

            signUpBtn.setOnClickListener {
                if (validate()) {
                    signUpViewModel.save(
                        emailEt.text.toString(),
                        passwordEt.text.toString()
                    )
                }
            }

            with(signUpViewModel) {
                loadingFlow.observe { isLoad ->
                    progressBar.apply {
                        visibility = if (isLoad) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                    signUpBtn.isEnabled = isLoad.not()
                }
                signedInFlow.observe { isSignedIn ->
                    if (isSignedIn) {

                        Toast.makeText(
                            context,
                            R.string.account_created,
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigate(
                            SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                        )
                    }
                }
                errorsChannel.receiveAsFlow().observe { error ->
                    when(error) {
                        is AppException.SuchEmailAlreadyRegistered -> emailTil.error = error.message
                        else -> showError(error.message)
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        with(binding) {

            val emailValidation = signUpViewModel.validateEmail(
                emailEt.text.toString().trim()
            )
            emailTil.error = emailValidation.error
            if (emailValidation.isValid.not()) {
                return false
            }

            val password = passwordEt.text.toString().trim()
            val passwordValidator = signUpViewModel.validatePassword(
                password
            )
            passwordTil.error = passwordValidator.error
            if (passwordValidator.isValid.not()) {
                return false
            }

            val repeatPassword = repeatPasswordEt.text.toString().trim()
            if (password != repeatPassword) {
                repeatPasswordTil.error = getString(R.string.passwords_not_equals_error)
                return false
            }
            repeatPasswordTil.error = null
            return true
        }
    }

}