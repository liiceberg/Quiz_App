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
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentSignInBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding: FragmentSignInBinding by viewBinding(FragmentSignInBinding::bind)
    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding) {

            signUpBtn.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            signInBtn.setOnClickListener {
                lifecycleScope.launch {
                    if (validate()) {
                        if (viewModel.login(emailEt.text.toString(), passwordEt.text.toString())) {
                            findNavController().navigate(R.id.action_signInFragment_to_startFragment)
                        }
                    }
                }

            }

            with(viewModel) {
                loadingFlow.observe(this@SignInFragment) { isLoad ->
                    progressBar.apply {
                        visibility = if (isLoad) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                    signInBtn.isEnabled = isLoad.not()
                }
                errorsChannel.receiveAsFlow().observe(this@SignInFragment) {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validate(): Boolean {
        with(binding) {
            val email = emailEt.text.toString().trim()
            if (email.isEmpty() ||
                email.matches(Regex("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}")).not()
            ) {
                emailTil.error = getString(R.string.email_error)
                return false
            }
            emailTil.error = null
            val password = passwordEt.text.toString().trim()
            if (password.matches(Regex("\\w{8,}")).not()) {
                passwordTil.error = getString(R.string.password_length_error)
                return false
            }
            passwordTil.error = null
            return true
        }
    }


}