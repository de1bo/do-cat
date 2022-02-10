package com.example.popcat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private int sound1, sound2;

    ImageView imagecat;
    TextView tv_count;
    Toast cttimer = null;
    TextView tv_text;
    Button btn_fever, btn_sound;
    String shared = "file";


    boolean flag = true;
    int images[]={R.drawable.popcat1,R.drawable.popcat2,R.drawable.popcat3};

    int count = 0;
    int i=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_sound = findViewById(R.id.btn_sound);
        btn_fever = findViewById(R.id.btn_fever);
        btn_sound = findViewById(R.id.btn_sound);
        tv_text = findViewById(R.id.tv_text);
        imagecat = findViewById(R.id.imagecat);
        tv_count = findViewById(R.id.tv_count);

        SharedPreferences SharePref = getSharedPreferences("SharePref", MODE_PRIVATE);
        count =  SharePref.getInt("count", 0);


        /*sound 부분*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        sound1 = soundPool.load(this,R.raw.sound1,1);
        sound2 = soundPool.load(this,R.raw.sound2,1);
        /*sound 끝*/

        String[] list = getResources().getStringArray(R.array.list);
        tv_text.setText(list[i]);

        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=2;
                soundPool.play(sound1,1,1,0,0,1);

            }
        });

        imagecat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                soundPool.play(sound1,1,1,0,0,1); //sound 실행 구문

                count++; //숫자 카운트
                i++;    //img list, text

                factory();

                if (i == 1){
                    i = -1;}
                return false;
            }
        });



        btn_fever.setOnClickListener(new View.OnClickListener() { //1초씩 카운트 다운되면서 toast 메세지 출력
            @Override
            public void onClick(View view) {
                CountDownTimer timer = new CountDownTimer(16000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (cttimer != null ) cttimer.cancel();
                        cttimer = Toast.makeText(getApplicationContext(),  + millisUntilFinished/1000+":SEC", Toast.LENGTH_SHORT);
                        cttimer.show();
                    }

                    @Override
                    public void onFinish() {
                        if (cttimer != null ) cttimer.cancel();
                        cttimer = Toast.makeText(getApplicationContext(), "FEVER종료", Toast.LENGTH_SHORT);
                        cttimer.show();
                    }
                }.start();

                int i=2;
                count +=200;
                tv_text.setText(list[i]);
                tv_count.setText(String.valueOf(count)); //카운트됨
                imagecat.setImageResource(images[i]);  //이미지 출력

                imagecat.setEnabled(false); //버튼 비활성화
                btn_fever.setEnabled(false); //버튼 비활성화
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() { //실제 running 구문
                    int i = 0;
                   // factory();
                    imagecat.setEnabled(true); //버튼 활성화
                    btn_fever.setEnabled(true); //버튼 활성화
                }
            }, 16000);
            }
        });


        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i= 2;
                soundPool.play(sound2,1,1,0,0,1);
                factory();
                if (i == 2)
                    i = -1;
            }
        });


    }
    // 출력 공장
    public void factory(){
        String[] list = getResources().getStringArray(R.array.list);
        tv_text.setText(list[i]);
        tv_count.setText(String.valueOf(count)); // 카운트됨
        imagecat.setImageResource(images[i]);  // 이미지 출력


    }
    @Override
    protected void onDestroy() {  //앱을 종료를 시켰을때(엑티비티가 파괴되었을때) sharedPreferences로 저장을 하고 나갈 수 있도록
        super.onDestroy();

        SharedPreferences SharePref = getSharedPreferences("SharePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = SharePref.edit(); //SharedPreferences안에 editor을 연결해줌

        editor.putInt("count", count);
       editor.commit();  //저장

    }
    }
