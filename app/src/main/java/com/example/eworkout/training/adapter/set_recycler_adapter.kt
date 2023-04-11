package com.example.eworkout.training.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eworkout.R
import com.example.eworkout.training.model.Set

class set_recycler_adapter(val set: List<Set>): RecyclerView.Adapter<set_recycler_adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_sets_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setTaken = set[position]
        holder._setName.text = setTaken.setName
        holder._totalExercise.text = setTaken.totalExercises
        holder._totalTime.text = setTaken.totalTime
    }

    override fun getItemCount(): Int {
        return set.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val _setName: TextView = itemView.findViewById(R.id.textView_SetName)
        val _totalExercise: TextView = itemView.findViewById(R.id.textView_TotalExercises)
        val _totalTime: TextView = itemView.findViewById(R.id.texView_TotalTime)
        val _image: ImageView = itemView.findViewById(R.id.Set_imageView)
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