package com.example.svatts.my22;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by svatts on 18-Jan-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase db = getWritableDatabase();

    public DatabaseHandler(Context context) {
        super(context, "MoneytorDB", null, 1);
        this.context = context;
        String query = "create table if not exists Transactions (uname varchar(20),description varchar(100),amount double,type varchar(10),uid bigint(20),tmsg varchar(500),ttime varchar(20) not null primary key)";
        db.execSQL(query);


    }

    public void deleteTable()
    {
     SQLiteDatabase sqlite = this.getWritableDatabase();
        sqlite.execSQL("drop table Transactions");
        Log.d("TableDeleted","true");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Transaction> getTransactions() {

        Log.d("inside getting","yeahe");
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        List<Transaction> transList = new ArrayList<Transaction>();

       // String selectQuery = "select * from Transactions order by  strftime('%Y-%m-%d %H:%M:%S',ttime)";
        String selectQuery = "select * from Transactions order by ttime desc";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("inside gettingdsa","yeahe");
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(context);
                transaction.uname = cursor.getString(0);
                transaction.desc = cursor.getString(1);
                transaction.amount = cursor.getString(2);
                transaction.type = cursor.getString(3);
                transaction.uid = cursor.getString(4);

                transaction.msg = cursor.getString(5);
                String dateInMilli = cursor.getString(6);
                transaction.dateInMilli = dateInMilli ;

                Date date = new Date(Long.parseLong(dateInMilli));

                //'Log.d("datebrhhh",date.toString());

                transaction.date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(date);

               /* Log.d("uname",transaction.uname);
                Log.d("desc",transaction.desc);
                Log.d("amount",transaction.amount);
                Log.d("type",transaction.type);
                Log.d("uid",transaction.uid);
                Log.d("date",transaction.date);
*/
                // Adding transaction to list
                transList.add(transaction);
            } while (cursor.moveToNext());
        }

        return transList;
    }


    public void modifyTransDesc(String tmsg,String tdateInMilli,String newDesc)
        {
            //tdate to be in float...
            String updateQuery = "update Transactions set description='"+newDesc+"' where tmsg='"+tmsg+"' and ttime='"+tdateInMilli+"'";
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(updateQuery);
        }

    public boolean setTransaction(Transaction tr) {
        /* Log.d("valuesare :",tr.uname);
        Log.d("valuesare :",tr.desc);
        Log.d("valuesare :",tr.amount);
        Log.d("valuesare :",tr.type);
        Log.d("valuesare :",tr.uid);
        Log.d("valuesare :",tr.date);
*/
        String message = tr.msg;

        log.d("dateisssss",tr.date);
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(tr.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateInMilli = String.valueOf(date.getTime());

        Cursor cursor = db.rawQuery("select tmsg,ttime from Transactions where ttime='"+dateInMilli+"' and amount='"+tr.amount+"' and description='"+tr.desc+"'",null);

        if(cursor.moveToFirst())
        {
            if(cursor.getString(0)!=null)
            {
                Log.d("Already in DB",String.valueOf(tr.date));
                return true;
            }
        }

        String insertQuery = "insert or ignore into Transactions values ('" + tr.uname + "','" + tr.desc + "','" + tr.amount + "','" + tr.type + "','" + tr.uid + "','" + tr.msg + "','" + dateInMilli + "') ";

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(insertQuery);

        return false;

    }

}
//