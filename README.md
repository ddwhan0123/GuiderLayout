# GuiderLayout

半透明视图引导页<br>

###作者：王亟亟

###blog:http://blog.csdn.net/ddwhan0123

#### 效果图
<img src="https://github.com/ddwhan0123/GuiderLayout/blob/master/Screen/one.png?raw=true" width="30%"/><br>
<br>
<img src="https://github.com/ddwhan0123/GuiderLayout/blob/master/Screen/two.png?raw=true" width="30%"/><br>
<br>
###如何使用?
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
