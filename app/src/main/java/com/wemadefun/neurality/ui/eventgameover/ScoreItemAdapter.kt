package com.wemadefun.neurality.ui.eventgameover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wemadefun.neurality.databinding.ItemScoreBinding

class ScoreItemAdapter : RecyclerView.Adapter<ScoreItemAdapter.TitleViewHolder>() {

    lateinit var data: List<Int>
    override fun getItemCount() = data.size

    class TitleViewHolder(val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        val numberText: TextView = binding.textViewItemNumber
        val scoreText: TextView = binding.textViewItemScore
        val line: View = binding.line
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemScoreBinding.inflate(layoutInflater, parent, false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        holder.numberText.text = (position + 1).toString()
        holder.scoreText.text = data[position].toString()
        if (data[position] == 0) { holder.scoreText.text = "-" }
        if (position == (itemCount-1)) { holder.line.visibility = View.GONE }
    }

}