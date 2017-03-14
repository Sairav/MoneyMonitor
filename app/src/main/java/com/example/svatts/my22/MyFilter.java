package com.example.svatts.my22;

import android.util.Log;
import android.widget.Filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by svatts on 23-Dec-16.
 */
class MyFilter extends Filter {

    private final RecentTransAdapter adapter;

    private final List<Transaction> originalList;

    private final List<Transaction> filteredList;

    public MyFilter(RecentTransAdapter adapter, List<Transaction> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        Log.d("constrianss iss", constraint.toString());

        String constring = constraint.toString();

        if (constraint.length() == 0) {
            Log.d("Adll", "yoyo");
            Log.d("origLen", String.valueOf(originalList.size()));

            filteredList.addAll(originalList);
            Log.d("filteredLen", String.valueOf(filteredList.size()));
        } else if (constring.equals("credit")) {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            for (final Transaction tr : originalList) {

                /*  if (tr.desc.startsWith(filterPattern)) {
                      Log.d("adding to list",filterPattern);
                      filteredList.add(tr);
                  }*/

                if (filterPattern.equals("credit") && tr.type.toLowerCase().contains("credit")) {
                    Log.d("FilterSucc", tr.desc + " : " + tr.date);
                    filteredList.add(tr);
                }
            }
        } else if (constraint.toString().trim().toLowerCase().matches("[a-z]{3}[-][0-9]{2}")) {
            StringBuilder sb = new StringBuilder("");
            sb.append("11-").append(constring);

            Log.d("dubug1", sb.toString());

            for (final Transaction tr : originalList) {

                Date date = null;
                try {
                    date = new SimpleDateFormat("dd-MMM-yy").parse(sb.toString());
                    Log.d("dubug2", date.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);
               /* String monthStr = null;
                monthStr = String.valueOf(month);
                if (String.valueOf(month).length() == 1) {
                    monthStr = "0" + month;
                }
                Log.d("dubug3", "" + month);
                Log.d("dubug4", "" + year);

                Log.d("debubggdate", tr.date);


                if (tr.date.trim().startsWith("" + year + "-" + monthStr)) {
                    filteredList.add(tr);
                    Log.d("TransAdd","Success");
                    //  TransactionsActivity.mAdapter.setList(filteredList);
                }
                else
                {
                    Log.d("TransAdd","No");
                }
*/

                //////for sqlite

                String monthStr = null;
                monthStr = String.valueOf(month);
                if (String.valueOf(month).length() == 1) {
                    monthStr = "0" + month;
                }
                Log.d("dubug3", "" + month);
                Log.d("dubug4", "" + year);

                Log.d("debubggdate", tr.date);
                Date date2=null;
                try {
                    date2  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(tr.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String datee = new SimpleDateFormat("yyyy-MM-dd").format(date2);

                Log.d("debubggdate22", datee);

                if (datee.trim().startsWith("" + year + "-" + monthStr)) {
                    filteredList.add(tr);
                    Log.d("TransAdd","Success");
                    //  TransactionsActivity.mAdapter.setList(filteredList);
                }
                else
                {
                    Log.d("TransAdd","No");
                }
                //////for sqlite

            }
        } else {
            for (final Transaction tr : originalList) {
                if (tr.desc.toLowerCase().contains(constraint.toString())) {
                    filteredList.add(tr);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // adapter.filteredTransList.clear();
        adapter.setList(filteredList);
        adapter.notifyDataSetChanged();
        TransactionsActivity.filteredList=filteredList;
    }
}