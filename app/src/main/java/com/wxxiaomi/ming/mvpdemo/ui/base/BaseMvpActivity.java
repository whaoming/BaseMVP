package com.wxxiaomi.ming.mvpdemo.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wxxiaomi.ming.mvpdemo.presenter.base.BasePresenter;
import com.wxxiaomi.ming.mvpdemo.presenter.base.BasePresenterImpl;

/**
 * Created by 12262 on 2016/6/15.
 */
public abstract class BaseMvpActivity<V,T extends BasePresenter<V>> extends AppCompatActivity {
    public T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        presenter.attach((V)this);
        initActtivity(savedInstanceState);
    }

    @Override
    protected void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.dettach();
        super.onDestroy();
    }

    // 实例化presenter
    public abstract T initPresenter();
    public abstract void initActtivity(Bundle savedInstanceState);
}
