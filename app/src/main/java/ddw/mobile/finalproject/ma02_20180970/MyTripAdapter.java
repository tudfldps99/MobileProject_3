package ddw.mobile.finalproject.ma02_20180970;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTripAdapter extends BaseAdapter {

    public static final String TAG = "MyTripAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<NaverTripDto> list;
    private NaverNetworkManager networkManager = null;
    private ImageFileManager imageFileManager = null;

    public MyTripAdapter(Context context, int resource, ArrayList<NaverTripDto> list) {     //context, 각 뷰의 layout(ListView 항목 하나에 해당하는 layout), 원본 데이터에 해당하는 list
        this.context = context;
        this.layout = resource;
        this.list = list;
        imageFileManager = new ImageFileManager(context);
        networkManager = new NaverNetworkManager(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {     //개수 반환
        return list.size();
    }


    @Override
    public NaverTripDto getItem(int position) {     //position에 해당하는 item 반환
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {       //해당하는 dto의 it값 position 반환
        return list.get(position).get_id();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;
        ViewHolder viewHolder = null;       //효율성 높임.

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tvTitle);
            viewHolder.tvAddress = view.findViewById(R.id.tvAddress);
            viewHolder.tvRoadAddress = view.findViewById(R.id.tvRoadAddress);

            view.setTag(viewHolder);
        } else {        //view가 null이 아닌경우. 이미 만들어진 경우.
            viewHolder = (ViewHolder)view.getTag();
        }

        NaverTripDto dto = list.get(position);

        viewHolder.tvTitle.setText(dto.getTitle());
        viewHolder.tvAddress.setText(dto.getAddress());
        viewHolder.tvRoadAddress.setText(dto.getRoadAddress());

        return view;
    }

    public void setList(ArrayList<NaverTripDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    // ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {       //정적. static. 각 화면의 요소를 멤버로 가지게 함.
        public TextView tvTitle = null;
        public TextView tvAddress = null;
        public TextView tvRoadAddress = null;
    }
}

