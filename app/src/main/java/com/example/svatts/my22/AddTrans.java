package com.example.svatts.my22;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by svatts on 30-Dec-16.
 */

public class AddTrans extends AppCompatActivity {
    Toolbar mActionBarToolbar;
    public static EditText dateSet, timeSet, amountSet, descSet;
    public boolean isError;
    public static Button submitDetails;
    Switch ttypeSwitch;
    String ttypeStr = "Debit";
    TextInputLayout amountTIL, descTIL;
    public String tamt, tdesc, tdate, ttime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_trans);

        amountTIL = (TextInputLayout) findViewById(R.id.amountt);
        descTIL = (TextInputLayout) findViewById(R.id.descc);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbarAddTrans);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Add Transaction");

        ttypeSwitch = (Switch) findViewById(R.id.ttypeSwitch);

        ttypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    ttypeStr = "Credit";

                else
                    ttypeStr = "Debit";
            }
        });

        descSet = (EditText) findViewById(R.id.setDesc);
        descSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().length() == 0) {
                    descTIL.setError("Desc cannot be empty");
                    isError = true;
                } else
                    descTIL.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    descTIL.setError("Desc cannot be empty");
                    isError = true;
                } else
                    descTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    descTIL.setError("Desc cannot be empty");
                    isError = true;
                } else
                    descTIL.setErrorEnabled(false);
            }
        });

        amountSet = (EditText) findViewById(R.id.setAmount);
        amountSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().length() == 0) {
                    amountTIL.setError("Amount cannot be empty");
                    isError = true;
                } else
                    amountTIL.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    amountTIL.setError("Amount cannot be empty");
                    isError = true;
                } else
                    amountTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    amountTIL.setError("Amount cannot be empty");
                    isError = true;
                } else
                    amountTIL.setErrorEnabled(false);
            }
        });
        submitDetails = (Button) findViewById(R.id.submitTrans);
        dateSet = (EditText) findViewById(R.id.setDate);
        timeSet = (EditText) findViewById(R.id.setTime);
        timeSet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showTimePickerDialog(v);
            }
        });
        dateSet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showDatePickerDialog(v);
            }
        });

        if (isError) {
            submitDetails.setEnabled(false);
        }
        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddTrans.this, "clickc brah", Toast.LENGTH_SHORT).show();

                tamt = amountSet.getText().toString();
                tdesc = descSet.getText().toString();
                tdate = dateSet.getText().toString();
                ttime = timeSet.getText().toString();

                Transaction tr = new Transaction(AddTrans.this);
                tr.amount = tamt ;
                tr.desc = tdesc;
                tr.date = tdate + " " + ttime;
                Toast.makeText(AddTrans.this,tr.date,Toast.LENGTH_SHORT).show();

                if(tr.msg==null)
                {
                    tr.msg = "Manually-Set-Trans"+tr.desc+tr.date+tr.amount+tr.type ;
                }

                Toast.makeText(AddTrans.this,tr.msg,Toast.LENGTH_SHORT).show();

                //new DatabaseHandler(AddTrans.this).setTransaction(tr);

            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");


    }
}
