package com.aspirephile.shared.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
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

public class ProcessErrorUI extends Fragment implements OnClickListener {
    private final Logger l = new Logger(ProcessErrorUI.class);
    private final NullPointerAsserter asserter = new NullPointerAsserter(l);


    private LinearLayout mainContainer, errorContainer;
    private View parentContentView;

    private TextView error;
    private String errorText;

    private String retryText;
    private Button retry;
    private OnClickListener onClickListener;
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
        View v = getDynamicallyCreatedContentView();
        initializeFields();
        return v;
    }

    private View getDynamicallyCreatedContentView() {
        l.d("Dynamically generating Process error UI");
        {
            {
                {
                    error = new TextView(getActivity());
                    error.setText(errorText);
                    error.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
                }
                {
                    retry = new Button(getActivity());
                    retry.setText(retryText);
                }
                {
                    errorContainer = new LinearLayout(getActivity());
                    errorContainer.setOrientation(LinearLayout.VERTICAL);
                    errorContainer.setGravity(Gravity.CENTER);
                    errorContainer.addView(error);
                    errorContainer.addView(retry);
                }
            }
            {
                progressBar = new ProgressBar(getActivity());
            }
            {
                mainContainer = new LinearLayout(getActivity());
                mainContainer.setOrientation(LinearLayout.VERTICAL);
                errorContainer.setGravity(Gravity.CENTER);
                mainContainer.addView(errorContainer);
                mainContainer.addView(progressBar);
            }
        }
        l.bridgeXML(asserter.assertPointer(error, retry, progressBar, errorContainer));
        return mainContainer;
    }

    private void initializeFields() {
        l.initializeFields();
        isAnimationEnabled = true;
        retry.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        l.onResume();
        super.onResume();
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

    public void setAnimationsEnabled(boolean isAnimationEnabled) {
        this.isAnimationEnabled = isAnimationEnabled;
    }

    public void setParentContentView(View view) {
        this.parentContentView = view;
    }

    public void setError(String error) {
        if (asserter.assertPointerQuietly(this.error)) {
            this.error.setText(error);
        } else {
            errorText = error;
        }
        internalSetError();
    }

    public void setRetryText(String retryText) {
        this.retryText = retryText;
    }

    private void internalSetError() {
        l.d("Setting errors");
        state = ProcessState.ERROR_SET;
        if (asserter.assertPointerQuietly(progressBar, errorContainer)) {
            progressBar.setVisibility(View.GONE);
            errorContainer.setVisibility(View.VISIBLE);
        }
        if (parentContentView != null)
            parentContentView.setVisibility(View.INVISIBLE);
    }

    public void showLoading() {
        l.d("Showing loading");
        state = ProcessState.LOADING;
        if (asserter.assertPointerQuietly(progressBar, errorContainer)) {
            progressBar.setVisibility(View.VISIBLE);
            errorContainer.setVisibility(View.GONE);
        }
        if (parentContentView != null)
            parentContentView.setVisibility(View.INVISIBLE);
    }

    public void resolveErrors() {
        l.d("Resolving errors");
        state = ProcessState.RESOLVED;
        if (asserter.assertPointer(progressBar, errorContainer)) {
            progressBar.setVisibility(View.GONE);
            errorContainer.setVisibility(View.GONE);
        }
        if (parentContentView != null)
            parentContentView.setVisibility(View.VISIBLE);
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
            if (parentContentView != null)
                parentContentView.setVisibility(View.GONE);
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
            if (parentContentView != null)
                parentContentView.setVisibility(View.VISIBLE);
            if (asserter.assertPointerQuietly(manager))
                manager.hide(this).commit();
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        if (asserter.assertPointer(onClickListener))
            onClickListener.onClick(v);
    }
}
