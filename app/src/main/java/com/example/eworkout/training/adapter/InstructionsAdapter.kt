package com.example.eworkout.training.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eworkout.databinding.ExerciseRecyclerviewItemBinding
import com.example.eworkout.databinding.HowToDoRecyclerviewItemBinding
import com.example.eworkout.training.model.Exercise
import com.example.eworkout.training.model.Instruction

class InstructionsAdapter(val list: List<Instruction>): RecyclerView.Adapter<InstructionsAdapter.ViewHolder>() {
    var numberOfStep = 1
    inner class ViewHolder(val binding: HowToDoRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Instruction) {
            binding.stepTextview.text = numberOfStep.toString()
            binding.instructionTextview1.text = item.title
            binding.instructionTextview2.text = item.content
            numberOfStep += 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HowToDoRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InstructionsAdapter.ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount() = list.size

}