package com.ericktijerou.hackernews.presentation.ui.feed

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun recycle()
}