package com.example.svatts.my22;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by svatts on 10-Feb-17.
 */

public class TransDetails extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4;
    ImageView imageView,editDesc,doneDesc;
    EditText editText;
    LinearLayout editDescLL ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_trans);

        Bundle bundle = getIntent().getExtras();

        String tamt = bundle.get("tamt").toString();
        final String tdesc = bundle.get("tdesc").toString();
        final String tdate = bundle.get("tdateInMilli").toString();
        final String tdate2 = bundle.get("tdate").toString();
        final String tmsg = bundle.get("tmsg").toString();
        //Bitmap bitmap = (Bitmap) bundle.getParcelable("trBitmap");

        imageView = (ImageView) findViewById(R.id.ttimg);
        editDescLL = (LinearLayout)findViewById(R.id.editDescLL);
        editDesc=(ImageView)findViewById(R.id.editDesc);
        editText = (EditText) findViewById(R.id.etdesc);
        doneDesc = (ImageView)findViewById(R.id.doneDesc);

        tv1 = (TextView) findViewById(R.id.ttamt);
        tv2 = (TextView) findViewById(R.id.ttdesc);
        tv3 = (TextView) findViewById(R.id.ttdate);
        tv4 = (TextView) findViewById(R.id.ttmsg);

        TextView[] tvs = {tv1, tv2, tv3, tv4};

        tv1.setText("₹" + tamt);
        tv2.setText(tdesc);
        tv3.setText(tdate2.substring(0, 10));
        tv4.setText(tmsg);

        // iv.setImageBitmap(bitmap);

        for (TextView tv : tvs) {
            tv.setTypeface(ScrollingActivity.sansation);
        }

        Utilities.setIcons(tdesc,imageView);

        editDescLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setVisibility(View.VISIBLE);
                editText.setText(tdesc);
                tv2.setVisibility(View.INVISIBLE);
                editDesc.setVisibility(View.INVISIBLE);
                doneDesc.setVisibility(View.VISIBLE);
            }
        });

        doneDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setText(editText.getText());
                editText.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.VISIBLE);
                editDesc.setVisibility(View.VISIBLE);
                doneDesc.setVisibility(View.INVISIBLE);

                new DatabaseHandler(TransDetails.this).modifyTransDesc(tmsg,tdate,tv2.getText().toString());
                int i=0;
                int index = 0 ;
                Log.d("DateUNmille",tdate);

                for(Transaction tr: Transaction.transactionList)
                {

                    if(tr.dateInMilli.equals(tdate))
                    {
                        Log.d("DateUNmillehrere",tr.dateInMilli);
                        index = i;
                    }
                    i++;
                }

                Transaction newTrans = Transaction.transactionList.get(index);
                newTrans.desc = tv2.getText().toString();
                Transaction.transactionList.set(index,newTrans);

                Log.d("newDesciS",Transaction.transactionList.get(index).desc);
            }
        });
    }
}
