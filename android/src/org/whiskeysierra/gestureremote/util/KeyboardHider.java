package org.whiskeysierra.gestureremote.util;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

public class KeyboardHider implements OnClickListener {

    @Override
    public void onClick(View view) {
        final Context context = view.getContext();
        final InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

}
