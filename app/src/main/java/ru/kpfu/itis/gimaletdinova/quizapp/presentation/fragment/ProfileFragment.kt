package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentProfileBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.MainActivity
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.util.ValidationUtil.hideKeyboard
import ru.kpfu.itis.gimaletdinova.quizapp.util.ValidationUtil.validateName

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding(
        FragmentProfileBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with(binding) {

            backBtn.setOnClickListener { view ->
                view.findNavController()
                    .navigate(R.id.action_profileFragment_to_startFragment)
            }

            usernameTv.text = prefs.getString(getString(R.string.username), "user")

            usernameEditBtn.setOnClickListener {
                if (usernameEtLayout.visibility == View.GONE) {
                    usernameEtLayout.visibility = View.VISIBLE
                } else {
                    editUsername(prefs)
                }
            }
            usernameEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editUsername(prefs)
                }
                true
            }

            setThemeButton()

            themeBtn.setOnClickListener {
                changeTheme(prefs)
                setThemeButton()
            }

            val userQuestionsNumber = prefs.getInt(getString(R.string.user_questions), 0)
            val totalQuestionsNumber = prefs.getInt(getString(R.string.total_questions), 0)
            userQuestionsTv.text = getString(R.string.user_questions_number, userQuestionsNumber)
            totalQuestionsTv.text = getString(R.string.total_questions_number, totalQuestionsNumber)
        }
    }

    private fun editUsername(prefs: SharedPreferences) {
        with(binding) {
            if (validateName(requireContext(), usernameEt)) {

                val name = usernameEt.text.toString()

                prefs.edit()
                    .putString(getString(R.string.username), name)
                    .apply()

                usernameTv.text = name
                usernameEtLayout.visibility = View.GONE

                hideKeyboard(context, view)
            }
        }
    }

    private fun setThemeButton() {
        val img = if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES)
            R.drawable.moon_svgrepo_com else R.drawable.sun_svgrepo_com
        binding.themeBtn.setImageResource(img)
    }

    private fun changeTheme(prefs: SharedPreferences) {
        val isNightMode = AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES

        prefs.edit()
            .putBoolean(getString(R.string.night_mode), !isNightMode)
            .apply()

        (activity as? MainActivity)?.setTheme(!isNightMode)
    }
}