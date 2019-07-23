package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.adapter.UnselectedWordExpandableListAdapter;
import com.example.momo.GetMeaningActivity;
import com.example.momo.R;

import java.util.List;

public class UnselectedWordsFragment extends Fragment {

    private ExpandableListView wordExpandableList;
    private List<String> wordList;
    private List<Boolean> wordIsChecked;
    public com.example.adapter.UnselectedWordExpandableListAdapter unselectedWordExpandableListAdapter;

    public UnselectedWordsFragment(List<String> wordList,List<Boolean> wordIsChecked) {
        // Required empty public constructor
        this.wordList = wordList;
        this.wordIsChecked = wordIsChecked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_words, container, false);
        //设置单词列表ExpandableListView
        wordExpandableList = view.findViewById(R.id.word_expandable_list_view);
        unselectedWordExpandableListAdapter = new UnselectedWordExpandableListAdapter(wordList,wordIsChecked);
        wordExpandableList.setAdapter(unselectedWordExpandableListAdapter);

        //设置分组的监听
        wordExpandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        //设置子项布局监听
        wordExpandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<List<String>> wordsList = unselectedWordExpandableListAdapter.getWordsList();
                Intent intent1 = new Intent(getContext(), GetMeaningActivity.class);
                intent1.putExtra("word",wordsList.get(groupPosition).get(childPosition));
                startActivity(intent1);
                return true;
            }
        });

        return view;
    }

}
