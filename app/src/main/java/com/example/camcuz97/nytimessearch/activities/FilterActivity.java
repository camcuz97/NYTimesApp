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
        // Gets filter from SearchActivity
        filter = Parcels.unwrap(getIntent().getParcelableExtra("filter"));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.strings_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //Sets up checkboxes and textfields
        setViews();
        //sets click listener
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                day = cal.get(Calendar.DATE);
                month = cal.get(Calendar.MONTH);
                //creates date picker
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

    private void setViews(){
        spinner.setSelection((filter.getSort().equals("Newest")) ? 0 : 1);
        cbArts.setChecked((filter.isArts()));
        cbSports.setChecked((filter.isSports()));
        cbStyle.setChecked((filter.isStyle()));
        etDate.setText(filter.getUnchangedDate());
    }

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
            //sets filters boolean vals accordingly
            case R.id.cbArts:
                filter.setArts(checked);
                break;
            case R.id.cbStyle:
                filter.setStyle(checked);
                break;
            case R.id.cbSports:
                filter.setSports(checked);
        }
    }

    public void onSubmit(View view){
        //setting filter's values
        filter.setSort(spinner.getSelectedItem().toString());
        Intent data = new Intent();
        filter.setUnchangedDate(etDate.getText().toString());
        filter.manipulateDate();
        //send filter back and end activity
        data.putExtra("filter", Parcels.wrap(filter));
        setResult(RESULT_OK, data);
        finish();
    }
}

