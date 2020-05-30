package com.gmail.fuskerr63.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactDecorator extends RecyclerView.ItemDecoration {
    private final int offset;

    public ContactDecorator(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view) != 0) {
            outRect.top = offset;
        }
        outRect.left = offset;
        outRect.right = offset;
    }
}
