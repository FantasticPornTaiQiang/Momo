package com.example.fragment;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.momo.HomeActivity;
import com.example.momo.MainActivity;
import com.example.momo.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ReviewTestFragment extends Fragment {

    private String word;
    //进入单词答案
    private Button goWord;
    //顶栏单词信息
    private TextView wordTextView;
    private TextView wordVoice;
    private ImageButton wordVoiceImageBtn;
    //上个单词
    private ImageButton backWord;
    //ps和pron
    private List<String> ps = new ArrayList<>();//音标
    private List<String> pron = new ArrayList<>();//发音（mp3的url）

    public ReviewTestFragment(String word) {
        this.word = word;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_review_test,container,false);

        wordTextView = view.findViewById(R.id.word_text_view);
        wordVoice = view.findViewById(R.id.word_voice_text_view);
        wordVoiceImageBtn = view.findViewById(R.id.word_voice_image_button);
        goWord = view.findViewById(R.id.go_word_btn);
        backWord = view.findViewById(R.id.back_word_btn);
        wordTextView.setText(word);

        goWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                ReviewAnswerFragment reviewAnswerFragment = new ReviewAnswerFragment(word);
                homeActivity.setReviewFragment(reviewAnswerFragment);
            }
        });

        backWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.setWordPosition(-1);
                ReviewTestFragment reviewTestFragment = new ReviewTestFragment(homeActivity.getWord());
                homeActivity.setReviewFragment(reviewTestFragment);
            }
        });

        String wordUrl = "http://dict-co.iciba.com/api/dictionary.php?w="+ word +"&key=7EA04BB54F883EF48494CED2EC02F64B";
        getMeaningByXml(wordUrl,view);


        return view;
    }

    public void getVoice(){

    }

    //发起请求，返回xml数据，并解析，然后将界面显示出来
    public void getMeaningByXml(String wordUrl,final View view){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(wordUrl).build();
        Call call = client.newCall(request);

        //异步调用并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("GetMeaningActivity", "失败");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String xmlData = response.body().string();
                //解析xml数据
                analyzeXml(xmlData);
                //显示界面
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMeaning(view);
                    }
                });
            }
        });
    }

    //解析xml数据
    public void analyzeXml(String xmlData){
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();//得到当前解析的事件

            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析某个节点
                    case XmlPullParser.START_TAG: {
                        if ("pron".equals(nodeName)) {
                            pron.add(xmlPullParser.nextText());
                        } else if ("ps".equals(nodeName)){
                            ps.add(xmlPullParser.nextText());
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //将解析后的数据显示出来
    public void showMeaning(final View view) {
        //ps和pron
        if ((!ps.isEmpty()) && (!pron.isEmpty())) {
            TextView psTextView1 = view.findViewById(R.id.word_voice_text_view);
            psTextView1.setText("[" + ps.get(0) + "]");
            ImageButton pronImageButton = view.findViewById(R.id.word_voice_image_button);
            pronImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(pron.get(0));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
