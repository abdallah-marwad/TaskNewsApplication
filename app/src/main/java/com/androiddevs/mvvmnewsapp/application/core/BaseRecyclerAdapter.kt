package com.tailors.doctoria.application.core

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/*
    created at 01/01/2024
    by Abdallah Marwad
    abdallahshehata311as@gmail.com
 */
abstract class BaseRecyclerAdapter<T, VB : BaseViewHolder<T>> :
    RecyclerView.Adapter<VB>() {
    val data by lazy { ArrayList<T>() }
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VB
    override fun onBindViewHolder(holder: VB, position: Int) {
        holder.bind(data[holder.adapterPosition])
    }

    fun addItem(item: T) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }

    fun addItemList(item: ArrayList<T>) {
        data.addAll(item)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < data.size) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): T {
        return data[position]
    }
}

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}