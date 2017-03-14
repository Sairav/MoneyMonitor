package com.example.svatts.my22;

/**
 * Created by svatts on 27-Feb-17.
 */
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by jahid on 12/10/15.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        TextView tv1= (TextView) getActivity().findViewById(R.id.setDate);
        String monthIs = String.valueOf(view.getMonth() + 1);

        String  dday  = String.valueOf(view.getDayOfMonth());

        if(String.valueOf(view.getDayOfMonth()).length()==1)
        {
            dday  = "0"+view.getDayOfMonth();
        }
        if(monthIs.length()==1)
        {
            monthIs = "0"+monthIs;
        }

        tv1.setText(dday+"/"+monthIs+"/"+view.getYear());
        //new AddTrans().showTimePickerDialog(AddTrans.dateView);

        //AddTrans.dateOfTrans = tv1.getText().toString();

    }
}