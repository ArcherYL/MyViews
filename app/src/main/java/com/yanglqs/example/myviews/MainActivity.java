package com.yanglqs.example.myviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yanglqs.example.viewslib.FloatImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.floatImg)
    FloatImageView floatImg;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_main, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        floatImg.setOnSingleTouch(new FloatImageView.OnSingleTouchListener() {
            @Override
            public void onSingleTouch(View v) {
                Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
            }
        });
        floatImg.startFreshWelfare();
    }

    private void initView() {

    }
}
