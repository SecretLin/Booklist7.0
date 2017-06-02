package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.R;

/**
 * Created by Secret on 2016/12/11.
 */

public class ShowBigPic extends Activity{
    private String url;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbigpic);
        iv = (ImageView) findViewById(R.id.bigpic);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        url = getIntent().getStringExtra("url");

        Glide.with(this).load(url).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
