package com.example.eworkout.training.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentSetsItemBinding
import com.example.eworkout.training.listener.SetOnClickListener
import com.example.eworkout.training.model.Set
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent

class set_recycler_adapter(val list: List<Set>,val listener: SetOnClickListener): RecyclerView.Adapter<set_recycler_adapter.ViewHolder>() {

    inner class ViewHolder(val binding: FragmentSetsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(set: Set) {
            set.apply {
                val bundle = Bundle().apply {
                    putString("set_id", setId)
                }
                binding.textViewSetName.text = setName
                binding.textViewTotalExercises.text = totalExercises
                binding.texViewTotalTime.text = totalTime
                binding.buttonViewDetail.setOnClickListener {
                    listener.onClick(bundle)
                }
                /*if(image != ""){
                    val radius = binding.root.context.resources.getDimensionPixelSize(R.dimen.corner_radius)
                    Glide.with(binding.root.context).load(image)
                        .transform(RoundedCorners(radius))
                        .centerCrop()
                        .into(binding.shapeableImageView)
                }

            }*/
            }
        }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                FragmentSetsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentset = list[position]
            holder.bind(currentset)
        }

        override fun getItemCount(): Int {
            return list.size
        }


        /*val list: List<Exercise>,
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
    }*/

}