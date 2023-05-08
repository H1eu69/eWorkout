package com.example.eworkout.custom_workout.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.eworkout.custom_workout.listener.CartOnCLickListener
import com.example.eworkout.custom_workout.model.ExerciseInCart
import com.example.eworkout.databinding.CartItemBinding


class CartAdapter(
    val list: List<ExerciseInCart>,
    val listener: CartOnCLickListener
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: ExerciseInCart)
        {
            item.apply {
                binding.textViewName.text = name
                if(repType == "Reps")
                    binding.textViewRep.text = "x" + reps.toString()
                else
                    binding.textViewRep.text = reps.toString() + "s"

                var requestOption = RequestOptions()
                requestOption = requestOption.transform(CenterCrop(), RoundedCorners(40))

                Glide.with(binding.root.context)
                    .load(image)
                    .apply(requestOption)
                    .into(binding.imageView)

                val bundle = Bundle().apply {
                    putString("exercise_id", id)
                    putString("name", name)
                    putString("image", image)
                }

                binding.btnEdit.setOnClickListener {
                    listener.onEdit(bundle)
                }

                binding.btnDelete.setOnClickListener {
                    listener.onDelete(bundle)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        holder.bind(item)
    }
}