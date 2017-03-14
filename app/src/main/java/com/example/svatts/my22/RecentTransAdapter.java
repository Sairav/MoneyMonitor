package com.example.svatts.my22;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by svatts on 24-Nov-16.
 */

public class RecentTransAdapter extends RecyclerView.Adapter<RecentTransAdapter.MyViewHolder> implements Filterable{
    final List<Transaction> InterimList = new ArrayList<Transaction>();

    private LayoutInflater inflater;
    List<Transaction> transactionList ;
    List<Transaction> filteredTransList;


    int filterCount=0;

    public RecentTransAdapter(List<Transaction> transactionList,Context context){
        this.transactionList = transactionList;
        filteredTransList = transactionList;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.transaction, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Transaction transaction = filteredTransList.get(position);

        holder.tamt.setText(transaction.amount);
        if(transaction.type.toLowerCase().contains("credit"))
        {
            holder.tamt.setTextColor(Color.parseColor("#009f00"));
            holder.tamt.setText("(+) "+transaction.amount);
        }
        else
        {
            holder.tamt.setTextColor(Color.parseColor("#9f0000"));
            holder.tamt.setText("(-) "+transaction.amount);
        }
        Date date = null;
        String formattedTime=null;
        Log.d("transDateis",transaction.date);
        try {
           // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
            Date d = sdf.parse(transaction.date);
             formattedTime = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tdesc.setTypeface(ScrollingActivity.sansation);
        holder.tamt.setTypeface(ScrollingActivity.sansation);
        holder.tdate.setTypeface(ScrollingActivity.sansation);

        holder.tdate.setText(formattedTime.toString());
        holder.tdesc.setText(transaction.desc);

        Utilities.setIcons(holder.tdesc.getText().toString(),holder.imageView);

        holder.imageView.buildDrawingCache(true);
        Bitmap bitmap = holder.imageView.getDrawingCache();
        //transaction.bitmap = bitmap;

    }

    @Override
    public int getItemCount() {
        return  filteredTransList.size();
    }

    @Override
    public Filter getFilter() {
        return new MyFilter(this,transactionList);
    }

    public Filter getFilter(int levelOfFilteredList) {

        /*for (int i = 0; i < this.getItemCount(); i++) {
            InterimList.add(this.getItem(i));
        }*/
        if(levelOfFilteredList==0)
        {
            return new MyFilter(this,InterimList);
        }
        //InterimList.addAll(filteredTransList) ;
        return new MyFilter(this,filteredTransList);
    }

    public void setList(List<Transaction> list) {
        this.filteredTransList = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tdate , tdesc , tamt ;
        public ImageView imageView ;
        public MyViewHolder(View view) {
            super(view);

            imageView = (ImageView)view.findViewById(R.id.timg);

            tdate = (TextView)view.findViewById(R.id.tdate);

            tdesc = (TextView)view.findViewById(R.id.tdesc);

            tamt = (TextView)view.findViewById(R.id.tamt);
        }

    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public class GetImages extends AsyncTask<String,String,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = getBitmapFromURL(params[0]);
            return bitmap;
        }
    }

     public Transaction getItem(int position)
     {
        return filteredTransList.get(position);
     }
}
