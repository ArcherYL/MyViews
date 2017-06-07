# MyViews

**app**: demo 展示项目

**viewslib**:所有的自定义view library

----

**下面是所有的自定义View介绍:**

#### FloatImageView####

一个可拖拽并会自动吸附屏幕边缘的ImageView,并且可以开启自动呼吸效果。

使用方法：

XML文件中配置它的初始位置，跟普通ImageView 相同

```java
<com.yanglqs.example.viewslib.FloatImageView
    android:id="@+id/floatImg"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:clickable="true"
    android:src="@drawable/img_fresh_welfare"
    />
```

Java代码中

```java
//设置点击事件
 floatImg.setOnSingleTouch(new FloatImageView.OnSingleTouchListener() {
            @Override
            public void onSingleTouch(View v) {
                Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
            }
        });
//开启呼吸效果
floatImg.startFreshWelfare();
//停止呼吸效果
floatImg.stopFreshWelfare();
```

**特别注意**

- 父容器必须是**RelativeLayout**.


- 控件的移动和边缘吸附都是相对于父容器而言，在设计时已经考虑到了父容器的padding值，也就是说FloatImageView的移动不会超出父容器的范围，并且会受到父容器padding值的约束。





