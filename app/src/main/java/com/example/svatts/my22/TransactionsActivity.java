package com.example.svatts.my22;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by svatts on 21-Oct-16.
 */

public class TransactionsActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean isFabOpen = false;
    SearchView searchtrans;
    CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView syncButton;
    public TextView monthButton;
    public RelativeLayout monthSelLay, syncLay;
    public static RecentTransAdapter mAdapter;
    //private Switch aSwitch;
    ViewGroup sharedView;
    boolean filtered;
    //public FragmentCommunicator fragmentCommunicator;
    String dateSelected;
    public static List<Transaction> filteredList = new ArrayList<Transaction>();
    public static RecentTransAdapter testAdapter = null;
    public Context context;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    public FrameLayout searchFrame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        context = this;
        sharedView = (RelativeLayout) findViewById(R.id.rell);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.transMain);
        fab = (FloatingActionButton) findViewById(R.id.fab);
      /*  fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
      */
        searchFrame = (FrameLayout) findViewById(R.id.searchFrame);
        monthSelLay = (RelativeLayout) findViewById(R.id.monthSelLay);
        syncLay = (RelativeLayout) findViewById(R.id.syncLay);


        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_frwrd);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_bckwrd);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionsActivity.this,AddTrans.class));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAct);
        setSupportActionBar(toolbar);

/*
        Slide slide = new Slide(Gravity.TOP);
        slide.addTarget(R.id.trans);
        getWindow().setEnterTransition(slide);
*/
        syncButton = (TextView) findViewById(R.id.syncButton);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("syncStart","true");
                List<Transaction> transList = Transaction.transactionList;

                for(Transaction tr : transList)
                {
                    Log.d("sync in prog","yo");
                    tr.setTransaction();
                }

                Toast.makeText(TransactionsActivity.this,"Upload Successful",Toast.LENGTH_SHORT);
            }
        });
        searchtrans = (SearchView) findViewById(R.id.searchTrans);

        searchtrans.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.trans);
        List<Transaction> newList = new ArrayList<Transaction>();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Log.d("transssslostis", "" + Transaction.transactionList.size());
        newList = Transaction.transactionList;
        filteredList = newList;

        monthButton = (TextView) findViewById(R.id.monthButton);
        // filterButton = (Button) findViewById(R.id.filterButton);
        //       aSwitch = (Switch) findViewById(R.id.switch1);

        String currentMonth = "";

        Calendar cal = Calendar.getInstance();
      /*  Date date = new Date();
        cal.setTime(date);
*/
        currentMonth = new SimpleDateFormat("MMMM").format(cal.getTime());

        // LinearLayout ll = (LinearLayout)findViewById(R.id.monthSelect);
        monthButton.setText(currentMonth);

        monthSelLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                // FragmentTransaction ft = fm.beginTransaction();
                MyFrag dFragment = new MyFrag();
                // Show DialogFragment
                dFragment.show(fm, "Dialog Fragment");
//                fragmentCommunicator.passDataToFragment(mAdapter);
            }
        });

        mAdapter = new RecentTransAdapter(newList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        ViewGroup viewGroup = (ViewGroup) view;
                        View v1 = viewGroup.getChildAt(0);
                        View v2 = viewGroup.getChildAt(1);
                        View v3 = viewGroup.getChildAt(2);

                        Pair[] pair = new Pair[3];

                        pair[0] = new Pair<View, String>(v1, "timgTrans");
                        pair[1] = new Pair<View, String>(v2, "tdescTrans");
                        pair[2] = new Pair<View, String>(v3, "tamtTrans");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(TransactionsActivity.this, pair);
                        Intent intent = new Intent(TransactionsActivity.this, TransDetails.class);
                        Transaction tr = filteredList.get(position);

                        intent.putExtra("tamt", tr.amount);
                        intent.putExtra("tdesc", tr.desc);
                        intent.putExtra("tdateInMilli", tr.dateInMilli);
                        intent.putExtra("tmsg", tr.msg);
                        intent.putExtra("tdate",tr.date);

                        startActivity(intent, options.toBundle());
                    }
                })
        );


    searchFrame.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // searchtrans.requestFocus();
            searchtrans.onActionViewExpanded();
        }
    });


    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("2^^inResume", "yeah");
        mAdapter = new RecentTransAdapter(Transaction.transactionList, this);
        recyclerView.setAdapter(mAdapter);
    }

    public void getDataFromFrag(String someValue, RecentTransAdapter recentTransAdapter) {
        Log.d("2^^^yo", someValue);
        dateSelected = someValue;
        monthButton.setText(dateSelected);
        mAdapter = recentTransAdapter;
        final List<Transaction> list = new ArrayList<Transaction>();

        /*aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("2^^list is ", String.valueOf(mAdapter.getItemCount()));


                if (!isChecked) {
                    Log.d("notchecked", "yo");
    //                    mAdapter.getFilter(0).filter("");
                    Log.d("2^^testafdap is ", String.valueOf(testAdapter.getItemCount()));
                    recyclerView.setAdapter(testAdapter);
                } else {
                    for(int i=0;i<mAdapter.getItemCount();i++)
                    {
                        list.add(mAdapter.getItem(i));
                    }
                    testAdapter = new RecentTransAdapter(list,getApplicationContext()) ;
                    mAdapter.getFilter(1).filter("credit");
                    recyclerView.setAdapter(mAdapter);
                }

            }
        });
*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


}
