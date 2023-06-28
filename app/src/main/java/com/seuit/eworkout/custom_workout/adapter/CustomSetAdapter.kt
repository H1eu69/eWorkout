package com.seuit.eworkout.custom_workout.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.seuit.eworkout.custom_workout.listener.CustomSetOnClickListener
import com.seuit.eworkout.custom_workout.model.CustomSet
import com.seuit.eworkout.databinding.CustomSetListItemBinding

class CustomSetAdapter(val list: List<CustomSet>,
val listener: CustomSetOnClickListener
) : RecyclerView.Adapter<CustomSetAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomSetListItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: CustomSet)
        {
            val bundle = Bundle()
            item.apply {
                binding.textViewSetName.text = name
                binding.textViewTotalExercises.text = "$numOfExercises Exercise"
                Glide.with(binding.root.context)
                    .load(image)
                    .into(binding.setImageView)
                bundle.putString("set_id", id)
                bundle.putBoolean("isSystemSet", false)
            }

            binding.buttonViewDetail.setOnClickListener {
                listener.onClick(bundle)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomSetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        holder.bind(item)
    }
}