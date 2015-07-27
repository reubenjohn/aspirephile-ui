package com.aspirephile.shared.ui;

import android.content.Context;

import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class ProcessErrorManager {
    public static final String parentActivityAttachMethodName = "attachProcessErrorManager";
    public static final String parentActivityDetachMethodName = "detachProcessErrorManager";
    private Logger l = new Logger(ProcessErrorManager.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);
    String retryText;
    OnProcessErrorRetry retryListener;
    private ProcessErrorFragment processErrorFragment = null;
    public final int requestCode;
    private String queuedErrorText;
    int queueCommand = QUEUE_EMPTY;
    static final int QUEUE_EMPTY = 0;
    static final int QUEUE_LOADING = 1;
    static final int QUEUE_ERROR = 2;

    public ProcessErrorManager(Context context, int requestCode) {
        retryText = context.getResources().getString(R.string.process_error_ui_retry);
        this.requestCode = requestCode;
    }

    public void onAttach(ProcessErrorFragment processErrorFragment) {
        l.onAttach();
        this.processErrorFragment = processErrorFragment;
        if (queueCommand == QUEUE_ERROR) {
            setError(queuedErrorText);
        } else if (queueCommand == QUEUE_LOADING) {
            showLoading();
        }
    }

    public void onDetach() {
        l.onDetach();
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
        if (asserter.assertPointerQuietly(processErrorFragment)) {
            processErrorFragment.setError(error, requestCode);
        } else {
            l.w("Manager has not been attached. The command is being queued instead");
            queueCommand = QUEUE_ERROR;
            this.queuedErrorText = error;
        }
    }

    public void showLoading() {
        if (asserter.assertPointerQuietly(processErrorFragment)) {
            processErrorFragment.showLoading(requestCode);
        } else {
            l.w("Manager has not been attached. The command is being queued instead");
            queueCommand = QUEUE_LOADING;
        }
    }

    public void setOnRetryListener(OnProcessErrorRetry onRetryListener) {
        this.retryListener = onRetryListener;
    }
}
