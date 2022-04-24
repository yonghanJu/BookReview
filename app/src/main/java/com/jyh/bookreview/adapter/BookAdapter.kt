package com.jyh.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jyh.bookreview.databinding.ItemBookBinding
import com.jyh.bookreview.model.Book

class BookAdapter:ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil){

    interface OnItemClickedListener{
        fun onItemClicked(bookModel:Book)
    }

    var onItemClickedListener:OnItemClickedListener? = null

    inner class BookItemViewHolder(private val binding: ItemBookBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(bookModel:Book){
            binding.titleTextView.text = bookModel.title
            binding.descriptionTextView.text = bookModel.description

            Glide
                .with(binding.titleTextView.context)
                .load(bookModel.coverSmallUrl)
                .into(binding.coverImageView)

            binding.root.setOnClickListener {
                onItemClickedListener?.onItemClicked(bookModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        // BookItemViewHolder를 반환
        // ItemBookBinding.inflate()을 통해 binding 생성
        // LayoutInflater.from(parent.context) 를 통해 parent:ViewGroup 의 context 사용
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<Book>(){
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}