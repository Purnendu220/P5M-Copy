package www.gymhop.p5m.adapters;

import android.view.View;

/**
 * Created by MyU10 on 3/15/2018.
 */

public interface AdapterCallbacks<Object> {
    void onAdapterItemClick(View viewRoot, View view, Object model, int position);

    void onAdapterItemLongClick(View viewRoot, View view, Object model, int position);

    void onShowLastItem();
}
