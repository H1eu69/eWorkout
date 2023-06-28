package com.seuit.eworkout.training.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.seuit.eworkout.R
import com.seuit.eworkout.databinding.ExerciseRecyclerviewItemBinding
import com.seuit.eworkout.training.listener.ExercisesOnClickListener
import com.seuit.eworkout.training.model.Exercise

class ExercisesAdapter(
    val list: List<Exercise>,
    val listener: ExercisesOnClickListener) : RecyclerView.Adapter<ExercisesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ExerciseRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(exercise: Exercise)
        {
            exercise.apply {
                val bundle = Bundle().apply {
                    putString("exercise_id", id)
                }
                binding.exerciseNameTextview.text = name
                binding.exerciseRepTextview.text = reps
                binding.exerciseInformationBtn.setOnClickListener {
                    listener.onClick(bundle)
                }
                if(image != ""){
                    val radius = binding.root.context.resources.getDimensionPixelSize(R.dimen.corner_radius)
                    Glide.with(binding.root.context).load(image)
                        .transform(RoundedCorners(radius))
                        .centerCrop()
                        .into(binding.shapeableImageView)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExerciseRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = list[position]
        holder.bind(exercise)
    }
}