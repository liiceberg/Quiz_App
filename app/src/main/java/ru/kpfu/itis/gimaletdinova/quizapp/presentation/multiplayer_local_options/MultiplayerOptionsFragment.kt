package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_local_options


import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentMultiplayerOptionsBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleHorizontalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleVerticalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_local_options.model.InputModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MAX_PLAYERS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MIN_PLAYERS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.getThemeColor
import ru.kpfu.itis.gimaletdinova.quizapp.util.getValueInPx

@AndroidEntryPoint
class MultiplayerOptionsFragment : Fragment(R.layout.fragment_multiplayer_options) {

    private val binding: FragmentMultiplayerOptionsBinding by viewBinding(
        FragmentMultiplayerOptionsBinding::bind
    )
    private var inputAdapter: InputAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            playersNumberTv.text = MIN_PLAYERS_NUMBER.toString()

            initRecyclerView()

            startBtn.setOnClickListener {
                if (isPlayersCorrect()) {

                    val players = inputAdapter?.currentList?.map { model -> model.text }
                        ?.toTypedArray() ?: arrayOf()

                    findNavController().navigate(
                        MultiplayerOptionsFragmentDirections.actionMultiplayerOptionsFragmentToPrelaunchFragment(
                            Mode.MULTIPLAYER,
                            players
                        )
                    )
                } else {
                    inputAdapter?.currentList?.count { it.text == "" }
                        ?.let { emptyFieldsNumber ->
                            if (emptyFieldsNumber > 0) showWarning()
                        }
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

    private fun initRecyclerView() {
        inputAdapter = InputAdapter(
            diffCallback = InputDiffUtilItemCallback(),
            ::onTextChanged
        )
        inputAdapter?.setItems(createInputList())

        binding.inputRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = inputAdapter
            val marginValue = 8.getValueInPx(resources.displayMetrics)
            addItemDecoration(SimpleHorizontalMarginDecoration(marginValue))
            addItemDecoration(SimpleVerticalMarginDecoration(marginValue))
        }

    }

    private fun onTextChanged(newInputModel: InputModel) {
        inputAdapter?.updateItem(newInputModel)
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
        addInputItem()
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
        removeInputItem()
    }

    private fun addInputItem() {
        inputAdapter?.apply {
            val newItem = InputModel(position = currentList.size + 1)
            addItem(newItem)
        }
    }

    private fun removeInputItem() {
        inputAdapter?.apply {
            removeItem(currentList.last())
        }
    }

    private fun deactivateView(view: ImageButton) {
        view.isEnabled = false
        val color =
            requireActivity().getThemeColor(com.google.android.material.R.attr.colorSecondary)
        view.setColorFilter(color)
    }

    private fun activateView(view: ImageView) {
        view.isEnabled = true
        val color =
            requireActivity().getThemeColor(com.google.android.material.R.attr.colorPrimaryVariant)
        view.setColorFilter(color)
    }

    private fun isPlayersCorrect(): Boolean {
        inputAdapter?.currentList?.let { list ->
            for (item in list) {
                if (item.isCorrect.not()) return false
            }
        }
        return true
    }


    private fun getPlayersNumber(): Int = binding.playersNumberTv.text.toString().toInt()

    private fun createInputList(): List<InputModel> {
        val list = mutableListOf<InputModel>()
        for (pos in 1..MIN_PLAYERS_NUMBER) {
            list.add(InputModel(position = pos))
        }
        return list
    }

    private fun showWarning() {
        Toast.makeText(context, R.string.options_dialog_text, Toast.LENGTH_LONG).show()
    }

}