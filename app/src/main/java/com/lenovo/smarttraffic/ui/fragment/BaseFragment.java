package com.lenovo.smarttraffic.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lenovo.smarttraffic.ui.activity.BaseActivity;
import com.lenovo.smarttraffic.util.CommonUtil;
import com.lenovo.smarttraffic.widget.ContentPage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public abstract class BaseFragment extends SupportFragment implements View.OnClickListener {
    public ContentPage contentPage;
    public ProgressDialog pdLoading;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    public ExecutorService service;
    public  String TAG = this.getClass().getSimpleName();
    public  String objlist;
    private String hendr = "";
    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
        service = new ThreadPoolExecutor(
                7,
                20,
                1000,
                TimeUnit.MILLISECONDS,
                //等待队列
                new ArrayBlockingQueue<Runnable>(10),
                //销毁队列如果线程大于等待的和最大的相加
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*
         * 初始化pdLoading
         */
        pdLoading = new ProgressDialog(getActivity());
        pdLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdLoading.setMessage("请稍后");
        pdLoading.setCanceledOnTouchOutside(false);
        pdLoading.setCancelable(true);
        if (contentPage == null) {
            contentPage = new ContentPage(getActivity()) {
                @Override
                public Object loadData() {
                    return requestData();
                }

                @Override
                public View createSuccessView() {
                    return getSuccessView();
                }
            };
        } else {
            CommonUtil.removeSelfFromParent(contentPage);
        }
        return contentPage;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
    }

    /**
     * 初始化 Toolbar
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        ((BaseActivity) getActivity()).initToolBar(toolbar, homeAsUpEnabled, title);
    }

    /**
     * 刷新状态
     */
    public void refreshPage(Object o) {
        contentPage.refreshPage(o);
    }

    /**
     * 返回据的fragment填充的具体View
     */
    protected abstract View getSuccessView();

    /**
     * 返回请求服务器的数据
     */
    protected abstract Object requestData();
    


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

}