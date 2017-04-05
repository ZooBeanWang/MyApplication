package myproject.com.myapp.RXJavaTest;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Zip通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件.
 * 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据。
 * Created by wang on 05/04/17.
 */

public class RxZipOperater {
    private static final String TAG = "RxZipOperater";

    /**
     * 在同一个线程中使用zip操作符
     */
    public static void testZip1() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe: 1");
                e.onNext(1);
                Log.d(TAG, "subscribe: 2");
                e.onNext(2);
                Log.d(TAG, "subscribe: 3");
                e.onNext(3);
                Log.d(TAG, "subscribe: 4");
                e.onNext(4);
                Log.d(TAG, "subscribe: 5");
                e.onNext(5);
                Log.d(TAG, "subscribe: Integer complete");
                e.onComplete();

            }
        });
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(TAG, "subscribe: A");
                e.onNext("A");
                Log.d(TAG, "subscribe: B");
                e.onNext("B");
                Log.d(TAG, "subscribe: C");
                e.onNext("C");
                Log.d(TAG, "subscribe: D");
                e.onNext("D");
                Log.d(TAG, "subscribe: String complete");
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + ":" + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    /**
     * 在不同线程中使用zip操作符
     */
    public static void testZip2() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe: 1");
                e.onNext(1);
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: 2");
                e.onNext(2);
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: 3");
                e.onNext(3);
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: 4");
                e.onNext(4);
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: 5");
                e.onNext(5);
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: Integer complete");
                e.onComplete();

            }
        }).subscribeOn(Schedulers.io());
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(TAG, "subscribe: A");
                e.onNext("A");
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: B");
                e.onNext("B");
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: C");
                e.onNext("C");
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: D");
                e.onNext("D");
                Thread.sleep(1000);
                Log.d(TAG, "subscribe: String complete");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + ":" + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    /**
     * 在不同线程发送但是无间隔时也会严格按照顺序执行，只是有的县城可可能连续发送多个，会根据最后一个发送的事件顺序执行
     */
    public static void testZip3() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "subscribe: 1");
                e.onNext(1);
                Log.d(TAG, "subscribe: 2");
                e.onNext(2);
                Log.d(TAG, "subscribe: 3");
                e.onNext(3);
                Log.d(TAG, "subscribe: 4");
                e.onNext(4);
                Log.d(TAG, "subscribe: 5");
                e.onNext(5);
                Log.d(TAG, "subscribe: Integer complete");
                e.onComplete();

            }
        }).subscribeOn(Schedulers.newThread());
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(TAG, "subscribe: A");
                e.onNext("A");
                Log.d(TAG, "subscribe: B");
                e.onNext("B");
                Log.d(TAG, "subscribe: C");
                e.onNext("C");
                Log.d(TAG, "subscribe: D");
                e.onNext("D");
                Log.d(TAG, "subscribe: String complete");
            }
        }).subscribeOn(Schedulers.newThread());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + ":" + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }
}
