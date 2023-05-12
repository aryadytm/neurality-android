package com.wemadefun.neurality.ui.train

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.wemadefun.neurality.databinding.ItemGameBinding
import com.wemadefun.neurality.ext.setImageWithGlide
import com.wemadefun.neurality.utils.GameProvider
import com.wemadefun.neurality.utils.getSubcategoryString

class TrainItemSubAdapter : RecyclerView.Adapter<TrainItemSubAdapter.TrainItemViewHolder>() {

    lateinit var data: List<GameProvider.GameData>

    override fun getItemCount() = data.size

    class TrainItemViewHolder(binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
        val gameTitle: TextView = binding.textTitle
        val gameSubcategory: TextView = binding.textSubcategory
        val gameImage: ImageView = binding.imageGameThumbnail
        val context: Context = binding.root.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGameBinding.inflate(inflater, parent, false)
        return TrainItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainItemViewHolder, position: Int) {
        holder.gameTitle.text = data[position].title
        holder.gameSubcategory.text = getSubcategoryString(data[position].subCategory, holder.gameSubcategory.resources)
        holder.gameImage.setImageWithGlide(data[position].drawableImgId)
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(TrainFragmentDirections.actionNavTrainToNavTrainDetails(data[position]))
        }
    }


}