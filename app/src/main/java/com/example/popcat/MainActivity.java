package com.example.popcat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    ImageButton btn_sound;
    ImageButton btn_fever;
    String shared = "file";


    boolean flag = true;

    int images[]={R.drawable.popcat1,R.drawable.popcat2,R.drawable.popcat3, R.drawable.sleep, R.drawable.walk};

    int count = 0;
    int i=0;
    int s=0;


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

        // destory
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
        MediaPlayer player = MediaPlayer.create(this, R.raw.sound1);
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

                player.start();

                count++; //숫자 카운트
                i++;    //img list, text

                factory();

                if (i == 1){
                    i = -1;
                } /*else if (count%50 == 0) {
                    catnip();
                } else {
                    Toast.makeText(getApplicationContext(), "뭔가 오류가 난거 같은데..", Toast.LENGTH_SHORT);
                    return false;
                }*/
                if (count%50 == 0) {
                    catnip();
                }
                return false;
            }
        });



        btn_fever.setOnClickListener(new View.OnClickListener() { //1초씩 카운트 다운되면서 toast 메세지 출력
            @Override
            public void onClick(View view) {
                CountDownTimer timer = new CountDownTimer(11000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (cttimer != null ) cttimer.cancel();
                        cttimer = Toast.makeText(getApplicationContext(),  "일어나기까지 "+ millisUntilFinished/1000+"초 남음", Toast.LENGTH_SHORT);
                        cttimer.show();
                    }

                    @Override
                    public void onFinish() {
                        if (cttimer != null ) cttimer.cancel();
                        cttimer = Toast.makeText(getApplicationContext(), "일어났다!!", Toast.LENGTH_SHORT);
                        cttimer.show();
                    }
                }.start();

                int i=3;
                count +=50;
                tv_text.setText("자는중..zzz");
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
            }, 11000);
            }
        });


        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catnip();
            }
        });


    }

    @Override   // menu바
    public boolean onCreateOptionsMenu( Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:

                SharedPreferences SharePref = getSharedPreferences("SharePref", MODE_PRIVATE);
                SharedPreferences.Editor editor = SharePref.edit(); //SharedPreferences안에 editor을 연결해줌
                editor.clear();
                editor.commit();  //저장

                System.exit(0); // 앱 종료
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // 출력 공장
    public void factory(){
        String[] list = getResources().getStringArray(R.array.list);
        tv_text.setText(list[i]);
        tv_count.setText(String.valueOf(count)); // 카운트됨
        imagecat.setImageResource(images[i]);  // 이미지 출력
    }

    // 캣닢주는 공장
    public void catnip() {
        i= 2;
        MediaPlayer player = MediaPlayer.create(this, R.raw.sound2);
        player.start();
       // soundPool.play(sound2,1,1,0,0,1);
        factory();
        if (i == 2)
            i = -1;
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
