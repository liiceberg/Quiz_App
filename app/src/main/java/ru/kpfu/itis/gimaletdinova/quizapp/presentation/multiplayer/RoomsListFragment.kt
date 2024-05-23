package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentRoomsListBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleVerticalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.ALL_ROOMS
import ru.kpfu.itis.gimaletdinova.quizapp.util.getValueInPx
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe
import java.util.stream.Collectors

@AndroidEntryPoint
class RoomsListFragment : Fragment(R.layout.fragment_rooms_list) {

    private val binding: FragmentRoomsListBinding by viewBinding(FragmentRoomsListBinding::bind)
    private val roomsListViewModel: RoomsListViewModel by viewModels()
    private var roomAdapter: RoomAdapter? = null
    private val UPDATE_INTERVAL = 1000L
    private lateinit var roomsSearchView: SearchView

    override fun onStart() {
        repeatCheckingRoomsForUpdates()
        roomsSearchView = requireParentFragment().requireView().findViewById(R.id.room_sv)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        roomsSearchView.setQuery("", false)
        setOnRoomsSearchListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            initRv()
        }

        with(roomsListViewModel) {

            loadingFlow.observe(this@RoomsListFragment) { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            roomFlow.observe(this@RoomsListFragment) {
                roomAdapter?.setItems(it)
            }
        }

    }

    private fun repeatCheckingRoomsForUpdates() {

        roomsListViewModel.run {
            doRepeatWork(
                UPDATE_INTERVAL
            ) {
                getRoomList(requireArguments().getBoolean(ALL_ROOMS))
                println(roomsSearchView.isFocused)
            }
        }
    }

    private fun setOnRoomsSearchListener() {
        roomsSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if (text != null) {
                    filterRoomList(text)
                }
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) {
                    filterRoomList(text)
                }

                return true
            }

        })
        roomsSearchView.setOnQueryTextFocusChangeListener { _, focused ->
            with(roomsListViewModel) {
                if (focused) {
                    stopRepeatWork()
                } else {
                    repeatCheckingRoomsForUpdates()
                }
            }
        }
    }

    private fun filterRoomList(text: String) {
        val query = text.trim().lowercase()
        roomsListViewModel.currentRoomList
            ?.stream()
            ?.filter { it.code.lowercase().startsWith(query) }
            ?.collect(Collectors.toList())
            ?.let {
                roomAdapter?.setItems(it)
            }
    }

    private suspend fun initRv() {
        binding.roomsRv.apply {
            roomAdapter =
                RoomAdapter(
                    RoomDiffUtilItemCallback(),
                    ::onItemClicked,
                    roomsListViewModel.getCategoriesList()
                )
            adapter = roomAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            val verticalMarginValue = 8.getValueInPx(resources.displayMetrics)
            addItemDecoration(SimpleVerticalMarginDecoration(verticalMarginValue))
        }
        roomsListViewModel.getRoomList(requireArguments().getBoolean(ALL_ROOMS))
    }

    private fun onItemClicked(room: Room) {
        findNavController().navigate(
            R.id.action_roomsListFragmentContainer_to_roomFragment,
            bundleOf(
                Keys.ROOM_CODE to room.code
            )
        )
    }

    override fun onStop() {
        roomsListViewModel.stopRepeatWork()
        super.onStop()
    }

    companion object {
        fun newInstance(getAllRooms: Boolean) = RoomsListFragment().apply {
            arguments = bundleOf(ALL_ROOMS to getAllRooms)
        }
    }
}