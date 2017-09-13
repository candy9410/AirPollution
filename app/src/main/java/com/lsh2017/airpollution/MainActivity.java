package com.lsh2017.airpollution;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.co.recruit_lifestyle.android.widget.WaveView;

public class MainActivity extends AppCompatActivity {

    String APIkey="OGuAmWlj%2FuZC4ouxjtLOKHThV%2FJUJFSH1tGMmoSaUkNbZhJ%2BBFQn4ksgpdenGjIClwI8ETPGz73bjNNGE1z6ZQ%3D%3D";

    WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    WaveView waveView;
    String date;

    boolean refresh=false;
    urlThread urlThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        date=sdf.format(new Date());

        Log.d("날짜",date.toString());

      //  urlThread.start();

        waveSwipeRefreshLayout=(WaveSwipeRefreshLayout)findViewById(R.id.wave_swipe);
        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                urlThread = new urlThread();
                urlThread.start();
                waveSwipeRefreshLayout.setRefreshing(false);
            }

        });


        Log.d("스와이프","끝");


    }
    class urlThread extends Thread{

        @Override
        public void run() {
            String address="http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMinuDustFrcstDspth?searchDate="+date+"&ServiceKey="+APIkey;
            Log.e("주소!",address.toString());

            try {
                //해임달
                URL url=new URL(address);
                //무지개로드생성
                InputStream is=url.openStream();
                InputStreamReader isr= new InputStreamReader(is);
                //xmlpullparser객체 생성
                XmlPullParserFactory pullParserFactory=XmlPullParserFactory.newInstance();
                XmlPullParser xpp=pullParserFactory.newPullParser();
                xpp.setInput(isr);

                xpp.next();
                int eventType=xpp.getEventType();

                String tag="";//태그이름

                String time="";
                String inform="";
                String grade="";

                while(eventType!=XmlPullParser.END_DOCUMENT){
                    Log.e("반복문 들어옴!","반복문 들어옴!");
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();
                            if(tag.equals("dataTime")){
                                xpp.next();
                                time=xpp.getText();
                                Log.d("날씨",time+"입니다.");
                            }else if(tag.equals("informOverall")){
                                xpp.next();
                                inform=xpp.getText();
                                Log.d("날씨",inform+"입니다.");
                            }else if(tag.equals("informGrade")){
                                xpp.next();
                                grade=xpp.getText();
                                Log.d("날씨",grade+"입니다.");
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag=xpp.getName();
                            if(tag.equals("item")){
                                Log.d("끝","끝");
                            }
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                    }
                    eventType=xpp.next();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }


    }
}
