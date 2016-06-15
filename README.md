# BaseMVP
一个最基础的mvp代码结构，优雅地处理了各层之间的绑定和初始化。解决了presenter和activity的生命周期方法
前言
===================
在之前的一篇文章中，关于对MVP的了解和介绍已经大概说了一下：
>Android程序结构--MVP模式
> http://blog.csdn.net/qq122627018/article/details/51459116

但是在实际的应用中其实有很多问题出现，例如我在写上面这篇博文之后，把自己的项目从mvc优化成mvp的过程中遇到了很多问题，总结一下：

 1. 对于view和presenter的业务代码不清晰，导致业务逻辑总是交错出现在这俩个模块中
 2. view和presenter的耦合度太高，不能体现出MVP的特色
 3. 在presenter中一直持有activity对象，导致内存泄漏
 4. 由于MVP模式中的基类封装得不好，导致很多重复代码的出现
 
所以在这里做个笔记，lz自己封装了一个大众化的比较简单的MVP基础架构，可以省去很多重复的代码
一个最基础的MVP结构
=================
一个最基础的MVP架构无非就是view->presenter->model,既然这是一个通用的结构，自然就少不了抽取出基类去节省工作，如图：
![这里写图片描述](http://img.blog.csdn.net/20160615195600232)
下面我们来说一说这个结构的优点
优雅地处理presenter的生命周期问题
---------------------------------
在presenter中，肯定需要有方法去维护所绑定的view的生命周期，不然当view退出的时候，presenter的动作还在继续执行着，这样很容易导致内存泄露。但是我们不可能每个view都去添加onCreate和onResume方法，这时候就需要把这些生命周期相关的方法放到基类中，也就是一个BasePresenter

```
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
```
所以一个presenter的样子应该是这样的：

```
/**
 * Created by 12262 on 2016/6/15.
 */
public interface LoginPresenter<T> extends BasePresenter<T>{
    void Login(String username,String password);
}
```
那么如果在每个presenter中都必须去实现BasePresenter里的方法的话，那肯定也是不符合软件设计规范的(让我装装b)，那该怎么办呢？这时候BasePresenterImpl就出现了。
```
/**
 * Created by 12262 on 2016/6/15.
 * by Mr.W
 * email:122627018@qq.com
 */
public abstract class BasePresenterImpl<T> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void dettach() {
        mView = null;
    }
    public void onResume(){};
}
```



所以一个完整的presenter应该是这样的：
```
/**
 * Created by 12262 on 2016/6/15.
 * by Mr.W
 * email:122627018@qq.com
 */
public class LoginPresenterImpl extends BasePresenterImpl<LoginView> implements LoginPresenter<LoginView>{
    @Override
    public void Login(String username, String password) {
        mView.showLoginDialog();
    }

    @Override
    public void attach(LoginView mView) {
        super.attach(mView);
        //这里就在view的onCreate()方法里调用，适合做各种初始化操作
    }
}
```

View与presenter配合处理生命周期
------------------------
BaseActivity扮演的角色，就是调用presenter的公共方法，也就是BasePresenter里的方法。想象一下，如果没有BaseActivity，那么每个Activity都需要重写自己的onResume()方法和onCreate（），然后在这些方法里面调用presenter的生命周期的方法。所以这些工作就全部交给BaseActivity来做。
```
/**
 * Created by 12262 on 2016/6/15.
 * by Mr.W
 * email:122627018@qq.com
 */
public abstract class BaseMvpActivity<V,T extends BasePresenter<V>> extends AppCompatActivity {
    public T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initPresenter()是抽象方法，让view初始化自己的presenter
        presenter = initPresenter();
        //presenter和view的绑定
        presenter.attach((V)this);
        //initActtivity是抽象方法，让view完成自身各种控件的初始化
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
```
所以BaseMvpActivity完成的工作，就是帮每个Activity绑定所对应的presenter，而且在activity的生命周期方法调用时，也完成对presenter生命周期方法的嗲用，所以一个完整的activity应该是这样子的：

```
public interface LoginView {
    void showLoginDialog();
}
```

```

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
```

关于View与Presenter的业务逻辑问题
------------------------------
在这个问题中，我也参考了很多篇文章，比如

> 谈谈关于MVP模式中V-P交互问题
> http://www.cnblogs.com/artech/archive/2010/03/25/1696205.html

这篇文章戳中了我的痛点，其中有一段话：

> 尽量弱化（不可能剔除）View对Presenter的依赖。实际上，对于MVP来说，View仅仅向Presenter递交用户交互请求，仅此而已。如果我们将View对Presenter的这点依赖关系实现在框架层次中，最终开发人员的编程来说就不需要这种依赖了。那么我就可以通过一定的编程技巧使View根本无法访问Presenter，从而避免Presenter成为Proxy的可能的


所以在使用MVP的过程中，尽量不要让view过度依赖presenter，把presenter看成是主角，你可能会更好地把你的代码写得更优雅


文章demo地址：

> github：https://github.com/122627018/BaseMVP



