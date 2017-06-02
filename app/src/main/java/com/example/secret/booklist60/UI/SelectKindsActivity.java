package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.secret.booklist60.Adapter.SelectKindsAdapter;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.Fragment.BookFragment;

import java.util.ArrayList;
import java.util.List;

/*
选择分类
 */
public class SelectKindsActivity extends Activity {

    ImageButton btnBack;
    BookFragment fragment = new BookFragment();

    private RecyclerView rv;
    private SelectKindsAdapter adapter;
    private List<String[]> kinds = new ArrayList<>();
    private List<String[]> kinds_more = new ArrayList<>();
    private List<String> kindsTitle = new ArrayList<>();
    private TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_kinds);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("分类");

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv = (RecyclerView) findViewById(R.id.rv);

        initData();

        adapter = new SelectKindsAdapter(this,kinds,kinds_more,kindsTitle);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }

    private void initData() {
        String[] kind_com = {"前端","后端", "Android","IOS","数据库","算法",
                "编程","人工智能","大数据&云计算","更多"};
        String[] kind_com_more = {"测试&运维","信息安全","软件工程","办公软件","通信"
                ,"游戏","产品&运营"};
        String[] kind_design = {"UI/UX","平面设计","工业设计","插画/漫画","影视动画","室内/室外",
                "机械设计","更多"};
        String[] kind_design_more = {"网页设计","交互设计","服装设计"};

        kinds.add(kind_com);
        kinds.add(kind_design);

        kinds_more.add(kind_com_more);
        kinds_more.add(kind_design_more);

        kindsTitle.add("计算机");
        kindsTitle.add("设计");
    }

}
