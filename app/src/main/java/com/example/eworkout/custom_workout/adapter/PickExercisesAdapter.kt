package com.example.eworkout.custom_workout.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.eworkout.R
import com.example.eworkout.custom_workout.listener.PickExercisesOnClickListener
import com.example.eworkout.custom_workout.model.PickExercise
import com.example.eworkout.databinding.CreateSetPickExercisesItemBinding
import com.example.eworkout.databinding.ExerciseRecyclerviewItemBinding
import com.example.eworkout.training.listener.ExercisesOnClickListener

class PickExercisesAdapter(
    val list: List<PickExercise>,
    val listener: PickExercisesOnClickListener
) : RecyclerView.Adapter<PickExercisesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CreateSetPickExercisesItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: PickExercise)
        {
            item.apply {
                binding.textViewExerciseName.text = item.name
                if(image != ""){
                    Glide.with(binding.root.context).load(image)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(40))) // round
                        .into(binding.imageView);
                }
                val bundle = Bundle().apply {
                    putString("exercise_id", id)
                }
                binding.exerciseInformationBtn.setOnClickListener {
                    listener.navigateToDetail(bundle)
                }

                binding.root.setOnClickListener {
                    listener.addToCart(bundle)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CreateSetPickExercisesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        holder.bind(item)
    }
}