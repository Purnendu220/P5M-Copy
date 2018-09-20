
package com.p5m.me.data;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RatingFeedbackAreaResList implements Serializable {

    private Long id;
    private Long parameterId;
    private String parameterName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

}
