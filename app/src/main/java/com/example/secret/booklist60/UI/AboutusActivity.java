package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.secret.booklist60.R;
/*
    “关于我们”的界面
 */

public class AboutusActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
