package com.example.camcuz97.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.camcuz97.nytimessearch.Filters;
import com.example.camcuz97.nytimessearch.R;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    //binding all views
    @BindView(R.id.spSort) Spinner spinner;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbStyle) CheckBox cbStyle;
    @BindView(R.id.cbSports) CheckBox cbSports;
    @BindView(R.id.etDate) EditText etDate;
    Calendar cal;
    int month; int day; int year;
    Filters filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        filter = Parcels.unwrap(getIntent().getParcelableExtra("filter"));
        //spinner = (Spinner) findViewById(R.id.spSort);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.strings_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        if(filter.getSort().equals("Newest")){
            spinner.setSelection(0);
        }
        else{
            spinner.setSelection(1);
        }
        if(filter.isArts()){cbArts.setChecked(true);}
        if(filter.isSports()){cbSports.setChecked(true);}
        if(filter.isStyle()){cbStyle.setChecked(true);}
        if(!filter.getBegin().equals("")){etDate.setText(filter.getUnchangedDate());}
        //etDate = (EditText) findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                day = cal.get(Calendar.DATE);
                month = cal.get(Calendar.MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int currYear, int monthOfYear, int dayOfMonth) {
                        etDate.setText(currYear + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                    }
                }, year,month, day);
                datePicker.setTitle("Select Date");
                datePicker.show();
            }
        });
    }
//    // attach to an onclick handler to show the date picker
//    public void showDatePickerDialog(View v) {
//        DatePickerFragment newFragment = new DatePickerFragment();
//        newFragment.show(getFragmentManager(), "datePicker");
//    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbArts:
                if (checked){
                    filter.setArts(true);
                }
                else{
                    filter.setArts(false);
                }
                break;
            case R.id.cbStyle:
                if (checked){
                    filter.setStyle(true);
                }
                else{
                    filter.setStyle(false);
                }
                break;
            case R.id.cbSports:
                if (checked){
                    filter.setSports(true);
                }
                else{
                    filter.setSports(false);
                }
        }
    }

    public void onSubmit(View view){
        filter.setSort(spinner.getSelectedItem().toString());
        Intent data = new Intent();
        //data.putExtra("sort", spSort);
        filter.setUnchangedDate(etDate.getText().toString());
        filter.manipulateDate();
        //data.putExtra("date", date);
        data.putExtra("filter", Parcels.wrap(filter));
        setResult(RESULT_OK, data);
        finish();
    }
}

