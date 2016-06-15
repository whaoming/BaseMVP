package com.wxxiaomi.ming.mvpdemo.presenter.base;

/**
 * Created by 12262 on 2016/6/15.
 * by Mr.W
 * email:122627018@qq.com
 */
public interface BasePresenter<T> {
    //绑定view，这个方法将会在activity中调用
    void attach(T mView);
    //解绑
    void dettach();
    //也就是activity的onResume(),presenter一般在这个方法里面做一些监听事件
    void onResume();
}
