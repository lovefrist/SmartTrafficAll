package com.lenovo.smarttraffic.ui.fragment;

import android.view.View;
import android.widget.TextView;

public class ForFragment extends BaseFragment {
    @Override
    protected View getSuccessView() {
        TextView textView = new TextView(getActivity());
        textView.setText("第三页");
        return textView;
    }

    @Override
    protected Object requestData() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
