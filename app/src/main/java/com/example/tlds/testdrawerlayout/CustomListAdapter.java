package com.example.tlds.testdrawerlayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by PC on 4/27/2016.
 */
public class CustomListAdapter extends ArrayAdapter {
    private List<Match> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    private ProgressDialog progressDialog;
    public CustomListAdapter(Context aContext, List<Match> listData) {
        super(aContext,R.layout.list_view_item);
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

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_item, null);
            holder = new ViewHolder();
            holder.field_name = (TextView) convertView.findViewById(R.id.txt_Field);
            holder.maximum_players = (TextView) convertView.findViewById(R.id.txt_Max);
            holder.price = (TextView) convertView.findViewById(R.id.txt_Price);
            holder.date = (TextView) convertView.findViewById(R.id.txt_Date);

            holder.btnJoin = (Button) convertView.findViewById(R.id.btnJoin);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Match match = this.listData.get(position);
        holder.field_name.setText(match.getField_name());
        holder.maximum_players.setText("Maximum players: " + match.getMaximum_players());
        holder.price.setText("Price: " + match.getPrice());
        holder.date.setText("Date: " + match.getDate());

        holder.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Join: ", listData.get(position).getField_name() + "----user_id: " + match.getUser_id());
                new SendJoin(match.getUser_id(), match.getMatch_id());
            }
        });


        return convertView;
    }


    static class ViewHolder {
        TextView field_name;
        TextView maximum_players;
        TextView price;
        TextView date;

        Button btnJoin;
    }

    class SendJoin implements LoadJson.OnFinishLoadJSonListener{

        public SendJoin(String user, String match){

            LoadJson loadJsonJoin = new LoadJson();
            loadJsonJoin.setOnFinishLoadJSonListener(this);

            HashMap<String, String> dataJoin = new HashMap<>();
            dataJoin.put("user_id", user);
            dataJoin.put("match_id", match);

            loadJsonJoin.sendDataToServer(Var.METHOD_JOIN_MATCH, dataJoin);
            progressDialog.show();
        }

        @Override
        public void finishLoadJSon(String error, String json) {
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }
            try {
                if (json != null) {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getBoolean(Var.KEY_ADD_MATCH)) {
                        //join thanh cong
                        Var.showToast(context, context.getResources().getString(R.string.add_join_success));
                    } else {
                        Var.showToast(context, context.getResources().getString(R.string.add_join_fail));
                    }
                } else {
                    Var.showToast(context, error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
