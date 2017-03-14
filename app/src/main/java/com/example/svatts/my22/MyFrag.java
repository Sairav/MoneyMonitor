package com.example.svatts.my22;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by svatts on 22-Dec-16.
 */

public class MyFrag extends DialogFragment {
    //interface via which we communicate to hosting Activity
    //private ActivityCommunicator activityCommunicator;
    RecentTransAdapter mAdapter;
    Button[] button ;
    String buttonText = null;
    String selectedYear = null ;
    TextView yearSelected = null ;
    ImageButton prevYear , nextYear ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.firstfrag, container,
                false);
        getDialog().setTitle("DialogFragment Tutorial");

        prevYear = (ImageButton)rootView.findViewById(R.id.prevYear);
        nextYear = (ImageButton)rootView.findViewById(R.id.nextYear);
        yearSelected = (TextView)rootView.findViewById(R.id.yearSelected);
        Date today = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(today);
        int currYear = cal.get(Calendar.YEAR);
        yearSelected.setText(String.valueOf(currYear));

        prevYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearSelected.setText(String.valueOf(Integer.parseInt(yearSelected.getText().toString())-1));
                selectedYear = yearSelected.getText().toString();
            }
        });

        nextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearSelected.setText(String.valueOf(Integer.parseInt(yearSelected.getText().toString())+1));
                selectedYear = yearSelected.getText().toString();
            }
        });

        button = new Button[12];
        String[] months = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};

        selectedYear = yearSelected.getText().toString();

        for (int i = 0; i <= 11; i++) {
            button[i] = (Button) rootView.findViewById(getResources().getIdentifier(months[i], "id", getActivity().getPackageName()));

        }
            // getDialog().setCanceledOnTouchOutside(true);
        // Do something else
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        for (int i = 0; i <= 11; i++)
        {
            final int integer = i ;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonText = button[integer].getText().toString();
                    Log.d("butText1111"+integer,buttonText);
                    Log.d("sel year..11",selectedYear);
                    //activityCommunicator.passDataToActivity(buttonText+"-"+selectedYear.substring(2),mAdapter);
                    mAdapter.getFilter().filter(buttonText+"-"+selectedYear.substring(2));
                    //activityCommunicator.passDataToActivity(buttonText+"-"+selectedYear.substring(2),mAdapter);
                    ((TransactionsActivity)getActivity()).getDataFromFrag(buttonText+"-"+selectedYear.substring(2),mAdapter);
                    dismiss();
                }
            });
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.detailDialogAnimation;
        return dialog;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = 720; // specify a value here
        int dialogHeight = 600 ; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        // ... other stuff you want to do in your onStart() method
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        activityCommunicator =(ActivityCommunicator)context;
      //  ((TransactionsActivity)context).fragmentCommunicator = this ;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = TransactionsActivity.mAdapter ;
    }
}
