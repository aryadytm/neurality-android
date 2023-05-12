package com.wemadefun.neurality.ui.train

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.wemadefun.neurality.databinding.ItemFeaturedGameBinding
import com.wemadefun.neurality.utils.GameProvider
import com.wemadefun.neurality.utils.getCategoryString

class ViewPagerAdapter(
    private val gameList: List<GameProvider.GameData>,
    private val context: Context) : RecyclerView.Adapter<ViewPagerAdapter.Holder>() {

    override fun getItemCount() = gameList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFeaturedGameBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(val binding: ItemFeaturedGameBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleText = binding.textTitle
        val categoryText = binding.textCategory
        val cardView = binding.cardView
        val imageView = binding.imageviewGame
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentGame = gameList[position]
        val resources = context.resources

        holder.titleText.text = currentGame.title
        holder.categoryText.text = getCategoryString(currentGame.category, resources)
        holder.imageView.setBackgroundResource(currentGame.drawableImgId)
        holder.cardView.setOnClickListener {
            it.findNavController().navigate(TrainFragmentDirections
                .actionNavTrainToNavTrainDetails(gameList[position]))
        }
    }

}