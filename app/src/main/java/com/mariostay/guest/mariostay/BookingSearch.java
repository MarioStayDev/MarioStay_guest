package com.mariostay.guest.mariostay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingSearch extends AppCompatActivity {
    @BindView(R.id.booking_search_toolbar) Toolbar toolbar;
    @BindView(R.id.booking_search_location_input) AutoCompleteTextView loc;
    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        loc.setAdapter(adapter);
    }
}
