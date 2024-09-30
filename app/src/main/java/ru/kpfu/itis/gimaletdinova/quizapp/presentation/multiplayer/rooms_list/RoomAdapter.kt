package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemRoomBinding
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList

class RoomAdapter(
    diffCallback: DiffUtil.ItemCallback<Room>,
    private val onItemClicked: ((Room) -> Unit),
    private val categoriesList: CategoriesList
): ListAdapter<Room, RoomHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder =
        RoomHolder(
            ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked,
            categoriesList
        )

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    fun setItems(items: List<Room>) {
        submitList(items)
    }
}