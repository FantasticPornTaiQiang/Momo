package com.example.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.fragment.UnselectedWordsFragment;
import com.example.momo.R;
import com.example.momo.WordListActivity;

import java.util.ArrayList;
import java.util.List;

public class UnselectedWordExpandableListAdapter extends BaseExpandableListAdapter {

    private List<String> wordList;
    private List<Boolean> wordIsChecked;
    private List<List<String>> wordsList;//wordList整理成wordsList
    private List<List<Boolean>> wordsIsCheckedTemp;//与wordsList对应，wordIsChecked整理成wordsIsCheckedTemp
    public List<String> nowWordList = new ArrayList<>();


    //返回分好类的单词
    public List<List<String>> getWordsList(){
        List<List<String>> wordsList = new ArrayList<>();
        for(int i = 0; i<wordList.size()/40;i++){
            List<String> temp = new ArrayList<>();
            for(int j=0;j<40;j++){
                if(!wordIsChecked.get(j+i*40)){
                    temp.add(wordList.get(i*40+j));
                }
            }
            wordsList.add(temp);
        }
        List<String> temp = new ArrayList<>();
        for(int i=0;i<wordList.size()%40;i++){
            if(!wordIsChecked.get((wordList.size()/40)*40+i)){
                temp.add(wordList.get((wordList.size()/40)*40+i));
            }
        }
        wordsList.add(temp);
        return wordsList;
    }

    //返回已选
    public List<String> getNowWordList(){
        return nowWordList;
    }

    //返回分好类的bool
    private List<List<Boolean>> getWordsIsCheckedTemp(){
        List<List<Boolean>> wordsIsChecked = new ArrayList<>();
        for(int i = 0; i<wordList.size()/40;i++){
            List<Boolean> temp = new ArrayList<>();
            for(int j=0;j<40;j++){
                if(!wordIsChecked.get(j+i*40)){
                    temp.add(wordIsChecked.get(i*40+j));
                }
            }
            wordsIsChecked.add(temp);
        }
        List<Boolean> temp = new ArrayList<>();
        for(int i=0;i<wordList.size()%40;i++){
            if(!wordIsChecked.get((wordList.size()/40)*40+i)){
                temp.add(wordIsChecked.get((wordList.size()/40)*40+i));
            }
        }
        wordsIsChecked.add(temp);
        return wordsIsChecked;
    }

    //构造函数
    public UnselectedWordExpandableListAdapter(List<String> wordList, List<Boolean> wordIsChecked){
        this.wordList = wordList;
        this.wordIsChecked = wordIsChecked;
        this.wordsList = getWordsList();
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
        UnselectedWordExpandableListAdapter.GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_parent_item,parent,false);
            groupViewHolder = new UnselectedWordExpandableListAdapter.GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.word_parent_text_view);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (UnselectedWordExpandableListAdapter.GroupViewHolder)convertView.getTag();
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
        final UnselectedWordExpandableListAdapter.ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_child_item,parent,false);
            childViewHolder = new UnselectedWordExpandableListAdapter.ChildViewHolder();
            childViewHolder.tvTitle = convertView.findViewById(R.id.unselected_word_text_view);
            childViewHolder.checkBox = convertView.findViewById(R.id.unselected_word_check_box);
            convertView.setTag(childViewHolder);
        }else {
            childViewHolder = (UnselectedWordExpandableListAdapter.ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(wordsList.get(groupPosition).get(childPosition));
        //设置CheckBox
        childViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_checked_light);
                    childViewHolder.checkBox.setChecked(true);
                    wordsIsCheckedTemp.get(groupPosition).set(childPosition,true);
                    nowWordList.add(wordsList.get(groupPosition).get(childPosition));
                    if(!WordListActivity.wordSelectedTemp.contains(wordsList.get(groupPosition).get(childPosition)))
                    WordListActivity.wordSelectedTemp.add(wordsList.get(groupPosition).get(childPosition));
                }
                else {
                    childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_uncheck_light);
                    childViewHolder.checkBox.setChecked(false);
                    wordsIsCheckedTemp.get(groupPosition).set(childPosition,false);
                    for(int i = 0;i<nowWordList.size();i++){
                        if(nowWordList.get(i) == wordsList.get(groupPosition).get(childPosition)){
                            nowWordList.remove(i);
                        }
                    }
                    for(int i = 0;i<WordListActivity.wordSelectedTemp.size();i++){
                        if(wordsList.get(groupPosition).get(childPosition).equals(WordListActivity.wordSelectedTemp.get(i))){
                            WordListActivity.wordSelectedTemp.remove(i);
                        }
                    }
                }
            }
        });

        if(wordsIsCheckedTemp.get(groupPosition).get(childPosition)){
            childViewHolder.checkBox.setChecked(true);
            childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_checked_light);
        } else {
            childViewHolder.checkBox.setChecked(false);
            childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_uncheck_light);
        }

        //解决Fragment3滑到Fragment1选中的自动没了的问题，记得要在Activity点确定后删掉wordSelectedTemp里的东西
        for(int i = 0; i< WordListActivity.wordSelectedTemp.size(); i++){
            if(wordsList.get(groupPosition).get(childPosition).equals(WordListActivity.wordSelectedTemp.get(i))){
                childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_checked_light);
                wordsIsCheckedTemp.get(groupPosition).set(childPosition,true);
                childViewHolder.checkBox.setChecked(true);
            }
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
