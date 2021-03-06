package com.mariostay.guest.mariostay;

import android.content.Intent;
//import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements BrowseFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

	//static final String KEY_SHARED_PREFERENCE = "com.mariostay.guest.mariostay.KEY_SHARED_PREF", KEY_USER_NAME = "com.mariostay.guest.mariostay.KEY_USER_NAME", KEY_EMAIL = "com.mariostay.guest.mariostay.KEY_EMAIL";
	//private final String KEY_LOGGED_IN = "com.mariostay.guest.mariostay.KEY_LOGGED_IN";
	final static String KEY_PROPERTY = "com.mariostay.guest.mariostay.KEY_PRPERTY",
        KEY_SEARCH_LOCATION = "com.mariostay.guest.mariostay.KEY_SEARCH_LOCATION",
        KEY_SEARCH_DATE = "com.mariostay.guest.mariostay.KEY_SEARCH_DATE";
	private final int REQUEST_LOGIN = 101;
	//private boolean userIsLoggedIn;
	private FragmentManager mFragmentManager = getSupportFragmentManager();
	//private SharedPreferences prefMan;
	private Toast mToast;
	private MenuItem prevMenuItem;

	private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		final ViewPager viewpager = findViewById(R.id.main_pager);
		setupViewPager(viewpager);

		final BottomNavigationView bNavView = findViewById(R.id.navigation);
		bNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				//if(!userIsLoggedIn) loginPrompt();
				if(mAuth.getCurrentUser() == null) loginPrompt();
				else switch(item.getItemId()) {
					case R.id.navigation_search:
						viewpager.setCurrentItem(0);
						break;
					/*case R.id.navigation_inbox:
						viewpager.setCurrentItem(1);
						break;
					case R.id.navigation_shortlist:
						viewpager.setCurrentItem(2);
						break;*/
					case R.id.navigation_booking:
						//viewpager.setCurrentItem(3);
						viewpager.setCurrentItem(1);
						break;
					case R.id.navigation_profile:
						//viewpager.setCurrentItem(4);
						viewpager.setCurrentItem(2);
						break;
				}
				return false;
			}
		});

		viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (prevMenuItem != null) {
					prevMenuItem.setChecked(false);
				}
				else
				{
					bNavView.getMenu().getItem(0).setChecked(false);
				}

				bNavView.getMenu().getItem(position).setChecked(true);
				prevMenuItem = bNavView.getMenu().getItem(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		mAuth = FirebaseAuth.getInstance();

		if(mAuth.getCurrentUser() == null) {
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivityForResult(loginIntent,REQUEST_LOGIN);
		}

		/*prefMan = getSharedPreferences(KEY_SHARED_PREFERENCE,MODE_PRIVATE);
		userIsLoggedIn = prefMan.getBoolean(KEY_LOGGED_IN,false);
		
		if(!userIsLoggedIn) {
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivityForResult(loginIntent,REQUEST_LOGIN);
		}*/

		mToast = new Toast(this);
    }

    private void setupViewPager(ViewPager v) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(mFragmentManager);
		adapter.addFragment(new BrowseFragment());
		adapter.addFragment(new Fragment());
		adapter.addFragment(new ProfileFragment());
		/*adapter.addFragment(new Fragment());
		adapter.addFragment(new Fragment());*/
		v.setAdapter(adapter);
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_search_property:
				startActivity(new Intent(this, BookingSearch.class));
				break;
			case R.id.menu_logout:
				/*prefMan.edit().putBoolean(KEY_LOGGED_IN,false).remove(KEY_USER_NAME).remove(KEY_EMAIL).apply();
				userIsLoggedIn = false;*/
				mAuth.signOut();
				Intent loginIntent = new Intent(this, LoginActivity.class);
				startActivityForResult(loginIntent,REQUEST_LOGIN);
				return true;
			/*case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;*/
		}
		return super.onOptionsItemSelected(item);
	}

	private void loginPrompt() {
		d("You are not logged in");
		startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null) return;
        String loc = intent.getStringExtra(KEY_SEARCH_LOCATION),
                dat = intent.getStringExtra(KEY_SEARCH_DATE);
        if(loc == null || dat == null) return;
        ;
    }

    private void d(String s) {
    	mToast.cancel();
    	mToast = Toast.makeText(this, s, Toast.LENGTH_LONG);
    	mToast.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case REQUEST_LOGIN:
				if(resultCode==RESULT_OK) {
					/*prefMan.edit().putBoolean(KEY_LOGGED_IN,true).apply();
					userIsLoggedIn=true;
					if(data !=null && !data.getBooleanExtra("GUEST", false)) d("Logged in");*/
					//mFragment = new BrowseFragment();
					//mFragmentManager.beginTransaction().replace(R.id.frame, mFragment).commit();
					FirebaseUser user = mAuth.getCurrentUser();
					if(user == null) finish();
				}
				else
					finish();
		}

	}

	public void onPropertyClicked(Bundle bundle) {
		Intent intent = new Intent(this, PropertyDetailsActivity.class);
		intent.putExtra(KEY_PROPERTY, bundle.getParcelable(KEY_PROPERTY));
		startActivity(intent);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}
