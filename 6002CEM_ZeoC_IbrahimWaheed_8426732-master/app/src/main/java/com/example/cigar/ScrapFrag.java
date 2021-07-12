package com.example.cigar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScrapFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScrapFrag extends Fragment {

   RecyclerView mRecycleView;

    public ScrapFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_webscrap,container,false);
        mRecycleView = (RecyclerView)view.findViewById(R.id.recycle_web_data);
        new SimpleCigarDataScrap().execute();
        return view;


    }

    // async as we are dealing with online , thus we dont want our app to hang or freeze up
    private class SimpleCigarDataScrap extends AsyncTask<Void,Void, Void> {
        private Exception exception;
        String Url = "https://www.simplycigars.co.uk/cuban-cigars-c-70.html"; // uk site
        ArrayList<Bitmap> Images = new ArrayList<>();
        ArrayList<String> Titles_array = new ArrayList<>();
        String result;
        webdataRecAdpater adp;
        @Override
        protected Void doInBackground(Void... url) {

            try {
                result ="Test";
                Document docs = Jsoup.connect(Url).timeout(6000).get();
                //result = docs.outerHtml();
                Elements body = docs.select("div.category-listing");
                Log.d("bodtag", String.valueOf(body.select("ul").select("li").size()));
                for (Element elements : body.select("ul").select("li")){
                    Element images = elements.select("img").first();
                    String images_url = images.absUrl("src");
                    //Log.d("ImageUrl",images_url);
                    // but in a input buffer
                    InputStream input_img = new java.net.URL(images_url).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input_img);
                    String titles = elements.select("span").text();
                   // Log.d("TitleSpan",elements.select("span").text());
                    Images.add(bitmap);
                    Titles_array.add(titles);
                }

                Log.d("bitmapArray",Images.toString());
                Log.d("TitleArray",Titles_array.toString());
                adp =  new webdataRecAdpater(getContext(),Titles_array,Images);
            }
            catch (IOException e){
                e.printStackTrace();
                result= e.toString();
            }
            return null;

        }
        protected void onPostExecute(Void Avoid)  {

            mRecycleView.setAdapter(adp);
            mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            super.onPostExecute(Avoid);

        }



    }




}
