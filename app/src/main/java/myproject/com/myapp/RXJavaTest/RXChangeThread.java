package myproject.com.myapp.RXJavaTest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import myproject.com.myapp.RetrofitTest.Api;
import myproject.com.myapp.RetrofitTest.RetrofitProvider;
import myproject.com.myapp.RetrofitTest.entity.LoginRequest;
import myproject.com.myapp.RetrofitTest.entity.LoginResponse;

/**
 * Created by wang on 01/04/17.
 */

public class RXChangeThread {
    private static final String TAG = "RXChangeThread";

    /**
     * 切换线程：
     * subscribeOn方法用来指定Observable对象执行的线程
     * observeOn用来指定Observer对象执行的线程
     * 多次指定Observable的线程只有第一次有效，其余的会被忽略
     * 多次指定Observer的线程，每设置一次就会切换一次
     */
    public static void changeThreadTest1() {
        Log.d(TAG, "changeThreadTest1: start");
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe thread:" + Thread.currentThread().getName());
                e.onNext(1);
            }
        });
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: thread->" + Thread.currentThread().getName());
                Log.d(TAG, "accept: " + integer);
            }
        };
        //多次指定Observable的线程只有第一次有效，其余的会被忽略
        //多次指定Observer的线程，每设置一次就会切换一次
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
        Log.d(TAG, "changeThreadTest1: end");
    }

    /**
     * 多次指定Observable的线程只有第一次有效，其余的会被忽略
     * 多次指定Observer的线程，每设置一次就会切换一次
     */
    public static void changeThreadTest2() {
        Log.d(TAG, "changeThreadTest2: start");
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe: thread:" + Thread.currentThread().getName());
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer + " thread after mainThread:" + Thread.currentThread().getName());
                    }
                }).observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer + " thread after ioThread:" + Thread.currentThread().getName());
                    }
                }).subscribe();
        Log.d(TAG, "changeThreadTest2: end");
    }

    /**
     * Rx和Retrofit结合进行网络请求
     */
    public static void retrofitRXTest(final Context context) {
        Api api = RetrofitProvider.get().create(Api.class);
        api.login(new LoginRequest())
                .subscribeOn(Schedulers.io())//网络请求在子线程
                .observeOn(AndroidSchedulers.mainThread())//请求结果在主线程中展示
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(LoginResponse value) {
                        Log.d(TAG, "onNext: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
