package guiderlayout.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import sample.guiderlayout.guiderlayoutlib.widget.GuiderLayout;


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
