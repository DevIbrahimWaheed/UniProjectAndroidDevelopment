package com.example.cigar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class webdataRecAdpater extends RecyclerView.Adapter<webdataRecAdpater.MyWebDataHolder>{
    Context context;
    ArrayList<String> text_title_string_data;
    ArrayList<Bitmap> bitmap;

    public webdataRecAdpater(Context ct,ArrayList<String> title , ArrayList<Bitmap> image){
        this.context =ct;
        this.text_title_string_data =title;
        this.bitmap = image;
        Log.d("apdaterTitle",text_title_string_data.toString());
        Log.d("adpaterBitmap",bitmap.toString());
    }

    @NonNull
    @Override
    public MyWebDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.webdata_rows,parent,false);
        return new MyWebDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWebDataHolder holder, int position) {

        holder.title_textView.setText(text_title_string_data.get(position));
        holder.web_imagesView.setImageBitmap(bitmap.get(position));

    }

    @Override
    public int getItemCount() {
        return text_title_string_data.size();
    }

    public class MyWebDataHolder extends RecyclerView.ViewHolder{

        TextView title_textView;
        ImageView web_imagesView;
        public MyWebDataHolder(@NonNull View itemView) {
            super(itemView);
            title_textView = itemView.findViewById(R.id.web_text_title);
            web_imagesView = itemView.findViewById(R.id.web_images);
        }
    }
}