package ru.kpfu.itis.gimaletdinova.quizapp.presentation.authorization

import android.os.Bundle
import android.view.View
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
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentSignUpBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding: FragmentSignUpBinding by viewBinding(FragmentSignUpBinding::bind)
    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {

            signUpBtn.setOnClickListener {
                lifecycleScope.launch {
                    if (validate()) {
                        if (viewModel.save(emailEt.text.toString(), passwordEt.text.toString())) {

                            Toast.makeText(context, R.string.account_created, Toast.LENGTH_SHORT)
                                .show()

                            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)

                        }
                    }
                }
            }

            with(viewModel) {
                loadingFlow.observe(this@SignUpFragment) { isLoad ->
                    progressBar.apply {
                        visibility = if (isLoad) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                    signUpBtn.isEnabled = isLoad.not()
                }
                errorsChannel.receiveAsFlow().observe(this@SignUpFragment) {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validate(): Boolean {
        with(binding) {

            val emailValidation = viewModel.validateEmail(
                emailEt.text.toString().trim()
            )
            emailTil.error = emailValidation.error
            if (emailValidation.isValid.not()) {
                return false
            }

            val password = passwordEt.text.toString().trim()
            val passwordValidator = viewModel.validatePassword(
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