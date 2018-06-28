package com.mariostay.guest.mariostay;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class PropertyDetailsActivity extends AppCompatActivity {

    private final String KEY_PROPERTY = "com.mariostay.guest.mariostay.KEY_PRPERTY";
    private MenuItem favicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        Toolbar toolbar = findViewById(R.id.property_details_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Property property = getIntent().getParcelableExtra(KEY_PROPERTY);
        actionBar.setTitle(property.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_property_details, menu);
        favicon = menu.findItem(R.id.menu_property_fav);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_property_share:

                return true;
            case R.id.menu_property_fav:
                if(getString(R.string.menu_property_fav_no).equals(favicon.getTitle().toString())) {
                    favicon.setTitle(R.string.menu_property_fav);
                    favicon.setIcon(R.drawable.ic_favorite_white_24dp);
                }
                else {
                    favicon.setTitle(R.string.menu_property_fav_no);
                    favicon.setIcon(R.drawable.ic_favorite_border_white_24dp);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
