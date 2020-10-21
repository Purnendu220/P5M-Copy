
package com.p5m.me.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class SafetyRemarkOptions {

    @SerializedName("options")
    private List<String> mOptions;

    public List<String> getOptions() {
        return mOptions;
    }

    public void setOptions(List<String> options) {
        mOptions = options;
    }

}
