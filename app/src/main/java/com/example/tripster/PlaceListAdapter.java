package com.example.tripster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaceListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Place> placeList;
    int position;
    private ClickListener listener;

    public PlaceListAdapter(Context context, int layout, ArrayList<Place> placeList, ClickListener listener) {
        this.context = context;
        this.layout = layout;
        this.placeList = placeList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtname,txtdesc;
        ImageButton list_favbutton;
        LinearLayout placelistlayout;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        this.position = position;
        View row = view;
        ViewHolder holder = new ViewHolder();
        SQLiteHelper sqLiteHelper;

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtname = (TextView) row.findViewById(R.id.txtname);
            holder.txtdesc = (TextView) row.findViewById(R.id.txtdesc);
            holder.imageView = (ImageView) row.findViewById(R.id.imgplace);
            holder.list_favbutton = (ImageButton) row.findViewById(R.id.list_favbutton);
            holder.placelistlayout = (LinearLayout) row.findViewById(R.id.placelistlayout);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Place place = placeList.get(position);

        holder.txtname.setText(place.getName());
        holder.txtdesc.setText(place.getDesc());

        byte[] placeImage = place.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(placeImage, 0, placeImage.length);
        holder.imageView.setImageBitmap(bitmap);

        int fid = Integer.valueOf(place.getFavourite());
        if(fid == 1) {
            holder.list_favbutton.setBackgroundResource(R.drawable.favorite_fill);
        }
        else {
            holder.list_favbutton.setBackgroundResource(R.drawable.favorite);
        }

        ViewHolder finalHolder = holder;
        holder.list_favbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fid == 1) {
                    finalHolder.list_favbutton.setBackgroundResource(R.drawable.favorite);
                    Toast.makeText(context.getApplicationContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                }
                else {
                    finalHolder.list_favbutton.setBackgroundResource(R.drawable.favorite_fill);
                    Toast.makeText(context.getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.placelistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(place.getId());
            }
        });

        return row;
    }

    interface ClickListener{
        void onClick(int id);
    }
}
