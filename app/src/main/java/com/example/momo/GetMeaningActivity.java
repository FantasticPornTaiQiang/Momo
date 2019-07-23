package com.example.momo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;
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

public class GetMeaningActivity extends AppCompatActivity {

    private List<String> ps = new ArrayList<>();//音标
    private List<String> pron = new ArrayList<>();//发音（mp3的url）
    private List<String> pos = new ArrayList<>();//词性
    private List<String> acceptation = new ArrayList<>();//释义
    private List<String> orig = new ArrayList<>();//例句
    private List<String> trans = new ArrayList<>();//例句翻译
    private LinearLayout titleContainer;//标题容器
    private LinearLayout mContainer;//总容器
    private LinearLayout origContainer;//例句容器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_meaning_layout);

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        String wordUrl = "http://dict-co.iciba.com/api/dictionary.php?w="+ word +"&key=7EA04BB54F883EF48494CED2EC02F64B";
        titleContainer = findViewById(R.id.title_container);
        TextView title = new TextView(this);
        title.setText(word);
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        titleContainer.addView(title);

        mContainer = findViewById(R.id.mContainer);
        origContainer = findViewById(R.id.orig_scrollview);

        getMeaningByXml(wordUrl);

        ImageButton backToWordListBtn = findViewById(R.id.back_to_word_list_btn);
        backToWordListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //发起请求，返回xml数据，并解析，然后将界面显示出来
    public void getMeaningByXml(String wordUrl){
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMeaning();
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
                        } else if ("pos".equals(nodeName)) {
                            pos.add(xmlPullParser.nextText());
                        } else if ("acceptation".equals(nodeName)) {
                            acceptation.add(xmlPullParser.nextText());
                        } else if ("orig".equals(nodeName)) {
                            orig.add(xmlPullParser.nextText());
                        } else if ("trans".equals(nodeName)) {
                            trans.add(xmlPullParser.nextText());
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
    public void showMeaning(){
        //ps和pron
        if((!ps.isEmpty())&&(!pron.isEmpty())){
            LinearLayout psLinearLayout1 = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.ps_container,mContainer,false);
            TextView psTextView1 = psLinearLayout1.findViewById(R.id.ps_text_view);
            psTextView1.setText("英 [" + ps.get(0) + "]");
            ImageButton pronImageButton1 = psLinearLayout1.findViewById(R.id.ps_image_button);
            pronImageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(pron.get(0));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            mContainer.addView(psLinearLayout1,2);
            LinearLayout psLinearLayout2 = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.ps_container,mContainer,false);
            TextView psTextView2 = psLinearLayout2.findViewById(R.id.ps_text_view);
            psTextView2.setText("美 [" + ps.get(1) + "]");
            ImageButton pronImageButton2 = psLinearLayout2.findViewById(R.id.ps_image_button);
            pronImageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(pron.get(1));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            mContainer.addView(psLinearLayout2,2);
        }
        //pos和acceptation
        if((!pos.isEmpty())&&(!acceptation.isEmpty())){
            for(int i=pos.size()-1;i>=0;i--){
                LinearLayout posAndAcceptationLinearLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.pos_and_acceptation_container,mContainer, false);
                TextView posTextView = posAndAcceptationLinearLayout.findViewById(R.id.pos_text_view);
                posTextView.setText(pos.get(i));
                TextView acceptationTextView = posAndAcceptationLinearLayout.findViewById(R.id.acceptation_text_view);
                acceptationTextView.setText(acceptation.get(i));
                mContainer.addView(posAndAcceptationLinearLayout,5);
            }
        }

        //orig和trans
        if((!orig.isEmpty())&&(!trans.isEmpty())){
            for(int i=0;i<orig.size();i++){
                LinearLayout origAndTransLinearLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.orig_and_trans_container,origContainer,false);
                TextView origTextView = origAndTransLinearLayout.findViewById(R.id.orig_text_view);
                orig.get(i).replace("\n","");
                origTextView.setText(orig.get(i)+trans.get(i));
                origContainer.addView(origAndTransLinearLayout);
            }
        }

    }
}
