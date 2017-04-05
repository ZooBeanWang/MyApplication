package myproject.com.myapp.RXJavaTest;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wang on 01/04/17.
 */

public class RxBaseUse {
    private static final String TAG = "RxBaseUse";

    /**
     * 测试1：最简单的使用方法
     */
    public static void baseUse1() {
        Log.d(TAG, "test1: start");
        //1.创建被观察者
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();

            }
        });
        //2.创建观察者
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
        //3.订阅--连接到一起
        observable.subscribe(observer);
        Log.d(TAG, "test1: end");
    }

    /**
     * 测试2：一部到位的复杂写法
     */
    public static void baseUse2() {
        Log.d(TAG, "test2: start");
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
        Log.d(TAG, "test2: end");
    }

    /**
     * 测试3：dispose调用后 observable还会继续发送事件，但是observer不会接受事件了
     */
    public static void disposetest() {
        Log.d(TAG, "disposetest: start");
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emitter: 1");
                emitter.onNext(1);
                Log.d(TAG, "emitter: 2");
                emitter.onNext(2);
                Log.d(TAG, "emitter: 3");
                emitter.onNext(3);
                Log.d(TAG, "emitter: complete");
                emitter.onComplete();
                Log.d(TAG, "emitter: 4");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext: " + value);
                if (value == 2) {
                    Log.d(TAG, "dispose");
                    disposable.dispose();
                    Log.d(TAG, "isdisposed:" + disposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
        Log.d(TAG, "disposetest: end");
    }

    /**
     * 测试4：只带一个Consumer参数的Observer只关心onNext事件，其他事件不关心
     */
    public static void consumer() {
        Log.d(TAG, "consumer: start");
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emitter: 1");
                emitter.onNext(1);
                Log.d(TAG, "emitter: 2");
                emitter.onNext(2);
                Log.d(TAG, "emitter: 3");
                emitter.onNext(3);
                Log.d(TAG, "emitter: complete");
                emitter.onComplete();
                Log.d(TAG, "emitter: 4");
                emitter.onNext(4);

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: " + integer);
            }
        });
        Log.d(TAG, "consumer: end");
    }


}
