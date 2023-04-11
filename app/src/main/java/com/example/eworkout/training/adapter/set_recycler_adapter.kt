package com.example.eworkout.training.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class set_recycler_adapter: RecyclerView.Adapter<set_recycler_adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): set_recycler_adapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: set_recycler_adapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    init class ViewHolder (val binding: ExerciseRecyclerviewItemBinding) : set_recycler_adapter.ViewHolder(binding.root)
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