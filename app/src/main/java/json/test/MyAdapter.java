/*
* ListView 는 껍데기에 불과하기 때문에 실제적으로 리스트뷰에 출력될
* 데이터는 어댑터가 관리한다!!
* 출력할 데이터가 단순한 TextView 한개일 경우는 , 굳이 Adapter클래스를
* 재정의할 필요없이 ArrayAdapter 를 사용하면 되지만, 지금 우리가 제작
* 하려는 ListView에 출력될 아이템은 복합위젯이므로, ArrayAdapter를
* 사용해서는 않되면 우리 상황에 맞게 재정의 해야 한다!!
* */
package json.test;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import json.test.model.MusicDTO;
import kr.co.stylenetwork.musicplayer.R;

public class MyAdapter extends BaseAdapter {
    JSONTest jt;
    ArrayList<MusicDTO> list=new ArrayList<MusicDTO>();
    LayoutInflater inflater;/*XML해석하여 메모리에 알맞는 객체를 올려주는
    객체 = 인플레이터*/

    public MyAdapter( JSONTest jt){
        this.jt=jt;
        inflater=(LayoutInflater)jt.getSystemService(jt.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        * getCount메서드 반환값만큼 인플레이션 일으키자!!
        * LayoutInflater
        * */
        Log.d("MyAdapter","convertView"+convertView);

        View view=null;

        if(convertView ==null) { /*아직 아이템이 비워져 있다면..*/
            view = inflater.inflate(R.layout.layout_item, parent, false);
        }else{
            view=convertView;
        }

        TextView txt_title=(TextView)view.findViewById(R.id.txt_title);
        TextView txt_singer=(TextView)view.findViewById(R.id.txt_singer);

        MusicDTO dto=list.get(position);
        txt_title.setText(dto.getTitle());
        txt_singer.setText(dto.getSinger());

        return view;
    }
}








