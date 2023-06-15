package com.example.eworkout.training.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eworkout.databinding.FragmentHistoryBinding
import com.example.eworkout.databinding.FragmentSetsItemBinding
import com.example.eworkout.databinding.SetTakenHistoryBinding
import com.example.eworkout.training.listener.SetOnClickListener
import com.example.eworkout.training.model.Set

class HistoryAdapter(val list: List<Set>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>()  {

    inner class ViewHolder(val binding: SetTakenHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: Set) {
            set.apply {
                binding.textViewSetNameHistory.text = setName
                binding.textViewTotalKCALHistory.text = totalExercises.toString()
                binding.texViewDuration.text = totalTime.toString()
                if(setImage != ""){
                    Glide.with(binding.root.context).load(setImage)
                        .fitCenter()
                        .into(binding.SetImageViewHistory)
                }

                if(set.totalTime == 0L){
                    binding.texViewDuration.visibility = View.INVISIBLE
                    binding.txtView2.visibility = View.INVISIBLE
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SetTakenHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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