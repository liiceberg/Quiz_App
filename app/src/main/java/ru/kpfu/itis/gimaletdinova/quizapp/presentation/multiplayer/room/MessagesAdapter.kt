package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemMessageBinding

class MessagesAdapter(
    private val list: MutableList<String>
) : Adapter<MessagesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesHolder {
        return MessagesHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessagesHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun add(message: String) {
        list.add(message)
        notifyItemInserted(list.size - 1)
    }
}