package www.gymhop.p5m.adapters;

import android.view.View;

/**
 * Created by MyU10 on 3/15/2018.
 */

public interface AdapterCallbacks<T> {
    void onItemClick(View viewRoot, View view, T model, int position);

    void onItemLongClick(View viewRoot, View view, T model, int position);

    void onShowLastItem();
}
