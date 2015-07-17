/*----------------------------------------------------------------
*  안드로이드는 이미 , JSON Parser를 보유하고 있다!!
*  따라서 Web서버로 부터 가져온 텍스트데이터를 JSON 파서를 이용해
*  분석하여 출력해본다!!
----------------------------------------------------------------*/
package json.test;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import json.test.model.MusicDTO;
import kr.co.stylenetwork.musicplayer.R;

public class JSONTest extends Activity {
    MyAsync async;
    EditText txt_url;
    ListView listView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_json);
        txt_url = (EditText) findViewById(R.id.txt_url);
        listView = (ListView) findViewById(R.id.listView);

        /*리스트뷰에 어댑터 적용*/
        adapter = new MyAdapter(this);

        listView.setAdapter(adapter);
    }

    /*------------------------------------------------
     웹서버 접속 전, 네트워크 상태 확인한다!!
    ------------------------------------------------*/
    public void checkNetwork() {
        ConnectivityManager connMgr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            loadURL();
        } else {
            Toast.makeText(this,
                    "접속할수 없습니다.네트워크 상태를 확인하세요",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void loadURL() {
        String url = txt_url.getText().toString();

        async = new MyAsync(adapter, listView);

        async.execute(url);/* doInbackground() 호출*/
    }

    public void btnClick(View view) {
        checkNetwork();
    }
}












