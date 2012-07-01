package org.whiskeysierra.gestureremote.util;

import android.app.Activity;
import android.view.View;
import com.markupartist.android.widget.ActionBar.Action;

public final class FinishAction implements Action {

    private final Activity activity;
    private final int drawable;

    public FinishAction(Activity activity, int drawable) {
        this.activity = activity;
        this.drawable = drawable;
    }

    @Override
    public int getDrawable() {
        return drawable;
    }

    @Override
    public void performAction(View view) {
        activity.finish();
    }
}
