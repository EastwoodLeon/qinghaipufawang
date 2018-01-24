package com.pxkeji.qinghaipufawang;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pxkeji.qinghaipufawang.data.MySection;
import com.pxkeji.qinghaipufawang.data.Service3Item;
import com.pxkeji.qinghaipufawang.data.adapter.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class Service3Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<MySection> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service3);

        initActionBar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mData = getSampleData();

        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);
        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MySection mySection = mData.get(position);
                if (mySection.isHeader) {
                    //Toast.makeText(Service3Activity.this, mySection.header, Toast.LENGTH_SHORT).show();
                }else {
                    Service3Item item = mySection.t;
                    Intent intent = new Intent(Service3Activity.this, BrowserActivity.class);
                    intent.putExtra(BrowserActivity.TITLE, item.getName());
                    intent.putExtra(BrowserActivity.URL, item.getUrl());
                    startActivity(intent);
                }

            }
        });

        mRecyclerView.setAdapter(sectionAdapter);
    }

    private List<MySection> getSampleData() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true, "省政府办公厅", false));
        list.add(new MySection(new Service3Item("青海省人民政府办公厅", "http://xxgk.qh.gov.cn/html/42/139235.html")));

        list.add(new MySection(true, "组成部门", false));
        list.add(new MySection(new Service3Item("省发展改革委", "http://xxgk.qh.gov.cn/html/44/142167.html")));
        list.add(new MySection(new Service3Item("省经济信息化委", "http://xxgk.qh.gov.cn/html/44/140195.html")));
        list.add(new MySection(new Service3Item("省教育厅", "http://xxgk.qh.gov.cn/html/44/39378.html")));
        list.add(new MySection(new Service3Item("省科技厅", "http://xxgk.qh.gov.cn/html/44/290690.html")));
        list.add(new MySection(new Service3Item("省民宗委", "http://xxgk.qh.gov.cn/html/44/139981.html")));
        list.add(new MySection(new Service3Item("省公安厅", "http://xxgk.qh.gov.cn/html/44/129950.html")));
        list.add(new MySection(new Service3Item("省安全厅", "http://xxgk.qh.gov.cn/html/44/129949.html")));
        list.add(new MySection(new Service3Item("省监察厅", "http://xxgk.qh.gov.cn/html/44/39403.html")));
        list.add(new MySection(new Service3Item("省民政厅", "http://xxgk.qh.gov.cn/html/44/140493.html")));
        list.add(new MySection(new Service3Item("省司法厅", "http://xxgk.qh.gov.cn/html/44/39381.html")));
        list.add(new MySection(new Service3Item("省财政厅", "http://xxgk.qh.gov.cn/html/44/140192.html")));
        list.add(new MySection(new Service3Item("省水利厅", "http://xxgk.qh.gov.cn/html/44/129946.html")));
        list.add(new MySection(new Service3Item("省国土资源厅", "http://xxgk.qh.gov.cn/html/44/141797.html")));
        list.add(new MySection(new Service3Item("省环境保护厅", "http://xxgk.qh.gov.cn/html/44/141751.html")));
        list.add(new MySection(new Service3Item("省农牧厅", "http://xxgk.qh.gov.cn/html/44/140365.html")));

        list.add(new MySection(true, "直属机构", false));
        list.add(new MySection(new Service3Item("省地税局", "http://xxgk.qh.gov.cn/html/666/246933.html")));
        list.add(new MySection(new Service3Item("省广电局", "http://xxgk.qh.gov.cn/html/666/246938.html")));
        list.add(new MySection(new Service3Item("省统计局", "http://xxgk.qh.gov.cn/html/666/246940.html")));
        list.add(new MySection(new Service3Item("省工商局", "http://xxgk.qh.gov.cn/html/666/246943.html")));
        list.add(new MySection(new Service3Item("省质监局", "http://xxgk.qh.gov.cn/html/666/246944.html")));
        list.add(new MySection(new Service3Item("省体育局", "http://xxgk.qh.gov.cn/html/666/246951.html")));
        list.add(new MySection(new Service3Item("省旅游局", "http://xxgk.qh.gov.cn/html/666/246953.html")));
        list.add(new MySection(new Service3Item("省扶贫局", "http://xxgk.qh.gov.cn/html/666/246954.html")));
        list.add(new MySection(new Service3Item("省食品药品监管局", "http://xxgk.qh.gov.cn/html/666/39390.html")));
        list.add(new MySection(new Service3Item("省安全监管局", "http://xxgk.qh.gov.cn/html/666/246948.html")));

        list.add(new MySection(true, "部门管理机构", false));
        list.add(new MySection(new Service3Item("省金融办", "http://xxgk.qh.gov.cn/html/667/246955.html")));
        list.add(new MySection(new Service3Item("省信访局", "http://xxgk.qh.gov.cn/html/667/141875.html")));
        list.add(new MySection(new Service3Item("省粮食局", "http://xxgk.qh.gov.cn/html/667/142850.html")));
        list.add(new MySection(new Service3Item("省能源局", "http://xxgk.qh.gov.cn/html/667/276380.html")));
        list.add(new MySection(new Service3Item("省监狱局", "http://xxgk.qh.gov.cn/html/667/129941.html")));
        list.add(new MySection(new Service3Item("省公务员局", "http://xxgk.qh.gov.cn/html/667/129939.html")));
        list.add(new MySection(new Service3Item("省测绘局", "http://xxgk.qh.gov.cn/html/667/245471.html")));

        list.add(new MySection(true, "派出机构", false));
        list.add(new MySection(new Service3Item("青海湖景区管理局", "http://xxgk.qh.gov.cn/html/668/245506.html")));

        list.add(new MySection(true, "其他机构", false));
        list.add(new MySection(new Service3Item("省人防办", "http://xxgk.qh.gov.cn/html/669/245507.html")));
        list.add(new MySection(new Service3Item("省档案局", "http://xxgk.qh.gov.cn/html/669/245491.html")));
        list.add(new MySection(new Service3Item("省新闻办", "http://xxgk.qh.gov.cn/html/669/245487.html")));

        list.add(new MySection(true, "中央驻青单位", false));
        list.add(new MySection(new Service3Item("省国税局", "http://xxgk.qh.gov.cn/html/674/245509.html")));
        list.add(new MySection(new Service3Item("青海调查总队", "http://xxgk.qh.gov.cn/html/674/245510.html")));
        list.add(new MySection(new Service3Item("省气象局", "http://xxgk.qh.gov.cn/html/674/245511.html")));
        list.add(new MySection(new Service3Item("省地震局", "http://xxgk.qh.gov.cn/html/674/245513.html")));
        list.add(new MySection(new Service3Item("青海储备物资管理局", "http://xxgk.qh.gov.cn/html/674/245517.html")));
        list.add(new MySection(new Service3Item("西宁海关", "http://xxgk.qh.gov.cn/html/674/245524.html")));
        list.add(new MySection(new Service3Item("省邮政管理局", "http://xxgk.qh.gov.cn/html/674/246930.html")));
        list.add(new MySection(new Service3Item("青海出入境检验检疫局", "http://xxgk.qh.gov.cn/html/674/245522.html")));
        list.add(new MySection(new Service3Item("青海省烟草专卖局", "http://xxgk.qh.gov.cn/html/674/245520.html")));

        return list;
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
