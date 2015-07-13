package com.aspirephile.shared.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

import static android.view.View.OnClickListener;

@SuppressWarnings("UnusedDeclaration")
public class ProcessErrorUI extends Fragment implements OnClickListener {
    private final Logger l = new Logger(ProcessErrorUI.class);
    private final NullPointerAsserter asserter = new NullPointerAsserter(l);


    private LinearLayout mainContainer, errorContainer;
    private View parentContentView;

    private TextView error;
    private String errorText;

    private String retryText;
    private Button retry;

    private OnProcessErrorRetry onProcessErrorRetry;
    private ProgressBar progressBar;

    private enum ProcessState {
        LOADING, ERROR_SET, RESOLVED
    }

    ProcessState state;
    boolean isAnimationEnabled;

    public ProcessErrorUI() {
        l.onConstructor();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        l.onCreate();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        l.onCreateView();
        View v = inflater.inflate(R.layout.fragment_process_error_ui, container,
                false);
        bridgeXML(v);
        initializeFields();
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        l.onAttach();
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        l.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        l.onResume();
        super.onResume();
        if (asserter.assertPointer(state)) {
            switch (state) {
                case ERROR_SET:
                    setError(errorText);
                    break;
                case LOADING:
                    showLoading();
                    errorContainer.setVisibility(View.GONE);
                    break;
                case RESOLVED:
                    resolveErrors();
                    break;
                default:
                    l.e("Unknown Process UI state");
            }
        }
    }

    @Override
    public void onPause() {
        l.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        l.onStop();
        super.onStop();
    }

    @Override
    public void onDetach() {
        l.onDetach();
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        l.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        l.onDestroy();
        super.onDestroy();
    }

    private void bridgeXML(View v) {
        l.bridgeXML();
        mainContainer = (LinearLayout) v.findViewById(R.id.container_process_error_ui_main);
        errorContainer = (LinearLayout) v.findViewById(R.id.container_process_error_ui_error);
        error = (TextView) v.findViewById(R.id.tv_process_error_ui_error);
        retry = (Button) v.findViewById(R.id.b_process_error_ui_retry);
        progressBar = (ProgressBar) v.findViewById(R.id.pb_process_error_ui);
        l.bridgeXML(asserter.assertPointer(mainContainer, error, retry, progressBar, errorContainer));
    }

    private void initializeFields() {
        l.initializeFields();
        isAnimationEnabled = true;
        retry.setOnClickListener(this);
    }

    public void setAnimationsEnabled(boolean isAnimationEnabled) {
        this.isAnimationEnabled = isAnimationEnabled;
    }

    public void setParentContentView(View view) {
        this.parentContentView = view;
    }

    public void setError(String errorText) {
        this.errorText = errorText;
        if (asserter.assertPointerQuietly(error)) {
            error.setText(errorText);
        }
        internalSetError();
    }

    public void setRetryText(String retryText) {
        this.retryText = retryText;
        if (asserter.assertPointerQuietly(retry))
            retry.setText(retryText);
    }

    private void internalSetError() {
        l.d("Setting errors");
        state = ProcessState.ERROR_SET;
        if (asserter.assertPointerQuietly(progressBar, errorContainer)) {
            progressBar.setVisibility(View.GONE);
            errorContainer.setVisibility(View.VISIBLE);
        }
        makeParentContentViewInvisible();
    }

    public void showLoading() {
        l.d("Showing loading");
        state = ProcessState.LOADING;
        if (asserter.assertPointerQuietly(progressBar, errorContainer)) {
            progressBar.setVisibility(View.VISIBLE);
            errorContainer.setVisibility(View.GONE);
        }
        makeParentContentViewInvisible();
    }

    public void resolveErrors() {
        l.d("Resolving errors");
        state = ProcessState.RESOLVED;
        if (asserter.assertPointerQuietly(progressBar, errorContainer)) {
            progressBar.setVisibility(View.GONE);
            errorContainer.setVisibility(View.GONE);
        }
        makeParentContentViewVisible();
    }

    private void makeParentContentViewVisible() {
        if (asserter.assertPointerQuietly(parentContentView))
            parentContentView.setVisibility(View.VISIBLE);
    }

    private void makeParentContentViewInvisible() {
        if (asserter.assertPointerQuietly(parentContentView))
            parentContentView.setVisibility(View.INVISIBLE);
    }

    private void makeParentContentViewGone() {
        if (asserter.assertPointerQuietly(parentContentView))
            parentContentView.setVisibility(View.INVISIBLE);
    }

    private void show() {
        internalShow(isAnimationEnabled);
    }

    private void show(boolean animate) {
        internalShow(animate);
    }

    private void internalShow(boolean animate) {
        if (!isVisible()) {
            l.d("Showing fragment");
            FragmentTransaction manager = getFragmentManager().beginTransaction();
            if (isAnimationEnabled) {
                manager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right);
            }
            makeParentContentViewGone();
            if (asserter.assertPointerQuietly(manager))
                manager.show(this).commit();
        }
    }

    private void hide() {
        internalHide(this.isAnimationEnabled, false);
    }

    private void hide(boolean isAnimationEnabled) {
        internalHide(isAnimationEnabled, false);
    }

    private void internalHide(boolean animate, boolean force) {
        if (isVisible() || force) {
            l.d("Hiding fragment");
            FragmentTransaction manager = getFragmentManager().beginTransaction();
            if (animate) {
                manager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right);
            }
            makeParentContentViewVisible();
            if (asserter.assertPointerQuietly(manager))
                manager.hide(this).commit();
        }
    }

    public void setOnProcessErrorRetry(OnProcessErrorRetry onProcessErrorRetry) {
        this.onProcessErrorRetry = onProcessErrorRetry;
    }

    @Override
    public void onClick(View v) {
        if (asserter.assertPointer(onProcessErrorRetry))
            onProcessErrorRetry.onRetry(v);
    }
}
