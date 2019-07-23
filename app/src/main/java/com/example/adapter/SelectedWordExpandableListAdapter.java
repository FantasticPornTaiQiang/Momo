package com.example.adapter;

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

public class SelectedWordExpandableListAdapter extends BaseExpandableListAdapter {

    private List<String> wordList;
    private List<Boolean> wordIsChecked;
    private List<List<String>> wordsList;//wordList整理成wordsList

    //返回分好类的单词
    public List<List<String>> getWordsList(){
        List<List<String>> wordsList = new ArrayList<>();
        for(int i = 0; i<wordList.size()/40;i++){
            List<String> temp = new ArrayList<>();
            for(int j=0;j<40;j++){
                if(wordIsChecked.get(j+i*40)){
                    temp.add(wordList.get(i*40+j));
                }
            }
            wordsList.add(temp);
        }
        List<String> temp = new ArrayList<>();
        for(int i=0;i<wordList.size()%40;i++){
            if(wordIsChecked.get((wordList.size()/40)*40+i)){
                temp.add(wordList.get((wordList.size()/40)*40+i));
            }
        }
        wordsList.add(temp);
        return wordsList;
    }

    //构造函数
    public SelectedWordExpandableListAdapter(List<String> wordList, List<Boolean> wordIsChecked){
        this.wordList = wordList;
        this.wordIsChecked = wordIsChecked;
        this.wordsList = getWordsList();
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
        SelectedWordExpandableListAdapter.GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_parent_item,parent,false);
            groupViewHolder = new SelectedWordExpandableListAdapter.GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.word_parent_text_view);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (SelectedWordExpandableListAdapter.GroupViewHolder)convertView.getTag();
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
        SelectedWordExpandableListAdapter.ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_child_item,parent,false);
            childViewHolder = new SelectedWordExpandableListAdapter.ChildViewHolder();
            childViewHolder.tvTitle = convertView.findViewById(R.id.unselected_word_text_view);
            childViewHolder.checkBox = convertView.findViewById(R.id.unselected_word_check_box);
            convertView.setTag(childViewHolder);
        }else {
            childViewHolder = (SelectedWordExpandableListAdapter.ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(wordsList.get(groupPosition).get(childPosition));

        childViewHolder.checkBox.setEnabled(false);
        childViewHolder.checkBox.setBackgroundResource(R.drawable.btn_phrase_checked_gray);


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
