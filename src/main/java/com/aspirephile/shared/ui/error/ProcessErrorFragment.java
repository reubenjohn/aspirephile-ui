package com.aspirephile.shared.ui.error;


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
import com.aspirephile.shared.ui.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;

@SuppressWarnings("UnusedDeclaration")
public class ProcessErrorFragment extends Fragment implements OnClickListener {
    private final Logger l = new Logger(ProcessErrorFragment.class);
    private final NullPointerAsserter asserter = new NullPointerAsserter(l);


    private LinearLayout mainContainer, errorContainer;
    private View parentContentView;

    private TextView error;
    private Button retry;

    private ProgressBar progressBar;
    private List<ProcessErrorManager> processErrorManagers = new ArrayList<>();
    private List<Integer> requestCodes = new ArrayList<>();

    private String errorText;
    private String retryText;

    private enum ProcessState {
        LOADING, ERROR_SET, NONE
    }

    ProcessState state;
    boolean isAnimationEnabled;

    public ProcessErrorFragment() {
        l.onConstructor();
        isAnimationEnabled = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        l.onCreate();
        setRetainInstance(true);
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
        restoreUIState();
        return v;
    }

    private void restoreUIState() {
        l.d("Restoring UI state");
        if (asserter.assertPointerQuietly(retryText))
            setRetryText(retryText);
        try {
            if (asserter.assertPointerQuietly(state)) {
                switch (state) {
                    case ERROR_SET:
                        setError(errorText, getLatestRequestCode());
                        break;
                    case LOADING:
                        showLoading(getLatestRequestCode());
                        break;
                    case NONE:
                        setError(null, getLatestRequestCode());
                        break;
                    default:
                        l.e("Unknown Process UI state");
                }
            } else {
                setError(null, getLatestRequestCode());
            }
        } catch (IndexOutOfBoundsException e) {
            l.w("No recent request codes to select from");
            setError(null, 0);
        }
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

    private void setRetryText(String retryText) {
        this.retryText = retryText;
        if (asserter.assertPointerQuietly(retry))
            retry.setText(retryText);
    }

    public void setError(String errorText, int requestCode) {
        if (requestCode != 0)
            handleErrorSetRequestCode(errorText, requestCode);
        if (asserter.assertPointerQuietly(errorText)) {
            l.d("Setting errors");

            this.errorText = errorText;
            if (asserter.assertPointerQuietly(error)) {
                error.setText(errorText);
            }
            state = ProcessState.ERROR_SET;
            if (asserter.assertPointerQuietly(progressBar, errorContainer)) {
                progressBar.setVisibility(View.GONE);
                errorContainer.setVisibility(View.VISIBLE);
            }
            makeParentContentViewInvisible();
        } else {
            l.d("Resolving errors");

            this.errorText = errorText;

            state = ProcessState.NONE;
            if (asserter.assertPointerQuietly(progressBar, errorContainer)) {
                progressBar.setVisibility(View.GONE);
                errorContainer.setVisibility(View.GONE);
            }
            makeParentContentViewVisible();
        }
    }

    private void handleErrorSetRequestCode(String errorText, int requestCode) {
        if (asserter.assertPointerQuietly(errorText)) {
            requestCodes.remove((Integer) requestCode);
            requestCodes.add(requestCode);
        } else {
            requestCodes.remove((Integer) requestCode);
        }
    }

    public void showLoading(int requestCode) {
        l.d("Showing loading");
        requestCodes.remove((Integer) requestCode);
        requestCodes.add(requestCode);
        state = ProcessState.LOADING;
        if (asserter.assertPointerQuietly(progressBar, errorContainer)) {
            progressBar.setVisibility(View.VISIBLE);
            errorContainer.setVisibility(View.GONE);
        }
        makeParentContentViewInvisible();
    }

    private void makeParentContentViewVisible() {
        if (asserter.assertPointerQuietly(parentContentView))
            parentContentView.setVisibility(View.VISIBLE);
        else
            l.w("The parent content view has not been set. Ignoring attempt to make it visible");
    }

    private void makeParentContentViewInvisible() {
        if (asserter.assertPointerQuietly(parentContentView))
            parentContentView.setVisibility(View.INVISIBLE);
        else
            l.w("The parent content view has not been set. Ignoring attempt to make it invisible");
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
            makeParentContentViewInvisible();
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

    public void attachOnProcessErrorManager(ProcessErrorManager errorManager) {
        processErrorManagers.add(errorManager);
        errorManager.onAttach(ProcessErrorFragment.this);
    }

    public void detachProcessErrorManager(ProcessErrorManager errorManager) {
        processErrorManagers.remove(errorManager);
        errorManager.onDetach();
    }

    @Override
    public void onClick(View v) {
        try {
            int latestRequestCode = getLatestRequestCode();
            for (ProcessErrorManager manager : processErrorManagers) {
                if (latestRequestCode == manager.requestCode) {
                    manager.onRetry();
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int getLatestRequestCode() throws IndexOutOfBoundsException {
        return requestCodes.get(requestCodes.size() - 1);
    }

}
