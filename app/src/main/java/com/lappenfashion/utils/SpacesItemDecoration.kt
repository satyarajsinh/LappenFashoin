package com.lappenfashion.utils

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView

public class SpacesItemDecoration : RecyclerView.ItemDecoration {

    var space: Int = 0

    constructor(space: Int) {
        this.space = space;
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0,space,space,space)
        /*outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
        outRect.top = space;
    } else {
        outRect.top = 0;
    }*/
    }
}