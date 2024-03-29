package com.seuit.eworkout.training.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seuit.eworkout.databinding.FragmentSetsItemBinding
import com.seuit.eworkout.training.listener.SetOnClickListener
import com.seuit.eworkout.training.model.Set

class SetsAdapter(val list: List<Set>, val listener: SetOnClickListener) : RecyclerView.Adapter<SetsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: FragmentSetsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(set: Set) {
            set.apply {
                val bundle = Bundle().apply {
                    putString("set_id", setId)
                    putBoolean("isSystemSet", true)
                }
                binding.textViewSetName.text = setName
                binding.textViewTotalExercises.text = totalExercises.toString()
                binding.texViewTotalTime.text = totalTime.toString() + " mins"
                binding.buttonViewDetail.setOnClickListener {
                    listener.onClick(bundle)
                    Log.d("set_id",setId)
                }
                if(setImage != ""){
                    Glide.with(binding.root.context).load(setImage)
                        .fitCenter()
                        .into(binding.SetImageView)
                }

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
}