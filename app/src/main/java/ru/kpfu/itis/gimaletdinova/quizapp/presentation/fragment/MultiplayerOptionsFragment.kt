package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentMultiplayerOptionsBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MAX_PLAYERS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MIN_PLAYERS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.IS_MULTIPLAYER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_NAMES
import ru.kpfu.itis.gimaletdinova.quizapp.util.ValidationUtil

class MultiplayerOptionsFragment : BaseFragment(R.layout.fragment_multiplayer_options) {

    private val binding: FragmentMultiplayerOptionsBinding by viewBinding(
        FragmentMultiplayerOptionsBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            playersNumberTv.text = MIN_PLAYERS_NUMBER.toString()

            startBtn.setOnClickListener {
                if (isPlayersCorrect()) {
                    binding.root.findNavController().navigate(
                        R.id.action_multiplayerOptionsFragment_to_prelaunchFragment,
                        bundleOf(
                            IS_MULTIPLAYER to true,
                            PLAYERS_NAMES to getPlayersNames()
                        )
                    )
                }
            }

            plusBtn.setOnClickListener {
                increasePlayersNumber()
            }
            minusBtn.setOnClickListener {
                decreasePlayersNumber()
            }
        }
    }

    private fun increasePlayersNumber() {
        with(binding) {
            val playersNumber = getPlayersNumber()
            if (playersNumber < MAX_PLAYERS_NUMBER) {
                playersNumberTv.text = (playersNumber + 1).toString()
                if (playersNumber + 1 == MAX_PLAYERS_NUMBER) {
                    deactivateView(plusBtn)
                }
                if (playersNumber == MIN_PLAYERS_NUMBER) {
                    activateView(minusBtn)
                }
            }
        }
        verifyEditTextVisibility()
    }

    private fun decreasePlayersNumber() {
        with(binding) {
            val playersNumber = getPlayersNumber()
            if (playersNumber > MIN_PLAYERS_NUMBER) {
                playersNumberTv.text = (playersNumber - 1).toString()
                if (playersNumber - 1 == MIN_PLAYERS_NUMBER) {
                    deactivateView(minusBtn)
                }
                if (playersNumber == MAX_PLAYERS_NUMBER) {
                    activateView(plusBtn)
                }
            }
        }
        verifyEditTextVisibility()
    }

    private fun deactivateView(view: ImageButton) {
        view.isEnabled = false
//        TODO replace with secondaryColor
        view.setColorFilter(resources.getColor(R.color.grey))
    }

    private fun activateView(view: ImageView) {
        view.isEnabled = true
        //        TODO replace with primaryVariantColor
        view.setColorFilter(resources.getColor(R.color.yellow_green))
    }

    private fun isPlayersCorrect(): Boolean {
        with(binding) {
            val validatingFields = listOf(player1Et, player2Et, player3Et, player4Et)
            var isCorrect = true
            for (field in validatingFields.subList(0, getPlayersNumber())) {
                isCorrect = isCorrect && ValidationUtil.validateName(requireContext(), field)
            }
            return isCorrect
        }
    }

    private fun verifyEditTextVisibility() {
        with(binding) {
            val validatingFields =
                listOf(player1EtLayout, player2EtLayout, player3EtLayout, player4EtLayout)
            val number = getPlayersNumber()
            for (ind in validatingFields.indices) {
                validatingFields[ind].visibility = if (ind < number) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getPlayersNumber(): Int = binding.playersNumberTv.text.toString().toInt()

    private fun getPlayersNames(): Array<String> {
        with(binding) {
            return listOf(player1Et, player2Et, player3Et, player4Et)
                .map { et -> et.text.toString() }
                .subList(0, getPlayersNumber())
                .toTypedArray()
        }
    }

}