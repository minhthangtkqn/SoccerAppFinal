package com.example.tlds.testdrawerlayout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PC on 4/27/2016.
 */
public class CustomListAdapter extends BaseAdapter {
    private List<Match> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext, List<Match> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_item, null);
            holder = new ViewHolder();
            holder.field_name = (TextView) convertView.findViewById(R.id.txt_Field);
            holder.maximum_players = (TextView) convertView.findViewById(R.id.txt_Max);
            holder.price = (TextView) convertView.findViewById(R.id.txt_Price);
            holder.date = (TextView) convertView.findViewById(R.id.txt_Date);

            holder.btnJoin = (Button) convertView.findViewById(R.id.btnJoin);
            holder.btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Join",listData.get(position).getField_name());

                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Match match = this.listData.get(position);
        holder.field_name.setText("San: " + match.getField_name());
        holder.maximum_players.setText("Maximum players: " + match.getMaximum_players());
        holder.price.setText("Price: " + match.getPrice());
        holder.date.setText("Date: " + match.getDate());

        return convertView;
    }

    // Tìm ID của Image ứng với tên của ảnh (Trong thư mục mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    static class ViewHolder {
        TextView field_name;
        TextView maximum_players;
        TextView price;
        TextView date;

        Button btnJoin;
    }
}
