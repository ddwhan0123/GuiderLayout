# GuiderLayout

半透明视图引导页<br>

###作者：王亟亟

###blog:http://blog.csdn.net/ddwhan0123

#### 效果图
<img src="https://github.com/ddwhan0123/GuiderLayout/blob/master/Screen/one.png?raw=true" width="30%"/><br>
<br>
<img src="https://github.com/ddwhan0123/GuiderLayout/blob/master/Screen/two.png?raw=true" width="30%"/><br>
<br>
###如何使用?<br>
布局部分
```Java
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/bg"
    tools:context="guiderlayout.sample.MainActivity">

    <Button
        android:id="@+id/button1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="90dp"
        android:layout_marginTop="90dp"
        android:background="@null"
        android:text="我是一段文字"
        android:textColor="#ffffff"
        android:textSize="15sp" />

    <Button
        android:id="@+id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="200dp"
        android:layout_marginTop="300dp"
        android:background="#00aaaa"
        android:text="我是一个按钮"
        android:textColor="#ffffff" />

    <sample.guiderlayout.guiderlayoutlib.widget.GuiderLayout
        android:id="@+id/view_root"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#66000000"
        android:visibility="invisible">

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:tag="common,below"
            android:visibility="invisible">


             <!-- 一些子控件-->


        </RelativeLayout>

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:tag="end,below"
            android:visibility="invisible">

          <!-- 一些子控件-->
        </RelativeLayout>

    </sample.guiderlayout.guiderlayoutlib.widget.GuiderLayout>
</FrameLayout>

```

java代码部分
<br>
```Java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    GuiderLayout guiderLayout;
    Button button1;
    Button button2;

    public static boolean FLAG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FLAG) {
            guiderLayout.showGuider(button1, "common", GuiderLayout.CLTP_CIRCLE);
        }
    }

    private void init() {
        guiderLayout = (GuiderLayout) findViewById(R.id.view_root);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
    }

    private void setListener() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        guiderLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                guiderLayout.showGuider(button2, "end", GuiderLayout.CLTP_CIRCLE);
                break;
            case R.id.button2:
                guiderLayout.showNoGuide();
                guiderLayout.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        guiderLayout = null;
    }
}

```

-----------

##docs:http://blog.csdn.net/ddwhan0123/article/details/52213063
