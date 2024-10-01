package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.room

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemMessageBinding

class MessagesHolder(
    private val binding: ItemMessageBinding,
) : ViewHolder(binding.root) {

    fun bind(message: String) {
        binding.messageTv.text = message
    }

}