package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty.*
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentCreateRoomBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class CreateRoomFragment : Fragment(R.layout.fragment_create_room) {

    private val binding: FragmentCreateRoomBinding by viewBinding(FragmentCreateRoomBinding::bind)
    private val createRoomViewModel: CreateRoomViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createRoomViewModel.getCategoriesList()

        with(binding) {
            var difficulty: LevelDifficulty? = null
            var category: Int? = null

            createBtn.setOnClickListener {
                lifecycleScope.launch {
                    val code = createRoomViewModel.create(
                        playersNumber = playersNumberTv.text.toString().toInt(),
                        categoryId = category,
                        difficulty = difficulty
                    )
                    if (code != null) {
                        findNavController().navigate(R.id.action_createRoomFragment_to_roomsListFragmentContainer)
                        val toastText = getString(R.string.room_created, code)
                        Toast.makeText(context, toastText, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            playersNumberTv.text = numberSb.progress.toString()

            numberSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    playersNumberTv.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })

            difficultyGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.dif_easy_rb -> difficulty = EASY
                    R.id.dif_medium_rb -> difficulty = MEDIUM
                    R.id.dif_hard_rb -> difficulty = HARD
                }
            }

            with(createRoomViewModel) {

                loadingFlow.observe(this@CreateRoomFragment) { isLoad ->
                    binding.progressBar.apply {
                        visibility = if (isLoad) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                }
                categoriesFlow.observe(this@CreateRoomFragment) {
                    it?.let {
                        categoriesSpinner.apply {

                            val list = mutableListOf<String>()
                            list.add("No category selected")

                            list.addAll(it.categoriesList.map { model -> model.displayName })


                            adapter =
                                ArrayAdapter(requireContext(), R.layout.spinner_category_item, list)

                            onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        if (position != 0) {
                                            val catId = it.categoriesList[position - 1].id
                                            category = catId
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                                }
                        }
                    }
                }

            }


        }
    }
}