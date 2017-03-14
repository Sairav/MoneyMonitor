package com.example.svatts.my22;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {
    private SwitchCompat switchCompat;
    public static Typeface sansation;
    private MenuItem mSearchMenuItem;
    CoordinatorLayout coordinatorLayout = null;
    List<Transaction> trList = null;
    public static boolean stopReading;
    BarChart barChart = null;
    float[] expenses = new float[6];
    int[] credits = new int[6];
    String[] monthNames = new String[6];
    static HashMap<Integer, String> monthMap = new HashMap<Integer, String>();
    TextView userName;

    static {
        monthMap.put(0, "Jan");
        monthMap.put(1, "Feb");
        monthMap.put(2, "Mar");
        monthMap.put(3, "Apr");
        monthMap.put(4, "May");
        monthMap.put(5, "Jun");
        monthMap.put(6, "Jul");
        monthMap.put(7, "Aug");
        monthMap.put(8, "Sep");
        monthMap.put(9, "Oct");
        monthMap.put(10, "Nov");
        monthMap.put(11, "Dec");
    }

    private int currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        sansation = Typeface.createFromAsset(getAssets(), "Sansation-Bold.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coor);
        barChart = (BarChart) findViewById(R.id.chart);
        userName = (TextView) findViewById(R.id.userName);

        userName.setText(Profile.getCurrentProfile().getName().split(" ")[0] + " !");
        //new DatabaseHandler(this).deleteTable();
        //to delete table from sqlite - testing purpose
        userName.setTypeface(sansation);

        Log.d("now refreshing inbox", "yo");
        refreshSmsInbox();
        //new ReadingSMS().execute();
        //refresh inbox for new sms

        trList = new ArrayList<Transaction>();

        // trList = new Transaction(this).getTransactions();  // getting transactions from pythonanywhere server

        trList = new Transaction(this).getTransactionsFromSQLite();

        Transaction.transactionList = trList;

        getPreviousMonthsInfo(trList);
        updateBarChart(expenses);
        Log.d("gettingVals", "yooyyo");
/*

        Log.d("valuesare :",trList.get(0).uname);
        Log.d("valuesare :",trList.get(0).desc);
        Log.d("valuesare :",trList.get(0).amount);
        Log.d("valuesare :",trList.get(0).type);
        Log.d("valuesare :",trList.get(0).uid);
        Log.d("valuesare :",trList.get(0).date);
*/

        setRecentTrans();

        TextView month = (TextView) findViewById(R.id.month);
        month.setText(monthNames[0] + "-" + currentYear);
        month.setTypeface(sansation);

        TextView amt = (TextView) findViewById(R.id.amt);
        month.setTypeface(sansation);
        amt.setText("₹ " + expenses[0]);

        TextView recentHeading = (TextView) findViewById(R.id.recentHeading);
        month.setTypeface(sansation);


        TextView graphHeading = (TextView) findViewById(R.id.graphHeading);
        month.setTypeface(sansation);

        TextView totalAvail = (TextView) findViewById(R.id.totalAvail);
        TextView totalBalYes = (TextView) findViewById(R.id.totalBalYes);
        TextView totalBalBob = (TextView) findViewById(R.id.totalBalBob);

        TextView totalBalText = (TextView) findViewById(R.id.totalAvailText);
        TextView totalBalYesText = (TextView) findViewById(R.id.totalBalYesText);
        TextView totalBalBobText = (TextView) findViewById(R.id.totalBalBobText);

        totalAvail.setTypeface(sansation);
        totalBalYes.setTypeface(sansation);
        totalBalBob.setTypeface(sansation);
        totalBalBobText.setTypeface(sansation);
        totalBalYesText.setTypeface(sansation);
        totalBalText.setTypeface(sansation);

        Double yesBal = null, bobBal = null, totalAvailBal = null;
        boolean yesSet = false, bobSet = false;

        while (yesBal == null || bobBal == null) {
            for (Transaction tr : trList) {
                if (!yesSet) {
                    yesBal = tr.parseAvailBalYes(tr.msg);
                }

                if (yesBal != null) {
                    yesSet = true;
                }

                if (!bobSet) {
                    bobBal = tr.parseAvailBalBob(tr.msg);
                }

                if (bobBal != null) {
                    bobSet = true;
                }
            }
        }

        totalAvailBal = yesBal + bobBal;

        totalBalYes.setText("₹ " + String.valueOf(yesBal));
        totalBalBob.setText("₹ " + String.valueOf(bobBal));
        totalAvail.setText("₹ " + String.valueOf(totalAvailBal));
        setFacebookDP();

    }
// ------------------ onCreate Ends ----------------------//


    private void setFacebookDP() {
        Bundle params = new Bundle();
        params.putString("fields", "id,email,gender,cover,picture.type(large)");

        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                if (data.has("picture")) {
                                    ImageView imageProfile = (ImageView) findViewById(R.id.fbdp);
                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    //Bitmap profilePic= BitmapFactory.decodeStream(profilePicUrl .openConnection().getInputStream());
                                    //mImageView.setBitmap(profilePic);
                                    Glide.with(getApplicationContext())
                                            .load(profilePicUrl)
                                            .asBitmap()
                                            .transform(new CropCircleTransform(getApplicationContext()))
                                            .into(imageProfile);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();


    }

    private void getPreviousMonthsInfo(List<Transaction> trList) {

        for (Transaction tr : trList) {
            Date date = new Date(Long.parseLong(tr.dateInMilli));

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            cal.setTime(new Date());
            int currMonth = cal.get(Calendar.MONTH);
            int currYear = cal.get(Calendar.YEAR);
            currentYear = currYear;
            monthNames[0] = monthMap.get(currMonth);

            cal.add(Calendar.MONTH, -1);
            monthNames[1] = monthMap.get(cal.get(Calendar.MONTH));

            Calendar c2 = Calendar.getInstance();
            Calendar c3 = Calendar.getInstance();
            Calendar c4 = Calendar.getInstance();
            Calendar c5 = Calendar.getInstance();

            c2.add(Calendar.MONTH, -2);
            monthNames[2] = monthMap.get(c2.get(Calendar.MONTH));

            c3.add(Calendar.MONTH, -3);
            monthNames[3] = monthMap.get(c3.get(Calendar.MONTH));

            c4.add(Calendar.MONTH, -4);
            monthNames[4] = monthMap.get(c4.get(Calendar.MONTH));

            c5.add(Calendar.MONTH, -5);
            monthNames[5] = monthMap.get(c5.get(Calendar.MONTH));

            if (month == currMonth && year == currYear && tr.type.toLowerCase().equals("debit")) {

                expenses[0] = expenses[0] + Float.parseFloat(tr.amount);

            }
            //last month (cal rolled 1 month )
            else if (month == cal.get(Calendar.MONTH) && year == cal.get(Calendar.YEAR) && tr.type.toLowerCase().equals("debit")) {
                expenses[1] = expenses[1] + Float.parseFloat(tr.amount);
            } else if (month == c2.get(Calendar.MONTH) && year == c2.get(Calendar.YEAR) && tr.type.toLowerCase().equals("debit")) {
                expenses[2] = expenses[2] + Float.parseFloat(tr.amount);
            } else if (month == c3.get(Calendar.MONTH) && year == c3.get(Calendar.YEAR) && tr.type.toLowerCase().equals("debit")) {
                expenses[3] = expenses[3] + Float.parseFloat(tr.amount);
            } else if (month == c4.get(Calendar.MONTH) && year == c4.get(Calendar.YEAR) && tr.type.toLowerCase().equals("debit")) {
                expenses[4] = expenses[4] + Float.parseFloat(tr.amount);
            } else if (month == c5.get(Calendar.MONTH) && year == c5.get(Calendar.YEAR) && tr.type.toLowerCase().equals("debit")) {
                expenses[5] = expenses[5] + Float.parseFloat(tr.amount);
            }
        }

        Log.d("Total_This_month_is", String.valueOf(expenses[0]));
    }

    private void updateBarChart(float[] expenses) {
        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(0f, expenses[0]));
        entries.add(new BarEntry(1f, expenses[1]));
        entries.add(new BarEntry(2f, expenses[2]));
        entries.add(new BarEntry(3f, expenses[3]));
        entries.add(new BarEntry(4f, expenses[4]));
        entries.add(new BarEntry(5f, expenses[5]));

        BarDataSet barDataSet = new BarDataSet(entries, "Expenditure");
        barDataSet.setColor(Color.parseColor("#CDDC39"));
        barDataSet.setValueTextColor(Color.parseColor("#122112"));


        BarData barData = new BarData(barDataSet);

        // barData.setBarWidth(0.9f);

        barChart.setData(barData);
        //barChart.setFitBars(true);
        String[] labels = {"Jan", "Feb", "Mar", "Apr", "May", "June"};
        barChart.getXAxis().setValueFormatter(new LabelFormatter(monthNames));
        barChart.invalidate();

    }

    private void setRecentTrans() {

        if (Transaction.transactionList.size() > 0) {
            RelativeLayout[] relativeLayout = new RelativeLayout[6];

            for (int i = 1; i <= 5; i++) {

                relativeLayout[i] = (RelativeLayout) findViewById(getResources().getIdentifier("t" + i, "id", getPackageName()));


                TextView tv = (TextView) relativeLayout[i].findViewById(getResources().getIdentifier("tdesc", "id", getPackageName()));
                tv.setText(Transaction.transactionList.get(i - 1).desc);

                ImageView imageView = (ImageView)relativeLayout[i].findViewById(getResources().getIdentifier("timg","id",getPackageName()));
                Utilities.setIcons(tv.getText().toString(),imageView);

                String formattedTime = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                    SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
                    Date d = sdf.parse(Transaction.transactionList.get(i - 1).date);
                    formattedTime = output.format(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TextView tv1 = (TextView) relativeLayout[i].findViewById(getResources().getIdentifier("tdate", "id", getPackageName()));
                tv1.setText(formattedTime);

                TextView tv2 = (TextView) relativeLayout[i].findViewById(getResources().getIdentifier("tamt", "id", getPackageName()));
                tv2.setText(Transaction.transactionList.get(i - 1).amount);

            }
        } else {
            Toast.makeText(this, "Could not load transactions , try again.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        trList = Transaction.transactionList;
        setRecentTrans();
    }

    public void fun(View view) {
        Bundle bundle = ActivityOptions
                .makeSceneTransitionAnimation(this)
                .toBundle();

        /*bundle.putSerializable("trList",(Serializable)trList);*/
        Intent intent = new Intent(this, TransactionsActivity.class);

        startActivity(intent, bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int indexDate = smsInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        //   arrayAdapter.clear();

        int i = 0;

        do {
            //  String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
            //        "\n" + smsInboxCursor.getString(indexBody) + "\n";
            //    arrayAdapter.add(str);

            if (!stopReading) {
                Date date = new Date(Long.parseLong(smsInboxCursor.getString(indexDate)));


                String newDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
                //DateTime fmt = DateTime.parse(date.toString(),DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss z yyyy"));
                //String formattedDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").format(date);

                // Toast.makeText(this,newDate,Toast.LENGTH_SHORT).show();

                Transaction tr = new Transaction(this);
                if (tr.isTransaction(smsInboxCursor.getString(indexBody))) {
                    Log.d("here now bro", "yea");
                    tr.msg = smsInboxCursor.getString(indexBody);
                    tr.date = newDate;
                    tr.call(this);


                }
                i++;
            }
        } while (smsInboxCursor.moveToNext());

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;

        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar snack = Snackbar.make(coordinatorLayout,"Tap Back Again To Exit", Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public class ReadingSMS extends AsyncTask<String,String,String>{

        ProgressDialog mProgressDialog;

        protected void onPreExecute()
        {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(ScrollingActivity.this);
            mProgressDialog.setMessage("Reading Your Inbox");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);

            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {
            refreshSmsInbox();
            return null;
        }
    }

}

class LabelFormatter implements IAxisValueFormatter {
    private final String[] mLabels;

    public LabelFormatter(String[] labels) {
        mLabels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mLabels[(int) value];
    }
}
