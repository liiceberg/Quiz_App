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

                        } else {
                            emailEt.error = getString(R.string.email_already_exist)
                        }
                    }
                }
            }

            viewModel.loadingFlow.observe(this@SignUpFragment) { isLoad ->
                progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        with(binding) {
            val email = emailEt.text.toString().trim()
            if (email.isEmpty() ||
                email.matches(Regex("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}")).not()) {
                emailEt.error = getString(R.string.email_error)
                return false
            }
            val password = passwordEt.text.toString().trim()
            if (password.matches(Regex("\\w{8,}")).not()) {
                passwordEt.error = getString(R.string.password_error)
                return false
            }
            val repeatPassword = repeatPasswordEt.text.toString().trim()
            if(password != repeatPassword) {
                repeatPasswordEt.error = getString(R.string.passwords_not_equals_error)
                return false
            }
            return true
        }
    }

}