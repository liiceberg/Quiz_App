package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room

class RoomDiffUtilItemCallback: DiffUtil.ItemCallback<Room>() {

    override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
        return oldItem == newItem
    }
}