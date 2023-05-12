package com.wemadefun.neurality.ui.stats

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.wemadefun.neurality.databinding.ItemStatsScoreBinding
import com.wemadefun.neurality.utils.getCategoryString

class StatsItemAdapter : RecyclerView.Adapter<StatsItemAdapter.StatsItemViewHolder>() {
    lateinit var data: List<Int>
    override fun getItemCount() = data.size

    class StatsItemViewHolder(val binding: ItemStatsScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        val category: TextView = binding.textStatsName
        val scoreBar: RoundCornerProgressBar = binding.barStatsScore
        val score: TextView = binding.textStatsScore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemStatsScoreBinding.inflate(layoutInflater, parent, false)
        return StatsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsItemViewHolder, position: Int) {
        val resources = holder.category.resources
        val score = data[position]
        if (score > 0)
            holder.score.text = data[position].toString()
        else
            holder.score.text = "-"
        holder.category.text = getCategoryString(position, resources)
        holder.scoreBar.progress  = data[position].toFloat() - 55f
    }
}