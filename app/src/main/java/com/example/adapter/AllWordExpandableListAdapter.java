package com.example.adapter;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.momo.R;
import com.example.momo.WordListActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AllWordExpandableListAdapter extends BaseExpandableListAdapter {

    private List<String> wordList;
    private List<Boolean> wordIsChecked;
    private List<List<String>> wordsList;
    private List<List<Boolean>> wordsIsCheckedTemp;
    public List<String> nowAllWordList = new ArrayList<>();

    //返回分好类的单词
    public List<List<String>> getWordsList(){
        List<List<String>> wordsList = new ArrayList<>();
        for(int i = 0; i<wordList.size()/40;i++){
            List<String> temp = new ArrayList<>();
            for(int j=0;j<40;j++){
                temp.add(wordList.get(i*40+j));
            }
            wordsList.add(temp);
        }
        List<String> temp = new ArrayList<>();
        for(int i=0;i<wordList.size()%40;i++){
            temp.add(wordList.get((wordList.size()/40)*40+i));
        }
        wordsList.add(temp);
        return wordsList;
    }

    //返回已选
    public List<String> getNowAllWordList(){
        return nowAllWordList;
    }

    //返回分好类的ListBool
    public List<List<Boolean>> getWordsIsCheckedTemp(){
        List<List<Boolean>> wordsIsCheckedTemp = new ArrayList<>();
        for(int i = 0; i<wordIsChecked.size()/40;i++){
            List<Boolean> temp = new ArrayList<>();
            for(int j=0;j<40;j++){
                temp.add(wordIsChecked.get(i*40+j));
            }
            wordsIsCheckedTemp.add(temp);
        }
        List<Boolean> temp = new ArrayList<>();
        for(int i=0;i<wordIsChecked.size()%40;i++){
            temp.add(wordIsChecked.get((wordIsChecked.size()/40)*40+i));
        }
        wordsIsCheckedTemp.add(temp);
        return wordsIsCheckedTemp;
    }

    //构造函数
    public AllWordExpandableListAdapter(List<String> wordList, List<Boolean> wordIsChecked){
        this.wordList = wordList;
        this.wordsList = getWordsList();
        this.wordIsChecked = wordIsChecked;
        this.wordsIsCheckedTemp = getWordsIsCheckedTemp();
    }

    @Override
    //获取分组的个数
    public int getGroupCount() {
        return wordsList.size();
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return wordsList.get(groupPosition).size();
    }

    //获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return wordsList.get(groupPosition);
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return wordsList.get(groupPosition).get(childPosition);
    }
    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }
    /**
     *
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     */
    //获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        AllWordExpandableListAdapter.GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_parent_item,parent,false);
            groupViewHolder = new AllWordExpandableListAdapter.GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.word_parent_text_view);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (AllWordExpandableListAdapter.GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.tvTitle.setText("Word List "+(groupPosition+1));
        return convertView;
    }
    /**
     *
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final AllWordExpandableListAdapter.ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_child_item,parent,false);
            childViewHolder = new AllWordExpandableListAdapter.ChildViewHolder();
            childViewHolder.tvTitle = convertView.findViewById(R.id.unselected_word_text_view);
            childViewHolder.checkBox = convertView.findViewById(R.id.unselected_word_check_box);
            convertView.setTag(childViewHolder);
        }else {
            childViewHolder = (AllWordExpandableListAdapter.ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(wordsList.get(groupPosition).get(childPosition));

        //设置CheckBox
        childViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(!wordIsChecked.get(groupPosition*40+childPosition)){
                    if(isChecked){
                        childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_checked_light);
                        wordsIsCheckedTemp.get(groupPosition).set(childPosition,true);
                        nowAllWordList.add(wordsList.get(groupPosition).get(childPosition));
                        if(!WordListActivity.wordSelectedTemp.contains(wordsList.get(groupPosition).get(childPosition)))
                            WordListActivity.wordSelectedTemp.add(wordsList.get(groupPosition).get(childPosition));
                    }
                    else {
                        childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_uncheck_light);
                        wordsIsCheckedTemp.get(groupPosition).set(childPosition,false);
                        if(nowAllWordList.contains(wordsList.get(groupPosition).get(childPosition))){
                            nowAllWordList.remove(wordsList.get(groupPosition).get(childPosition));
                        }
                        if(WordListActivity.wordSelectedTemp.contains(wordsList.get(groupPosition).get(childPosition))){
                            WordListActivity.wordSelectedTemp.remove(wordsList.get(groupPosition).get(childPosition));
                        }
                    }
                }
            }
        });

        //wordIsChecked只在初始化的时候加载一次
        if(wordIsChecked.get(groupPosition*40+childPosition)){
            childViewHolder.checkBox.setEnabled(false);
            childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_checked_gray);
        } else {
            childViewHolder.checkBox.setEnabled(true);
            childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_uncheck_light);
        }

        if(wordsIsCheckedTemp.get(groupPosition).get(childPosition)){
            if(!wordIsChecked.get(groupPosition*40+childPosition)){
                childViewHolder.checkBox.setChecked(true);
                childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_checked_light);
            }
        } else {
            if(!wordIsChecked.get(groupPosition*40+childPosition)){
                childViewHolder.checkBox.setChecked(false);
                childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_uncheck_light);
            }
        }

        //解决Fragment3滑到Fragment1选中的自动没了的问题，记得要在Activity点确定后删掉wordSelectedTemp里的东西
        if(WordListActivity.wordSelectedTemp.contains(wordsList.get(groupPosition).get(childPosition))&&!wordIsChecked.get(groupPosition*40+childPosition)){
            childViewHolder.checkBox.setChecked(true);
            childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_checked_light);
            wordsIsCheckedTemp.get(groupPosition).set(childPosition,true);
        }


        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }

    static class ChildViewHolder {
        TextView tvTitle;
        CheckBox checkBox;
    }
}
