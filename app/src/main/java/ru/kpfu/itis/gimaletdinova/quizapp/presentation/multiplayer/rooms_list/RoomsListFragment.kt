package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentRoomsListBinding
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleVerticalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.util.getValueInPx
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe
import ru.kpfu.itis.gimaletdinova.quizapp.util.showErrorMessage
import java.util.stream.Collectors

@AndroidEntryPoint
class RoomsListFragment : Fragment(R.layout.fragment_rooms_list) {

    private val binding: FragmentRoomsListBinding by viewBinding(FragmentRoomsListBinding::bind)
    private val roomsListViewModel: RoomsListViewModel by viewModels()
    private var roomAdapter: RoomAdapter? = null
    private lateinit var roomsSearchView: SearchView

    override fun onStart() {
        roomsListViewModel.repeatCheckingRoomsForUpdates(requireArguments().getBoolean(ALL_ROOMS))
        roomsSearchView = requireParentFragment().requireView().findViewById(R.id.room_sv)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        roomsSearchView.setQuery("", false)
        setOnRoomsSearchListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(roomsListViewModel) {

            getCategoriesList()

            loadingFlow.observe(this@RoomsListFragment) { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            categoriesFlow.observe(this@RoomsListFragment) { categories ->
                categories?.let { initRv(categories) }
            }

            roomFlow.observe(this@RoomsListFragment) {
                roomAdapter?.setItems(it)
            }

            errorsChannel.receiveAsFlow().observe(this@RoomsListFragment) {
                activity?.showErrorMessage(it.message)
            }
        }

    }

    private fun setOnRoomsSearchListener() {
        roomsSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                text?.let { filterRoomList(text) }
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                text?.let { filterRoomList(text) }
                return true
            }

        })
        roomsSearchView.setOnQueryTextFocusChangeListener { _, focused ->
            with(roomsListViewModel) {
                if (focused) {
                    stopRepeatWork()
                } else {
                    repeatCheckingRoomsForUpdates(requireArguments().getBoolean(ALL_ROOMS))
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

    private fun initRv(categoriesList: CategoriesList) {
        binding.roomsRv.apply {
            roomAdapter =
                RoomAdapter(
                    RoomDiffUtilItemCallback(),
                    ::onItemClicked,
                    categoriesList
                )
            adapter = roomAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            val verticalMarginDecoration = SimpleVerticalMarginDecoration(
                VERTICAL_MARGIN_VALUE.getValueInPx(resources.displayMetrics)
            )
            addItemDecoration(verticalMarginDecoration)
        }
    }

    private fun onItemClicked(room: Room) {
        findNavController().navigate(
            RoomsListContainerFragmentDirections.actionRoomsListContainerFragmentToRoomFragment(
                room.code
            )
        )
    }

    override fun onStop() {
        roomsListViewModel.stopRepeatWork()
        super.onStop()
    }

    companion object {

        private const val VERTICAL_MARGIN_VALUE = 8
        private const val ALL_ROOMS = "ALL_ROOMS"

        fun newInstance(getAllRooms: Boolean) = RoomsListFragment().apply {
            arguments = bundleOf(ALL_ROOMS to getAllRooms)
        }
    }
}