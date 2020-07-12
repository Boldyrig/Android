package com.gmail.fuskerr63.android.library.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/***
 * Простейший декоратор, добавляющий отступы
 */
public class ContactDecorator extends RecyclerView.ItemDecoration {
    private final transient int pixelOffset;

    /***
     *
     * @param pixelOffset  значение отступов в <b>пикселях</b>
     */
    public ContactDecorator(int pixelOffset) {
        this.pixelOffset = pixelOffset;
    }

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = pixelOffset;
        }
        outRect.left = pixelOffset;
        outRect.right = pixelOffset;
    }
}
