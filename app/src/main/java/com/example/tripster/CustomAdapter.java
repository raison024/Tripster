package com.example.tripster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private final List<Place> placeList;
    private final List<Place> placeListAll;
    private final ClickListener listener;
    SQLiteHelper sqLiteHelper;

    CustomAdapter(Context context, ArrayList<Place> placeList, ClickListener listener){
        this.context = context;
        this.placeList = placeList;
        this.placeListAll = new ArrayList<>(placeList);
        this.listener = listener;
        sqLiteHelper = new SQLiteHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.place_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Place plist =  placeList.get(position);
        if(plist.getFavourite() == 1) {
            holder.list_favbutton.setBackgroundResource(R.drawable.favorite_fill);
        }
        else {
            holder.list_favbutton.setBackgroundResource(R.drawable.favorite);
        }
        holder.list_favbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int intid= plist.getId();

                if(plist.getFavourite() == 1) {
                    holder.list_favbutton.setBackgroundResource(R.drawable.favorite);
                    sqLiteHelper.removeFavourite(intid);
                    plist.setFavourite(0);
                    Toast.makeText(context.getApplicationContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                }
                else {
                    holder.list_favbutton.setBackgroundResource(R.drawable.favorite_fill);
                    sqLiteHelper.addFavourite(intid);
                    plist.setFavourite(1);
                    Toast.makeText(context.getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.txtname.setText(String.valueOf(plist.getName()));
        holder.txtdesc.setText(String.valueOf(plist.getDesc()));

        float floatrating= plist.getRating();
        String strrating = Float.toString(floatrating);
        holder.list_rating.setText(strrating);

        byte[] placeImage = plist.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(placeImage, 0, placeImage.length);
        holder.imgplace.setImageBitmap(bitmap);

        holder.placelistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(plist.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    //U
    @Override
    public Filter getFilter() {
        return filter;
    }
    //U
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constaint) {

            List<Place> filteredList = new ArrayList<>();

            if (constaint.toString().isEmpty()) {
                filteredList.addAll(placeListAll);
            } else {
                for (Place place: placeListAll) {
                    if (place.getName().toLowerCase().contains(constaint.toString().toLowerCase())
                            || place.getDesc().toLowerCase().contains(constaint.toString().toLowerCase())) {
                        filteredList.add(place);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            placeList.clear();
            placeList.addAll((Collection<? extends Place>) filterResults.values);
            notifyDataSetChanged();
        }
    };



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtname, txtdesc, list_rating;
        ImageView imgplace;
        ImageButton list_favbutton;
        LinearLayout placelistlayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);
            txtdesc = itemView.findViewById(R.id.txtdesc);
            list_rating = itemView.findViewById(R.id.list_rating);
            imgplace = itemView.findViewById(R.id.imgplace);
            list_favbutton = itemView.findViewById(R.id.list_favbutton);
            placelistlayout = itemView.findViewById(R.id.placelistlayout);
        }
    }

    interface ClickListener{
        void onClick(int id);
    }
}
