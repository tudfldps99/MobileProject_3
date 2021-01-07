package ddw.mobile.finalproject.ma02_20180970;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCursorAdapter extends BaseAdapter {

    static final String TAG = "MyCursorAdapter";
    LayoutInflater inflater;
    int layout;
    int count;
    private Context context;
    private ArrayList<ContactDto> tripList;

    public MyCursorAdapter(Context context, int layout, ArrayList<ContactDto> tripList) {
        this.context = context;
        this.layout = layout;
        this.tripList = tripList;
        count = 0;

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tripList.size(); //원본데이터가 ArrayList 에 담겨있으니까 이 데이터의 개수를 반환하면 됨
    }

    @Override
    public Object getItem(int position) {
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tripList.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        ViewHolder viewHolder;

        Log.d(TAG, "getView()!");
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvImage = (ImageView)convertView.findViewById(R.id.ivImage);
            viewHolder.tvPlace = (TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvContactDate);
            viewHolder.tvDays = (TextView)convertView.findViewById(R.id.tvContactDays);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tvImage.setImageResource(tripList.get(position).getImg());
        viewHolder.tvPlace.setText(String.valueOf(tripList.get(position).getPlace()));
        viewHolder.tvDate.setText(String.valueOf(tripList.get(position).getDate()));
        viewHolder.tvDays.setText(String.valueOf(tripList.get(position).getDays()));

        return convertView;
    }
    static class ViewHolder {
        ImageView tvImage;
        TextView tvPlace;
        TextView tvDate;
        TextView tvDays;
    }
}