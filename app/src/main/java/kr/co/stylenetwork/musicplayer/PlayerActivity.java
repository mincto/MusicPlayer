package kr.co.stylenetwork.musicplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.List;

import json.test.MyAdapter;
import json.test.MyAsync;
import json.test.model.MusicDTO;

public class PlayerActivity extends Activity implements AdapterView.OnItemClickListener{
    String TAG;
    ListView listView;
    MyAdapter adapter; /*보여질 데이터가 복합위젯일 아닐경우 단순한 TextView 하나일경우엔 굳이 Adapter를 재정의하지 말자!!*/
    String sd_path;
    /*안드로이드에서는 음악,동영상 등 미디어파일을 제어하기 위해 지원되는 객체*/
    MediaPlayer mediaPlayer;
    ImageView[] btn= new ImageView[4];
    String current_title;
    int current_position;/*현재 재생중인 음악의 ArrayList내의 index*/
    TextView txt_title;
    String url="http://192.168.0.8:8080";
    String  filename;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=this.getClass().getName();

        setContentView(R.layout.activity_player);
        listView = (ListView)findViewById(R.id.listView);
        txt_title = (TextView)findViewById(R.id.txt_title);

        btn[0]=(ImageView)findViewById(R.id.bt_prev);
        btn[1]=(ImageView)findViewById(R.id.bt_stop);
        btn[2]=(ImageView)findViewById(R.id.bt_play);
        btn[3]=(ImageView)findViewById(R.id.bt_next);

        bar=(ProgressBar)findViewById(R.id.bar);

        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);/*리스너와의 연결*/

        checkNetwork();
    }

    public void loadFormSDCard(){
        /*
        * 스마트폰마다 SDCARD 의 위치가 틀릴 수 있으므로,
        * 본인의 스마트폰의 SDCARD 디렉토리를 프로그래밍적으로 조사해보자
        * */
        /* 각 스마트폰 환경에 맞게 알아서 SDCARD저장소 디렉토리를 구함*/
        File dir=Environment.getExternalStorageDirectory();

        /* SD카드 디렉토리의 경로 얻자!!*/
        sd_path=dir.getAbsolutePath();

        Log.d(TAG,"SD카드 디렉토리경로는 "+sd_path);

        /* SD카드내의 하위 디렉토리 출력해보기!!*/
        File f = new File(sd_path+"/Music");

        File[] child=f.listFiles();
        Log.d(TAG, "child is " + child);

        /* improved for  */
        for(File file : child ){
            Log.d(TAG, file.getName());
            //list.add(file.getName());/*파일명을 리스트에 담자*/
        }
    }

    /*-------------------------------------------------------------------
        네트워크의 상태확인한다
        왜?? 네트워크 상태가 유효할때만 데이터를 가져올 수 있으므로.
    -------------------------------------------------------------------*/
    public void checkNetwork(){
        ConnectivityManager connMgr = null;
        connMgr=(ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(this,"네트워크 상태 유효함",Toast.LENGTH_SHORT).show();

            /*웹서버에 연동 시작*/
            MyAsync myAsync = new MyAsync(this, adapter);
            myAsync.execute(url+"/member.jsp");

        } else {
            Toast.makeText(this,"네트워크 상태 문제가 있습니다.",Toast.LENGTH_SHORT).show();
        }

    }

    public void play(){
        if(mediaPlayer == null){ /*최초의 재생이라면..*/
            mediaPlayer= new MediaPlayer();
            try {
            /*실행할 파일 지정 */
                //mediaPlayer.setDataSource(this, Uri.parse(sd_path+"/Music"+"/"+current_title));
                mediaPlayer.setDataSource(this, Uri.parse(url+"/music/"+filename));

                Log.d(TAG, url+"/music/"+filename);

                mediaPlayer.prepare();/*재생전 초기화 및 준비*/
                mediaPlayer.start();
                /*정지할 수 있도록 일시정지 버튼을 보여주자*/
                btn[2].setImageResource(R.drawable.pause);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{ /*멈춘후 다시 재생이라면..*/
            if(mediaPlayer.isPlaying()){/*플레이 중이라면..*/
                mediaPlayer.pause();
                btn[2].setImageResource(R.drawable.play);
            }else{
                mediaPlayer.start();
                btn[2].setImageResource(R.drawable.pause);
            }
            /*이미 멈추어있다면 다시  start()*/
        }
    }

    public void stop(){
        if(mediaPlayer !=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            btn[2].setImageResource(R.drawable.play);
        }
    }
    public void next() {
        current_position++;
        setTitle();
        stop();
        play();
    }
    public void prev(){
        current_position--;
        setTitle();
        stop();
        play();
    }

    public void btnClick(View view){
        switch (view.getId()){
            case R.id.bt_prev:prev();break;
            case R.id.bt_stop:stop();break;
            case R.id.bt_play:play();break;
            case R.id.bt_next:next();break;
        }
    }

    /*재생 제목교체 하기 */
    public void setTitle(){
        List list=adapter.getList();
        MusicDTO dto=(MusicDTO)list.get(current_position);
        current_title= dto.getTitle();
        txt_title.setText(current_title);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RelativeLayout layout = (RelativeLayout)view;
        TextView txt_title=(TextView)layout.findViewById(R.id.txt_title);
        TextView txt_singer=(TextView)layout.findViewById(R.id.txt_singer);

        List list=adapter.getList();
        MusicDTO dto=(MusicDTO)list.get(position);
        filename=dto.getFile();
        current_title = txt_title.getText().toString();
        current_position=position;
        setTitle();
        stop();
        play();
        Toast.makeText(this,", 제목은 "+txt_title.getText()+",파일명은 "+filename, Toast.LENGTH_SHORT).show();
    }


    /*getter / setter*/

    public ProgressBar getBar() {
        return bar;
    }
}






