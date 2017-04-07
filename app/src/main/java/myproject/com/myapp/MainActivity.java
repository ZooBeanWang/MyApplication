package myproject.com.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import myproject.com.myapp.RetrofitLearn.RetrofitTest;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RetrofitTest.callTestEnqueue();
    }
}
