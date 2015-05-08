package com.cs571.nanjiang.ebaysearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by slyar on 4/30/15.
 */
public class ResultActivity extends ActionBarActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        if (intent != null) {
            TextView resultFor = (TextView) findViewById(R.id.resultFor);
            resultFor.setText(intent.getStringExtra("resultFor"));

            ArrayList<ProductParcel> items = intent.getParcelableArrayListExtra("res_json");

            if (items != null) {
                final CustomAdapter customAdapter = new CustomAdapter(this, items);
                ListView itemListView = (ListView) findViewById(R.id.listview);
                itemListView.setItemsCanFocus(false);
                itemListView.setAdapter(customAdapter);

                // item onClick Listener
                itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ProductParcel item = (ProductParcel) customAdapter.getItem(i);
                        Intent tmpIntent = new Intent(getApplicationContext(), DetailActivity.class);
                        Bundle tmpBundle = new Bundle();
                        tmpBundle.putParcelable("item", item);
                        tmpIntent.putExtras(tmpBundle);
                        ResultActivity.this.startActivity(tmpIntent);
                    }
                });


            }

        }
    }
}

class CustomAdapter extends ArrayAdapter<ProductParcel> {

    public final Context context;
    public final ArrayList<ProductParcel> items;

    CustomAdapter(Context context, ArrayList items) {
        super(context, R.layout.list_view_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater nanInflater = LayoutInflater.from(getContext());
        View customView = nanInflater.inflate(R.layout.list_view_item, parent, false);

        ImageView result_img = (ImageView) customView.findViewById(R.id.result_img);
        TextView result_title = (TextView) customView.findViewById(R.id.result_title);
        TextView result_price = (TextView) customView.findViewById(R.id.result_price);

        result_title.setText(items.get(position).title);
        result_price.setText(items.get(position).convertedCurrentPrice + " " + items.get(position).shippingServiceCost);
        new getIMG(result_img).execute(items.get(position).galleryURL);

        return customView;
    }

    @Override
    public ProductParcel getItem(int position) {
        return items.get(position);
    }
}
