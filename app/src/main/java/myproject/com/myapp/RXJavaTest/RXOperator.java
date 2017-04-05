package myproject.com.myapp.RXJavaTest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import myproject.com.myapp.RetrofitTest.Api;
import myproject.com.myapp.RetrofitTest.RetrofitProvider;
import myproject.com.myapp.RetrofitTest.entity.LoginRequest;
import myproject.com.myapp.RetrofitTest.entity.LoginResponse;
import myproject.com.myapp.RetrofitTest.entity.RegisterRequest;
import myproject.com.myapp.RetrofitTest.entity.RegisterResponse;

/**
 * Created by wang on 01/04/17.
 */

public class RXOperator {
    private static final String TAG = "RXOperator";

    /**
     * map操作符对上游发送的每一个事件应用一个函数, 使得每一个事件都按照指定的函数去变化.
     */
    public static void mapTest() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(5);
            }
        }).map(new Function<Integer, String>() {//Function接收一个Integer参数对象返回一个String对象
            @Override
            public String apply(Integer integer) throws Exception {
                return "fushiont return:" + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        });
    }

    /**
     * flatmap把上游的一个事件转换成下游的若干个事件，不保证执行的顺序
     */
    public static void flatMapTest() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List list = new ArrayList();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value:" + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        });
    }

    /**
     * concatMap将上游的一个事件转换成下游的若干个事件，保证事件的传递顺序
     */
    public static void concatMapTest() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List list = new ArrayList();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value:" + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        });
    }

    public static void testLogin(final Context context){
        final Api api = RetrofitProvider.get().create(Api.class);
        api.register(new RegisterRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        Log.d(TAG, "accept: dosomethings");
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                        return api.login(new LoginRequest());
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
