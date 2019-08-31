package com.example.chimchakae.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chimchakae.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> followerList = new ArrayList<ListViewItem>();

    @Override
    public int getCount() {
        return followerList.size();
    }

    @Override
    public Object getItem(int position) {
        return followerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView followerIdView = (TextView) convertView.findViewById(R.id.tv_followerEmail);
        TextView followerCarNumView = (TextView) convertView.findViewById(R.id.tv_followerCarNum);

        ListViewItem listViewItem = followerList.get(position);

        followerIdView.setText(listViewItem.getUserId());
        followerCarNumView.setText(listViewItem.getCarNum());

        return convertView;

    }

    public void addItem(String followerId, String followerCarNum) {
        ListViewItem item = new ListViewItem();

        item.setCarNum(followerCarNum);
        item.setUserId(followerId);

        followerList.add(item);
    }


    public void refreshAdapter(ListViewAdapter items) {
        this.followerList.clear();
        this.followerList.addAll(items.followerList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.followerList.clear();
        notifyDataSetChanged();
    }
}
