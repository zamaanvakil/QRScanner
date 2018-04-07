package com.example.android.qrscanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

/**
 * Created by Zamaan on 22-02-2018.
 */

public class ScansListAdapter extends ArrayAdapter<Scan> {

    Context context;
    int resource;
    List<Scan> scanList;

    public ScansListAdapter(Context context, int resource, List<Scan> scanList) {
        super(context, resource, scanList);
        this.context = context;
        this.resource = resource;
        this.scanList = scanList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(resource,null);

        ImageView type_icon = (ImageView) view.findViewById(R.id.type_icon);
        TextView result_tv = (TextView) view.findViewById(R.id.result_tv);
        TextView date_tv = (TextView) view.findViewById(R.id.date_tv);

        Scan scan = scanList.get(position);

        if(Objects.equals(scan.getType(), "link")) {
            type_icon.setImageResource(R.drawable.link_icon);
        }
        result_tv.setText(scan.getResult());
        date_tv.setText(scan.getDate());


        return view;
    }
}
