package com.ericktijerou.hackernews.presentation.ui.feed

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(private val onDelete: (CustomViewHolder) -> Unit) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        viewHolder1: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        viewHolder.let {
            val listItemViewHolder = viewHolder as CustomViewHolder
            onDelete(listItemViewHolder)
        }
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView: View? =
            (viewHolder as? FeedPagedListAdapter.FeedViewHolder)?.viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(
            c,
            recyclerView,
            foregroundView,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView: View? =
            (viewHolder as? FeedPagedListAdapter.FeedViewHolder)?.viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil()
            .clearView(foregroundView)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView: View? =
            (viewHolder as? FeedPagedListAdapter.FeedViewHolder)?.viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(
            c,
            recyclerView,
            foregroundView,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        val foregroundView: View? =
            (viewHolder as? FeedPagedListAdapter.FeedViewHolder)?.viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
    }
}