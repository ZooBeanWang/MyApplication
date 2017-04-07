package myproject.com.myapp.RXJavaTest;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by wang on 07/04/17.
 */

public class RxInterval {
    private static final String TAG = "RxInterval";

    /**
     * 每隔100毫秒执行一次
     */
    public static void intervalTest1() {
        Flowable.interval(100, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()//如果事件过多的情况下会丢弃一部分
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: " + aLong);
                    }
                });
    }

    /**
     * 同一个事件会重复发送
     */
    public static void repeatTest() {
        Flowable.just(1).repeat(5)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 延迟发射某个事件
     */
    public static void timerTest() {
        Observable.just(4).timer(5, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: " + aLong);
                    }
                });
    }
}
