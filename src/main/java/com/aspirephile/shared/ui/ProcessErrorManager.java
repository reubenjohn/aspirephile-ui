package com.aspirephile.shared.ui;

import android.content.Context;

import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class ProcessErrorManager {
    private Logger l = new Logger(ProcessErrorManager.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);
    String retryText = "Retry";
    OnProcessErrorRetry retryListener;
    private ProcessErrorFragment processErrorFragment = null;
    public final int requestCode;

    public ProcessErrorManager(Context context, int requestCode) {
        retryText = context.getResources().getString(R.string.process_error_ui_retry);
        this.requestCode = requestCode;
    }

    public void onAttach(ProcessErrorFragment processErrorFragment) {
        this.processErrorFragment = processErrorFragment;
    }

    public void onDetach() {
        this.processErrorFragment = null;
    }

    public void onRetry() {
        if (asserter.assertPointer(retryListener)) {
            retryListener.onRetry();
        }
    }

    public void setRetryText(String retryText) {
        this.retryText = retryText;
    }

    public void setError(String error) {
        if (asserter.assertPointer(processErrorFragment)) {
            processErrorFragment.setError(error, requestCode);
        }
    }

    public void showLoading() {
        processErrorFragment.showLoading(requestCode);
    }

    public void setOnRetryListener(OnProcessErrorRetry onRetryListener) {
        this.retryListener = onRetryListener;
    }
}
