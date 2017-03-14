package com.example.svatts.my22;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

//import cz.msebera.android.httpclient.Header;

/**
 * Created by Sairav on 8/2/2016.
 */

public class Transaction {
    protected String desc;
    protected String date;
    protected String amount;
    protected String msg;
    protected String type;
    protected String uname = Profile.getCurrentProfile().getName();
    protected String uid = Profile.getCurrentProfile().getId();
    public String dateInMilli = null;
    // public Bitmap bitmap;

    int i = 0;
    public static List<Transaction> transactionList = new ArrayList<Transaction>();
    // protected Activity act ;
    protected Context context = null;

    public Transaction(Context con) {
        context = con;
        this.uname = Profile.getCurrentProfile().getName();
        this.uid = Profile.getCurrentProfile().getId();
    }

    public Double parseAvailBalYes(String msg) {
        if (!this.msg.contains("Tot Avbl Bal-Rs") && !this.msg.contains("Tot Avbl Bal-INR"))
            return null;
        Double availBal = null;
        if (this.msg.contains("Tot Avbl Bal-INR")) {
            availBal = Double.parseDouble(this.msg.substring(this.msg.indexOf("Tot Avbl Bal-INR") + "Tot Avbl Bal-INR".length(), this.msg.lastIndexOf("on")).trim().replaceAll(",", ""));
        } else {
            availBal = Double.parseDouble(this.msg.substring(this.msg.indexOf("Tot Avbl Bal-Rs") + "Tot Avbl Bal-Rs".length(), this.msg.lastIndexOf("on")).trim().replaceAll(",", ""));
        }
        String balinString = String.valueOf(availBal);
        return availBal;
    }

    public Double parseAvailBalBob(String msg) {
        if (!this.msg.contains("Clear Bal Rs."))
            return null;

        Double availBal = Double.parseDouble(this.msg.substring(this.msg.indexOf("Clear Bal Rs.") + "Clear Bal Rs.".length(), this.msg.indexOf(")")).trim().replaceAll(",", ""));
        String balinString = String.valueOf(availBal);
        return availBal;
    }

    public boolean isTransaction(String msg) {

        if ((msg.contains("A/c ...") || msg.contains("Ac XX") || msg.contains("1967 Sal")) && (msg.trim().startsWith("INR ") || msg.trim().toLowerCase().startsWith("rs")) && (msg.trim().toLowerCase().contains("credit") || msg.trim().toLowerCase().contains("debit")))
            return true;

        else
            return false;
    }

    public Double parseAmount(String msg) {
        String amt = null;
        msg = msg.toLowerCase();


        if (parseTransType(msg).equals("isCredit")) {
            amt = msg.substring(0, msg.indexOf("is credited to")).trim();
        } else if (parseTransType(msg).equals("isDebit")) {
            amt = msg.substring(0, msg.indexOf("is debited to")).trim();
        } else if (parseTransType(msg).equals("hasDebit")) {
            amt = msg.substring(0, msg.indexOf("has been debited to")).trim();
        } else if (parseTransType(msg).equals("hasCredit")) {
            amt = msg.substring(0, msg.indexOf("has been credited to")).trim();
        } else if (parseTransType(msg).equals("Credit")) {
            amt = msg.substring(0, msg.indexOf("credited to")).trim();
        } else if (parseTransType(msg).equals("Debit")) {
            amt = msg.substring(0, msg.indexOf("debited to")).trim();
        }

        if (amt.contains("rs.")) {
            amt = amt.replace("rs.", "").trim();
        }

        if (amt.contains("rs")) {
            amt = amt.replace("rs", "").trim();
        }

        if (amt.contains("inr")) {
            amt = amt.replace("inr", "").trim();
        }

        if (amt.contains(",")) {
            amt = amt.replaceAll(",", "");
        }
        double amount = Double.parseDouble(amt);

        //Toast.makeText(context, "AMOUNT::::" + amount, Toast.LENGTH_LONG).show();

        return amount;
    }

    public void test() {

        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = context.getContentResolver().query(uri, projection, null, null, null);
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + "strAddress, ");
                    smsBuilder.append(intPerson + "intPerson, ");
                    smsBuilder.append(strbody + "strbody, ");
                    smsBuilder.append(longDate + "longDate, ");
                    smsBuilder.append(int_Type);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }

        Toast.makeText(context, smsBuilder.toString(), Toast.LENGTH_LONG).show();
        Log.d("TEXTISSS", smsBuilder.toString());
    }

    public String parseTransType(String msg) {

        if (msg.toLowerCase().contains("is credited to")) {
            return "isCredit";
        } else if (msg.toLowerCase().contains("is debited to")) {
            return "isDebit";
        } else if (msg.toLowerCase().contains("has been debited to")) {
            return "hasDebit";
        } else if (msg.toLowerCase().contains("has been credited to")) {
            return "hasCredit";
        } else if (msg.toLowerCase().contains("credited to")) {
            return "Credit";
        } else if (msg.toLowerCase().contains("debited to")) {
            return "Debit";
        } else {
            Log.d("testtt", msg);

        }

        return "creditttt";
    }

    public String parseDate(String msg) {
        String date = null;

        Boolean hasDate = false;
        DateTime dateTime = new DateTime();
        // Toast.makeText(context,"LEN ::",Toast.LENGTH_LONG).show();
        String[] inputText = msg.split(" ");//split on a whitespace
        //Toast.makeText(context,"LEN is::"+inputText.length,Toast.LENGTH_LONG).show();
        String pattern = "dd-MMM";
        String pattern2 = "dd-MM-yyyy";
        String pattern3 = "dd/MM/yyyy";

        for (String str : inputText) {


            //Use the Parse() method
            try {
                //  Toast.makeText(context,"CHECK FOR:"+str,Toast.LENGTH_LONG).show();
                dateTime = DateTime.parse(str, DateTimeFormat.forPattern(pattern));
                String dateStr = dateTime.toString("dd/MM");
                //  Toast.makeText(context, "d1" + dateStr, Toast.LENGTH_SHORT).show();
                dateStr = dateStr + "/" + Calendar.getInstance().get(Calendar.YEAR);
                //  Toast.makeText(context, "d2" + dateStr, Toast.LENGTH_SHORT).show();
                dateTime = DateTime.parse(dateStr, DateTimeFormat.forPattern("dd/MM/yyyy"));
                hasDate = true;
                break;//no need to execute/loop further if you have your date

            } catch (Exception ex) {

                //Use the Parse() method
                try {
                    //  Toast.makeText(context,"CHECK FOR:"+str,Toast.LENGTH_LONG).show();
                    dateTime = DateTime.parse(str, DateTimeFormat.forPattern(pattern2));
                    dateTime = DateTime.parse(dateTime.toString("dd/MM/yyyy"), DateTimeFormat.forPattern("dd/MM/yyyy"));
                    hasDate = true;
                    break;//no need to execute/loop further if you have your date
                } catch (Exception z) {
                    //
                    try {
                        //  Toast.makeText(context,"CHECK FOR:"+str,Toast.LENGTH_LONG).show();
                        dateTime = DateTime.parse(str, DateTimeFormat.forPattern(pattern3));
                        dateTime = DateTime.parse(dateTime.toString("dd/MM/yyyy"), DateTimeFormat.forPattern("dd/MM/yyyy"));
                        hasDate = true;
                        break;//no need to execute/loop further if you have your date
                    } catch (Exception xx) {

                    }
                }
            }
        }


//after breaking from the foreach loop, you can check if hasDate=true
//if it is, then your user entered a date and you can retrieve it from the dateTime

        if (hasDate) {
            date = dateTime.toString("dd/MM/yyyy");
            //Toast.makeText(context,"Date is::"+date,Toast.LENGTH_LONG).show();

            //user entered a date, get it from dateTime
        } else {
            Toast.makeText(context, "Date is not in msg ", Toast.LENGTH_LONG).show();
            //user didn't enter any date
        }

/*

        else
        {
            Toast.makeText(context,"THIS IS D :(((",Toast.LENGTH_LONG).show();

        }
*/

        try {
            Date tdate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            dateInMilli = String.valueOf(tdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    private String parseDescription(String msg) {
        String desc = "";

        Log.d("DESCIS", desc);

        if (msg.contains("1967 Sal")) {
            desc = "Salary 1967";
        } else if (msg.toLowerCase().contains("a/c ...3300")) {
            desc = "BOB E-Banking";
        } else if (msg.toLowerCase().contains("mbanking")) {
            desc = "BOB M-Banking";
        } else {
            if (StringUtils.countMatches(msg, ":") == 4) {
                try {
                    desc = msg.substring(StringUtils.ordinalIndexOf(msg, ":", 4) + 1, msg.indexOf("Tot Avbl Bal"));
                } catch (StringIndexOutOfBoundsException sioobe) {
                    Log.d("testt2", msg);
                }
            }
            else if (StringUtils.countMatches(msg, ":") >= 5) {
                try {
                    desc = msg.substring(StringUtils.ordinalIndexOf(msg, ":", 5) + 1, msg.indexOf("Tot Avbl Bal"));
                } catch (StringIndexOutOfBoundsException sioobe) {
                    Log.d("testt2", msg);
                }
            }
        }


        if (desc.equals("") && msg.contains("Funds Trf /IMPS/RRN")) {
            desc = "IMPS Funds Transfer";
        }

        return desc;
    }

    public void call(final Context context) {
        this.context = context;

        Log.d("inside call now", "mhmm");

        if (parseTransType(msg).toLowerCase().contains("credit"))
            type = "Credit";

        if (parseTransType(msg).toLowerCase().contains("debit"))
            type = "Debit";

        if (isTransaction(msg)) {
            amount = parseAmount(msg).toString();


            ///////////////////////////////////////////// ASync HTTP CLIENT LIBRARY/////////////////////////////

            desc = parseDescription(msg);
            //setTransaction();

            if (!ScrollingActivity.stopReading) {
                setTransactionInSQLite();
            }
        }
        ///////////////////////////////////////////// ASync HTTP CLIENT LIBRARY/////////////////////////////
    }

    public void setTransaction() {
        RequestParams params = new RequestParams();

        params.put("date", dateInMilli);
        params.put("amount", Float.parseFloat(amount));
        params.put("type", type);
        params.put("desc", desc);
        params.put("tmsg",msg);
        params.put("uname", Profile.getCurrentProfile().getName());
        params.put("uid", Float.parseFloat(Profile.getCurrentProfile().getId()));

        Log.d("detaislllls",dateInMilli+amount+desc+type);
        Log.d("detaislllls2222",msg);


        PAAPI.post("/setTransaction", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("Response::", response.toString());

                // Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStart() {
                // called before request is started
                // Toast.makeText(context, "Going to make API CALL", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
                //Toast.makeText(context,"failed in setting :"+statusCode +"-"+throwable,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                ///Toast.makeText(context, "yo added successfully", Toast.LENGTH_LONG).show();
            }
        });

        PAAPI.cancelConxn();
    }

    public List<Transaction> getTransactions() {
        Thread bgThread = null;

        List<Transaction> trList = new ArrayList<>();

        try {
            trList = (List<Transaction>) new Mytask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return trList;
    }

    public List<Transaction> getTransactionsFromSQLite() {
        List<Transaction> transactionsFromSQLite = new ArrayList<Transaction>();

        DatabaseHandler dh = new DatabaseHandler(context);
        transactionsFromSQLite = dh.getTransactions();
        Log.d("transsize", "" + transactionsFromSQLite.size());
        return transactionsFromSQLite;
    }

    public void setTransactionInSQLite() {
        Log.d("inside setTransSqliet", "djsaj");
        DatabaseHandler dh = new DatabaseHandler(context);
        ScrollingActivity.stopReading = dh.setTransaction(this);
        Log.d("FirstStopReadingIs", String.valueOf(ScrollingActivity.stopReading));
    }

    public class Mytask extends AsyncTask<Object, Object, List<Transaction>> {

        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(context);
            pdia.setMessage("Loading...");
            pdia.show();
        }


        @Override
        protected List<Transaction> doInBackground(Object... params) {
            String text = "";
            BufferedReader reader = null;
            List<Transaction> trList = new ArrayList<>();
            List<Transaction> recentList = new ArrayList<>();
            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL("http://sairav.pythonanywhere.com/getTransaction");

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
              /*  wr.write( data );
                wr.flush();*/

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                Log.d("1^^^check", "point");
                text = sb.toString();


                Log.d("1^^^check", "poooo");
                JSONArray jsonArray = new JSONArray(text);

                Log.d("2^^jsonObj", jsonArray.getJSONObject(1).getString("uname"));
                Transaction trr = null;
                trList = new ArrayList<>();
                Log.d("1^^^check", "poin2t");

                int count = 5;
                for (int i = jsonArray.length() - 1; i >= 0; i--) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    trr = new Transaction(context);

                    trr.uname = jsonObject.getString("uname");
                    trr.desc = jsonObject.getString("description");
                    trr.amount = jsonObject.getString("amount");
                    trr.type = jsonObject.getString("type");
                    trr.uid = jsonObject.getString("uid");
                    trr.date = jsonObject.getString("ttime");

                    trList.add(trr);

                    if (count >= 1) {
                        recentList.add(trr);
                    }

                    Log.d("1^^^check", "3point");
                }
            } catch (Exception ex) {
                Log.d("exceptionBrah", ex.toString());
                Log.d("localized", ex.getLocalizedMessage());
                Log.d("mess", ex.getMessage());
            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }
            // Toast.makeText(context,"list size in fff "+trList.size(),Toast.LENGTH_SHORT).show();
            Log.d("debugP", String.valueOf(trList.size()));
            return trList;
        }

        @Override
        protected void onPostExecute(List<Transaction> o) {
            super.onPostExecute(o);
            pdia.dismiss();
            // Toast.makeText(context,"list size in fff "+o.size(),Toast.LENGTH_SHORT).show();
        }
    }
}


class PAAPI {
    protected static final String BASE_URL = "http://sairav.pythonanywhere.com";

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void cancelConxn() {
        client.cancelAllRequests(true);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}






/*

 VOLLEY REQUEST

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://sairav.pythonanywhere.com",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            // Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            Log.d("ResponseIs", response);

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("ErrorIs", String.valueOf(error));

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("date", date);
                    params.put("amount", amount);
                    params.put("type", type);
                    params.put("desc", desc);

                    params.put("uname", Profile.getCurrentProfile().getName());
                    params.put("uid", Profile.getCurrentProfile().getId());

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);


*/
