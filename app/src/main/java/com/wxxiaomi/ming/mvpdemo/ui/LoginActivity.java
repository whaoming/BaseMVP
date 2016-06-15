package com.wxxiaomi.ming.mvpdemo.ui;

import android.os.Bundle;

import com.wxxiaomi.ming.mvpdemo.presenter.LoginPresenter;
import com.wxxiaomi.ming.mvpdemo.presenter.impl.LoginPresenterImpl;
import com.wxxiaomi.ming.mvpdemo.ui.base.BaseMvpActivity;
import com.wxxiaomi.ming.mvpdemo.ui.view.LoginView;

public class LoginActivity extends BaseMvpActivity<LoginView,LoginPresenter<LoginView>> implements LoginView {
    @Override
    public LoginPresenter<LoginView> initPresenter() {
        return new LoginPresenterImpl();
    }

    @Override
    public void initActtivity(Bundle savedInstanceState) {

    }

    @Override
    public void showLoginDialog() {

    }
}
