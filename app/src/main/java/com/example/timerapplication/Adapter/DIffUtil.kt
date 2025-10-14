package com.example.timerapplication.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.timerapplication.data.TimerModel

class DIffUtils(
    private val oldList: List<TimerModel>,
    private val newList: List<TimerModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}