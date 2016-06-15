package com.wxxiaomi.ming.mvpdemo.presenter;

import com.wxxiaomi.ming.mvpdemo.presenter.base.BasePresenter;

/**
 * Created by 12262 on 2016/6/15.
 */
public interface LoginPresenter<T> extends BasePresenter<T>{
    void Login(String username,String password);
}
