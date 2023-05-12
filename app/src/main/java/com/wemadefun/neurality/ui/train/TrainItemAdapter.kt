package com.wemadefun.neurality.ui.train

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wemadefun.neurality.databinding.ItemTitleBinding
import com.wemadefun.neurality.utils.GameProvider
import com.wemadefun.neurality.utils.getCategoryString

class TrainItemAdapter : RecyclerView.Adapter<TrainItemAdapter.TitleViewHolder>() {

    lateinit var data: Map<Int, List<GameProvider.GameData>>
    lateinit var categories: List<Int>
    override fun getItemCount() = categories.size

    class TitleViewHolder(val binding: ItemTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleTextView: TextView = binding.textViewItemTitle
        val gamesRecyclerView: RecyclerView = binding.recyclerTrainSub
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemTitleBinding.inflate(layoutInflater, parent, false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val categoryId = categories[position]
        val gamesInCategory = data[categoryId]
        holder.titleTextView.text = getCategoryString(categoryId, holder.titleTextView.resources)

        val gameSelectAdapter = TrainItemSubAdapter()
        gameSelectAdapter.data = gamesInCategory!!
        holder.gamesRecyclerView.adapter = gameSelectAdapter
    }

}