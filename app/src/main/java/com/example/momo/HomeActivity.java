package com.example.momo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adapter.HomeViewPagerAdapter;
import com.example.fragment.AddFragment;
import com.example.fragment.ReviewAnswerFragment;
import com.example.fragment.ReviewTestFragment;
import com.example.fragment.SelectWordsFragment;
import com.example.fragment.SettingsFragment;
import com.example.fragment.StatisticsFragment;
import com.example.util.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeActivity extends AppCompatActivity{

    //ViewPager
    private ViewPager viewPager;
    public HomeViewPagerAdapter viewPagerAdapter;
    //碎片
    public List<Fragment> fragmentList = new ArrayList<>();
    private AddFragment addFragment = new AddFragment();//0
    private ReviewTestFragment reviewTestFragment;//0
    private SelectWordsFragment selectWordsFragment = new SelectWordsFragment();//1
    private StatisticsFragment statisticsFragment = new StatisticsFragment();//2
    private SettingsFragment settingsFragment = new SettingsFragment();//3
    //按钮
    private Button btnReview;
    private Button btnSelectWords;
    private Button btnStatistics;
    private Button btnSettings;
    //已选单词及位置
    public List<String>checkedWord = new ArrayList<>();
    public int wordPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //实例化按钮
        btnReview = findViewById(R.id.btn_review);
        btnSelectWords = findViewById(R.id.btn_select_words);
        btnStatistics = findViewById(R.id.btn_statistics);
        btnSettings = findViewById(R.id.btn_settings);

        bottomNavigationSelect(0);
        initView();

        viewPager =  findViewById(R.id.viewpager);
        //给ViewPager设置监听事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //当滑动图片时获取图片下标，然后通过图片下标来设置选中那个按钮
            @Override
            public void onPageSelected(int position) {
                bottomNavigationSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        viewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(viewPagerAdapter);

    }

    //改变页面
    public void changeFragment(int i){
        if (i == 0){
            viewPager.setCurrentItem(0);
            bottomNavigationSelect(0);
        }
        if (i == 1) {
            viewPager.setCurrentItem(1);
            bottomNavigationSelect(1);
        }
    }

    //初始化按钮和碎片
    private void initView(){
        //底部导航栏按钮设置监听
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
                bottomNavigationSelect(0);
            }
        });
        btnSelectWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                bottomNavigationSelect(1);
            }
        });
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
                bottomNavigationSelect(2);
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3);
                bottomNavigationSelect(3);
            }
        });

        //将四个碎片添加进碎片列表
        initReviewWord();
        if (checkedWord.isEmpty()) {
            fragmentList.add(addFragment);
        }else {
            reviewTestFragment = new ReviewTestFragment(checkedWord.get(wordPosition));
            fragmentList.add(reviewTestFragment);
        }
        fragmentList.add(selectWordsFragment);
        fragmentList.add(statisticsFragment);
        fragmentList.add(settingsFragment);
    }

    //更新第一个碎片为单词答案碎片
    public void setReviewFragment(Fragment fragment){
        fragmentList.clear();
        fragmentList.add(fragment);
        fragmentList.add(selectWordsFragment);
        fragmentList.add(statisticsFragment);
        fragmentList.add(settingsFragment);
        Log.d("aaa", fragment+"");
        viewPagerAdapter.notifyDataSetChanged();

    }

    //获取保存的指定位置的单词
    public String getWord(){
        return checkedWord.get(wordPosition);
    }

    //根据记忆情况初始化单词列表
    public void initReviewWord(){
//        File file = new File("/data/data/" + getPackageName()+"/shared_prefs","memory_num" + TimeUtil.getCuttentDay());
//        if (file.exists()) {
//            SharedPreferences preferences = getSharedPreferences("memory", MODE_PRIVATE);
//            Map<String, Integer> map = (Map<String, Integer>) preferences.getAll();
//            loadList();
//            sortMap(map);
//        }else {
            loadList();
//        }
    }

    //移动单词
    public void setWordPosition(int wordPosition) {
        this.wordPosition += wordPosition;
        if (this.wordPosition < 0)
            this.wordPosition = 0;
        if (this.wordPosition == checkedWord.size())
            this.wordPosition = checkedWord.size();
    }

    //底部导航栏选中变绿色
    private void bottomNavigationSelect(int index){
        Button btnReview=(Button)findViewById(R.id.btn_review);
        Button btnSelectWords=(Button)findViewById(R.id.btn_select_words);
        Button btnStatistics=(Button)findViewById(R.id.btn_statistics);
        Button btnSettings=(Button)findViewById(R.id.btn_settings);
        switch (index){
            case 0:
                btnReview.setBackgroundResource(R.drawable.tab_review_selected);
                btnSelectWords.setBackgroundResource(R.drawable.tab_book_unselected);
                btnStatistics.setBackgroundResource(R.drawable.tab_graph_unselected);
                btnSettings.setBackgroundResource(R.drawable.tab_settings_unselected);
                ((TextView)findViewById(R.id.btn_review_text)).setTextColor(Color.parseColor("#20B2AA"));
                ((TextView)findViewById(R.id.btn_select_words_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_statistics_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_settings_text)).setTextColor(Color.parseColor("#000000"));
                break;
            case 1:
                btnReview.setBackgroundResource(R.drawable.tab_review_unselected);
                btnSelectWords.setBackgroundResource(R.drawable.tab_book_selected);
                btnStatistics.setBackgroundResource(R.drawable.tab_graph_unselected);
                btnSettings.setBackgroundResource(R.drawable.tab_settings_unselected);
                ((TextView)findViewById(R.id.btn_review_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_select_words_text)).setTextColor(Color.parseColor("#20B2AA"));
                ((TextView)findViewById(R.id.btn_statistics_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_settings_text)).setTextColor(Color.parseColor("#000000"));
                break;
            case 2:
                btnReview.setBackgroundResource(R.drawable.tab_review_unselected);
                btnSelectWords.setBackgroundResource(R.drawable.tab_book_unselected);
                btnStatistics.setBackgroundResource(R.drawable.tab_graph_selected);
                btnSettings.setBackgroundResource(R.drawable.tab_settings_unselected);
                ((TextView)findViewById(R.id.btn_review_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_select_words_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_statistics_text)).setTextColor(Color.parseColor("#20B2AA"));
                ((TextView)findViewById(R.id.btn_settings_text)).setTextColor(Color.parseColor("#000000"));
                break;
            case 3:
                btnReview.setBackgroundResource(R.drawable.tab_review_unselected);
                btnSelectWords.setBackgroundResource(R.drawable.tab_book_unselected);
                btnStatistics.setBackgroundResource(R.drawable.tab_graph_unselected);
                btnSettings.setBackgroundResource(R.drawable.tab_settings_selected);
                ((TextView)findViewById(R.id.btn_review_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_select_words_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_statistics_text)).setTextColor(Color.parseColor("#000000"));
                ((TextView)findViewById(R.id.btn_settings_text)).setTextColor(Color.parseColor("#20B2AA"));
                break;
        }

    }

    //加载选中单词
    public void loadList() {
        SharedPreferences preferences = getSharedPreferences("SelectedWordsData", MODE_PRIVATE);
        Map<String, String> map = (Map<String, String>) preferences.getAll();
        Set<Map.Entry<String, String>> set = map.entrySet();
        List<String> temp = checkedWord;
        checkedWord.clear();
        for (Map.Entry<String,String> me : set){
            if(!temp.contains(me.getValue()))
            checkedWord.add(me.getValue());
        }
    }
    //整理单词
    private void sortMap(Map map) {
        //将map.entrySet()转换成list
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        //通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });
        checkedWord.clear();
        for (Map.Entry<String, Integer> mapping : list) {
            checkedWord.add(mapping.getKey());
        }
    }
    //删除单词
    public void removeWord(String word){
        SharedPreferences preferences = getSharedPreferences("SelectedWordsData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(word);
        editor.apply();
    }


}
