package com.example.momo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fragment.AllWordsFragment;
import com.example.fragment.SelectedWordsFragment;
import com.example.fragment.UnselectedWordsFragment;
import com.example.obj.Book;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordListActivity extends AppCompatActivity {

    public List<Book> bookList = new ArrayList();//书列表
    public List<String> wordList = new ArrayList<>();//单词列表，500词
    public List<Boolean> wordIsChecked = new ArrayList<>();//单词是否被选，与wordList对应
    public static List<String> wordSelectedTemp = new ArrayList<>();

    private List<Fragment> fragmentList = new ArrayList<>();
    private UnselectedWordsFragment unselectedWordsFragment;
    private SelectedWordsFragment selectedWordsFragment;
    private AllWordsFragment allWordsFragment;
    private ViewPager viewPager;
    //弹出对话框
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    int count;//已选词的数目

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordlist_layout);
        //初始化信息
        Intent intent = getIntent();
        String bookUrl = intent.getStringExtra("book_url");

        int sectionIndex = intent.getIntExtra("section_index",0);
        int bookIndex = intent.getIntExtra("book_index",0);
        initBook();
        //在线获取单词列表
        downloadWord(bookUrl,bookIndex,sectionIndex);

        //初始化顶部布局
        initView(bookIndex,sectionIndex);

        //回退按钮
        ImageButton backToBookImageButton = findViewById(R.id.back_to_book_list_btn);
        backToBookImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //初始化顶部布局
    private void initView(int bookIndex,int sectionIndex){
        TextView bookTitleTextView = findViewById(R.id.book_title_text_view);
        bookTitleTextView.setText(bookList.get(bookIndex).sectionList.get(sectionIndex));
        RadioButton unselButton = findViewById(R.id.unsel_btn);
        RadioButton selButton = findViewById(R.id.sel_btn);
        RadioButton allButton = findViewById(R.id.all_btn);

        unselButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0,true);
            }
        });
        selButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1,true);
            }
        });
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2,true);
            }
        });
    }

    //发送Http请求，下载词汇
    private void downloadWord(final String url,final int bookIndex,final int sectionIndex){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData,bookIndex,sectionIndex);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final String responseData, final int bookIndex,final int sectionIndex)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //初始化单词列表
                initWords(responseData,bookIndex,sectionIndex);
                //初始化碎片
                initFragment();
                //ViewPager配置(初始化、设置监听)
                viewPager = findViewById(R.id.sel_view_pager);
                viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }
                    //当滑动图片时获取图片下标，然后通过图片下标来设置选中那个按钮
                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                RadioButton unselectedBtn = findViewById(R.id.unsel_btn);
                RadioButton selectedBtn = findViewById(R.id.sel_btn);
                RadioButton allBtn = findViewById(R.id.all_btn);
                allBtn.setText(wordList.size()+"\n全部");
                selectedBtn.setText(count+"\n已选");
                unselectedBtn.setText((wordList.size()-count)+"\n未选");

                //确认选词
                Button confirmWords = findViewById(R.id.confirm_words);
                confirmWords.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //归并unselected
                        final List<String> nowWordList = unselectedWordsFragment.unselectedWordExpandableListAdapter.getNowWordList();
                        final SharedPreferences.Editor editor = getSharedPreferences("SelectedWordsData", MODE_PRIVATE).edit();
                        for(int i=0;i<nowWordList.size();i++){
                            editor.putString(nowWordList.get(i),nowWordList.get(i));
                        }

                        //归并all
                        List<String> nowAllWordList = new ArrayList<>();
                        if(allWordsFragment.isConstructed){
                            nowAllWordList = allWordsFragment.allWordExpandableListAdapter.getNowAllWordList();
                            for(int j=0;j<nowAllWordList.size();j++){
                                    editor.putString(nowAllWordList.get(j),nowAllWordList.get(j));
                            }
                        }

                        for(int i=0;i<nowAllWordList.size();i++){
                            nowWordList.add(nowAllWordList.get(i));
                        }
                        final List<String> nowNowNOWWordList = new ArrayList(new HashSet(nowWordList));

                        final List<Boolean> isCancel = new ArrayList<>();
                        isCancel.add(false);

                        //弹出对话框
                        if(nowNowNOWWordList.size() == 0){
                            Toast.makeText(WordListActivity.this,"没有选择任何单词",Toast.LENGTH_SHORT).show();
                        } else {
                            alertDialog = null;
                            builder = new AlertDialog.Builder(WordListActivity.this);
                            alertDialog = builder.setMessage("确认把" + nowNowNOWWordList.size() + "个单词加入学习规划吗？")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            isCancel.set(0,true);
                                            alertDialog.cancel();
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.apply();
                                            //跳转界面
                                            Intent intent = new Intent(WordListActivity.this,HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    }).create();
                            alertDialog.show();

                            if(isCancel.get(0)){
                                nowAllWordList.clear();
                                nowWordList.clear();
                                nowNowNOWWordList.clear();
                            }
                        }

                    }
                });

            }
        });
    }

    private void initWords(String responseData,final int bookIndex,int sectionIndex){
        String[] words = responseData.split("\\r?\\n");

        if(sectionIndex < bookList.get(bookIndex).sectionList.size()-1){
            for(int i=0;i<500;i++){
                wordList.add(words[sectionIndex*500+i]);
                wordIsChecked.add(false);
            }
        }
        else{
            for(int i=0;i<words.length%500;i++){
                wordList.add(words[sectionIndex*500+i]);
                wordIsChecked.add(false);
            }
        }
    }

    private void initBook(){
        /*500词一个组*/
        Book book1; Book book2; Book book3; Book book4;
        Book book5; Book book6; Book book7; Book book8; Book book9;

        book1 = new Book("中考英语词汇",R.drawable.selbook_icon_junior_high,"http://49.234.190.181/junior.html");
        book2 = new Book("高考英语词汇",R.drawable.selbook_icon_high,"http://49.234.190.181/gao.html");
        book3 = new Book("大学四级英语词汇",R.drawable.selbook_icon_u4,"http://49.234.190.181/u4.html");
        book4 = new Book("大学英语六级词汇",R.drawable.selbook_icon_u6,"http://49.234.190.181/u6.html");
        book5 = new Book("托福词汇",R.drawable.selbook_icon_toefl,"http://49.234.190.181/toefl.html");
        book6 = new Book("雅思词汇",R.drawable.selbook_icon_ielts,"http://49.234.190.181/ielts.html");
        book7 = new Book("考研词汇",R.drawable.selbook_icon_university,"http://49.234.190.181/yan.html");
        book8 = new Book("专四词汇",R.drawable.selbook_icon_u4p,"http://49.234.190.181/u4p.html");
        book9 = new Book("专八词汇",R.drawable.selbook_icon_u8p,"http://49.234.190.181/u8p.html");

        book1.sectionList = new ArrayList<>();book2.sectionList = new ArrayList<>();book3.sectionList = new ArrayList<>();book4.sectionList = new ArrayList<>();
        book5.sectionList = new ArrayList<>();book6.sectionList = new ArrayList<>();book7.sectionList = new ArrayList<>();book8.sectionList = new ArrayList<>();book9.sectionList = new ArrayList<>();

        book1.sectionList.add("中考英语词汇1");book1.sectionList.add("中考英语词汇2");book1.sectionList.add("中考英语词汇3");book1.sectionList.add("中考英语词汇4");book1.sectionList.add("中考英语词汇5");
        book2.sectionList.add("高考英语词汇1");book2.sectionList.add("高考英语词汇2");book2.sectionList.add("高考英语词汇3");book2.sectionList.add("高考英语词汇4");book2.sectionList.add("高考英语词汇5");
        book2.sectionList.add("高考英语词汇6");book2.sectionList.add("高考英语词汇7");book2.sectionList.add("高考英语词汇8");book2.sectionList.add("高考英语词汇9");
        book3.sectionList.add("大学四级英语词汇1");book3.sectionList.add("大学四级英语词汇2");book3.sectionList.add("大学四级英语词汇3");book3.sectionList.add("大学四级英语词汇4");
        book3.sectionList.add("大学四级英语词汇5");book3.sectionList.add("大学四级英语词汇6");book3.sectionList.add("大学四级英语词汇7");
        book4.sectionList.add("大学六级英语词汇1");book4.sectionList.add("大学六级英语词汇2");book4.sectionList.add("大学六级英语词汇3");book4.sectionList.add("大学六级英语词汇4");
        book4.sectionList.add("大学六级英语词汇5");book4.sectionList.add("大学六级英语词汇6");book4.sectionList.add("大学六级英语词汇7");
        book5.sectionList.add("托福词汇1");book5.sectionList.add("托福词汇2");book5.sectionList.add("托福词汇3");book5.sectionList.add("托福词汇4");book5.sectionList.add("托福词汇5");
        book5.sectionList.add("托福词汇6");book5.sectionList.add("托福词汇7");book5.sectionList.add("托福词汇8");book5.sectionList.add("托福词汇9");book5.sectionList.add("托福词汇10");
        book6.sectionList.add("雅思词汇1");book6.sectionList.add("雅思词汇2");book6.sectionList.add("雅思词汇3");book6.sectionList.add("雅思词汇4");
        book6.sectionList.add("雅思词汇5");book6.sectionList.add("雅思词汇6");book6.sectionList.add("雅思词汇7");
        book7.sectionList.add("考研词汇1");book7.sectionList.add("考研词汇2");book7.sectionList.add("考研词汇3");book7.sectionList.add("考研词汇4");book7.sectionList.add("考研词汇5");
        book7.sectionList.add("考研词汇6");book7.sectionList.add("考研词汇7");book7.sectionList.add("考研词汇8");book7.sectionList.add("考研词汇9");
        book7.sectionList.add("考研词汇10");book7.sectionList.add("考研词汇11");book7.sectionList.add("考研词汇12");book7.sectionList.add("考研词汇13");
        book8.sectionList.add("专四词汇1");book8.sectionList.add("专四词汇2");book8.sectionList.add("专四词汇3");book8.sectionList.add("专四词汇4");
        book8.sectionList.add("专四词汇5");book8.sectionList.add("专四词汇6");book8.sectionList.add("专四词汇7");book8.sectionList.add("专四词汇8");
        book8.sectionList.add("专四词汇9");book8.sectionList.add("专四词汇10");book8.sectionList.add("专四词汇11");
        book9.sectionList.add("专八词汇1");book9.sectionList.add("专八词汇2");book9.sectionList.add("专八词汇3");
        book9.sectionList.add("专八词汇4");book9.sectionList.add("专八词汇5");book9.sectionList.add("专八词汇6");

        bookList.add(book1);bookList.add(book2);bookList.add(book3);bookList.add(book4);
        bookList.add(book5);bookList.add(book6);bookList.add(book7);bookList.add(book8);bookList.add(book9);
    }

    private int upDateWordList(){
        int count = 0;
        SharedPreferences sharedPreferences = getSharedPreferences("SelectedWordsData",MODE_PRIVATE);
        for(int i=0;i<wordList.size();i++){
            if(sharedPreferences.contains(wordList.get(i))){
                wordIsChecked.set(i,true);
                count++;
            }
        }
        return count;
    }

    private void initFragment(){
        allWordsFragment = new AllWordsFragment(wordList,wordIsChecked);
        count = upDateWordList();
        unselectedWordsFragment = new UnselectedWordsFragment(wordList,wordIsChecked);
        selectedWordsFragment = new SelectedWordsFragment(wordList,wordIsChecked);
        fragmentList.add(unselectedWordsFragment);
        fragmentList.add(selectedWordsFragment);
        fragmentList.add(allWordsFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    //ViewPager适配器
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }





//按行读取txt
    /*public List<String> Txt() {
        //将读出来的一行行数据使用List存储
        String filePath = "/data/data/com.example.momo/files/gaokao.txt";

        List newList = new ArrayList<String>();
        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                        newList.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {
                Toast.makeText(getApplicationContext(), "can not find file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }*/

}
