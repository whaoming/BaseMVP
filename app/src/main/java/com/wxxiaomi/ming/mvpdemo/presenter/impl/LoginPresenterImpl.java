package com.wxxiaomi.ming.mvpdemo.presenter.impl;

import com.wxxiaomi.ming.mvpdemo.presenter.LoginPresenter;
import com.wxxiaomi.ming.mvpdemo.presenter.base.BasePresenterImpl;
import com.wxxiaomi.ming.mvpdemo.ui.view.LoginView;

/**
 * Created by 12262 on 2016/6/15.
 */
public class LoginPresenterImpl extends BasePresenterImpl<LoginView> implements LoginPresenter<LoginView>{
    @Override
    public void Login(String username, String password) {
        mView.showLoginDialog();
    }

    @Override
    public void attach(LoginView mView) {
        super.attach(mView);
    }
}
