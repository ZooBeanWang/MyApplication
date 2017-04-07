package myproject.com.myapp.RxJavaTest;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by wang on 06/04/17.
 */

public class RxFlowable {
    private static final String TAG = "RxFlowable";

    /**
     * 在Flowable里默认有一个大小为128的水缸, 当上下游工作在不同的线程中时, 上游就会先把事件发送到这个水缸中,
     * 因此, 下游虽然没有调用request, 但是上游在水缸中保存着这些事件, 只有当下游调用request时, 才从水缸里取出事件发给下游.
     */
    public static void flowableBaseUse() {
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onNext(5);
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR);//增加了一个参数这种方式会在出现上下游流速不均衡的时候直接抛出一个异常,这个异常就是著名的MissingBackpressureException
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {//调用Subscription.cancel()也可以切断水管
                Log.d(TAG, "onSubscribe: " + s);
                s.request(Long.MAX_VALUE);//request当做是一种能力, 当成下游处理事件的能力
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: " + t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
        flowable.subscribe(subscriber);
    }
}
