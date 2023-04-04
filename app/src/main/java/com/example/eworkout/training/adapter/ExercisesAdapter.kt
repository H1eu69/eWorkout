package com.example.eworkout.training.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eworkout.databinding.ExerciseRecyclerviewItemBinding
import com.example.eworkout.training.listener.ExercisesOnClickListener
import com.example.eworkout.training.model.Exercise

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