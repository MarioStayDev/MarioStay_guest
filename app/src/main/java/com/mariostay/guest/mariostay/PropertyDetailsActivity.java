package com.mariostay.guest.mariostay;

import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class PropertyDetailsActivity extends AppCompatActivity {

    private final String KEY_PROPERTY = "com.mariostay.guest.mariostay.KEY_PRPERTY";
    private MenuItem favicon;
    private Property property;
    @BindViews({R.id.chip1, R.id.chip2, R.id.chip3, R.id.chip4, R.id.chip5, R.id.chip6, R.id.chip7, R.id.chip8, R.id.chip9, R.id.chip10, R.id.chip11, R.id.chip12, R.id.chip13, R.id.chip14, R.id.chip15})
    List<ImageView> chips;
    @BindView(R.id.property_details_toolbar) Toolbar toolbar;
    @BindView(R.id.property_details_desc) TextView desc;
    @BindView(R.id.property_details_notice_period) TextView notice;
    @BindView(R.id.property_details_min_stay) TextView stay;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        ButterKnife.bind(this);
        mToast = new Toast(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        property = getIntent().getParcelableExtra(KEY_PROPERTY);
        actionBar.setTitle(property.getName());
        init();
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

    private void init () {
        desc.setText(property.getShortDescription());
        Resources resources = getResources();
        int i = property.getNoticePeriod();
        notice.setText(resources.getQuantityString(R.plurals.property_details_notice_period, i, i));
        i = property.getMinStayTime();
        stay.setText(resources.getQuantityString(R.plurals.property_details_stay_time, i, i));
        i = 0;
        Map<String, Boolean> iMap = property.getAmenities();
        for(String s : iMap.keySet()) {
            if(!iMap.get(s)) chips.get(i).setVisibility(View.GONE);
            i++;
        }
    }

    private void d(String s) {
        mToast.cancel();
        mToast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        mToast.show();
    }
}
