package com.claudechrist.premiere_soccer_league_app;

import android.content.Context;
import android.content.Intent;

public class CustomIntent extends Intent {

    private int itemPosition;

    public CustomIntent(Context packageContext, Class<?> cls, int itemPosition) {
        super(packageContext, cls);
        this.itemPosition = itemPosition;
    }

    public int getItemPosition() {
        return itemPosition;
    }
}
