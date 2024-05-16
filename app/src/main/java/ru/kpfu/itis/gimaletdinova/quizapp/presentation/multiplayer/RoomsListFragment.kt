package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentRoomsListBinding

@AndroidEntryPoint
class RoomsListFragment : Fragment(R.layout.fragment_rooms_list) {

    private val binding: FragmentRoomsListBinding by viewBinding(FragmentRoomsListBinding::bind)
    private val roomsListViewModel: RoomsListViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            createBtn.setOnClickListener {
                findNavController().navigate(R.id.action_roomsListFragment_to_createRoomFragment)
            }
        }
    }
}