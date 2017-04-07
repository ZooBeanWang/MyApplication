package myproject.com.myapp.RxJavaTest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import myproject.com.myapp.R;

public class RxFlowableActivity extends AppCompatActivity {
    private static final String TAG = "RxFlowableActivity";
    private Button btn_start;
    private Button btn_request;
    private Subscription subscription;
    private Flowable<Integer> flowable = null;
    private Subscriber<Integer> subscriber = null;

    public static void startRxFlowableActivity(Context context) {
        Intent intent = new Intent(context, RxFlowableActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_flowable);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_request = (Button) findViewById(R.id.btn_request);
        init1();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
            }
        });

    }

    private void init1() {
        flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 127; i++) {
                    Log.d(TAG, "subscribe: " + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io());
        subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription.request(1);
            }
        });
    }

    /**
     * 可以存储的事件没有大小限制，如果下游不处理还是可能会抛OOM 的
     */
    private void init2() {
        flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    Log.d(TAG, "subscribe: " + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io());
        subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription.request(128);
            }
        });
    }

    //Drop就是直接把存不下的事件丢弃,Latest就是只保留最新的事件
    private void init3() {
        flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    Log.d(TAG, "subscribe: " + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io());
        subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription.request(128);
            }
        });
    }

    private void init4() {
        flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    Log.d(TAG, "subscribe: " + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io());
        subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription.request(128);
            }
        });
    }
}
