package com.example.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.adapter.BookExtendableListViewAdapter;
import com.example.momo.R;
import com.example.momo.WordListActivity;
import com.example.obj.Book;

import java.util.ArrayList;
import java.util.List;

public class SelectWordsFragment extends Fragment {

    private ExpandableListView expandableListView;

    public List<Book> bookList = new ArrayList();

    //构造函数
    public SelectWordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initBook();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_words, container, false);
        expandableListView = view.findViewById(R.id.expend_list);
        expandableListView.setAdapter(new BookExtendableListViewAdapter(bookList));
        //设置分组的监听
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        //设置子项布局监听
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), WordListActivity.class);
                intent.putExtra("book_url",bookList.get(groupPosition).getBookUrl());
                intent.putExtra("section_index",childPosition);
                intent.putExtra("book_index",groupPosition);
                startActivity(intent);
                return true;
            }
        });
        //设置只能打开一个
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = new BookExtendableListViewAdapter(bookList).getGroupCount();
                for(int i = 0;i < count;i++){
                    if (i!=groupPosition){
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });
        return view;
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

}

