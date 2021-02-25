package com.project.base.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinshaobo at 2021/2/25 14:31
 */
public class ValidatorResult {

    private boolean hasError;

    private List<String> errorMessages;

    public ValidatorResult() {
        this.hasError = false;
        this.errorMessages = new ArrayList<>();
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void addErrorMessage(String errorMsg) {
        this.errorMessages.add(errorMsg);
    }
}
