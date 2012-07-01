package org.whiskeysierra.gestureremote.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.markupartist.android.widget.ActionBar.Action;

public final class ChildActivityIntentAction implements Action {

    private final Activity parent;
    private final Class<? extends Activity> activity;
    private final int drawable;

    public ChildActivityIntentAction(Activity parent, Class<? extends Activity> activity, int drawable) {
        this.parent = parent;
        this.activity = activity;
        this.drawable = drawable;
    }

    @Override
    public int getDrawable() {
        return drawable;
    }

    @Override
    public void performAction(View view) {
        parent.startActivityForResult(new Intent(parent, activity), 0);
    }
}
