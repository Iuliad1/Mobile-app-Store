package com.example.store2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.store2.R
import com.example.store2.databinding.ViewpagerImageItemBinding

class ViewPageForImages: RecyclerView.Adapter<ViewPageForImages.ViewPageForImagesViewHolder>() {

    inner class ViewPageForImagesViewHolder(val binding: ViewpagerImageItemBinding) :
        ViewHolder(binding.root) {
        fun bind(imagePath: String) {
            Glide.with(itemView)
                .load(imagePath)
                //.placeholder(R.drawable.placeholder_image) // drawable for loading
                //.error(R.drawable.error_image) // drawable for error handling
                .into(binding.imageProdDetails)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPageForImagesViewHolder {
        return  ViewPageForImagesViewHolder(
            ViewpagerImageItemBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPageForImagesViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
