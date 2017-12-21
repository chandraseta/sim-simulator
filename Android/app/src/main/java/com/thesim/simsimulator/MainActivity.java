package com.thesim.simsimulator;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thesim.simsimulator.model.User;
import com.thesim.simsimulator.utils.Helper;

import java.util.Locale;

import static com.thesim.simsimulator.AuthActivity.firebaseAuth;
import static com.thesim.simsimulator.AuthActivity.firebaseUser;


/**
 * MainActivity contains the main menu for SIM Simulator.
 */
public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private final String TAG = this.getClass().getSimpleName();
  private FragmentManager fragmentManager;

  public DrawerLayout drawerLayout;

  BroadcastReceiver locationReceiver;
  IntentFilter filter;

  private TextView tvName;
  private TextView tvEmail;
  private ImageView ivLogo;

  public double latitude;
  public double longitude;

  boolean darkThemeActivated;
  private boolean backPressedOnce = false;
  private Handler handler = new Handler();

  private final Runnable runnable = new Runnable() {
    @Override
    public void run() {
      backPressedOnce = false;
    }
  };

  String jsonData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    darkThemeActivated = false;

    fragmentManager = this.getSupportFragmentManager();

    if (savedInstanceState == null) {
      fragmentManager.beginTransaction()
          .replace(R.id.frame, new MenuFragment(), "MenuFragment")
          .commit();
      firebaseAuth = FirebaseAuth.getInstance();
      firebaseUser = firebaseAuth.getCurrentUser();
    }

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    Menu menu = navigationView.getMenu();

    MenuItem map = menu.findItem(R.id.nav_item_map);
    SpannableString s1 = new SpannableString(map.getTitle());
    s1.setSpan(new TextAppearanceSpan(this, R.style.TextStyle_NavBar), 0, s1.length(), 0);
    map.setTitle(s1);

    MenuItem account = menu.findItem(R.id.nav_item_account);
    SpannableString s2 = new SpannableString(account.getTitle());
    s2.setSpan(new TextAppearanceSpan(this, R.style.TextStyle_NavBar), 0, s2.length(), 0);
    account.setTitle(s2);

    navigationView.setNavigationItemSelectedListener(this);

    final View headerView = navigationView.getHeaderView(0);
    tvName = (TextView) headerView.findViewById(R.id.nav_header_name);
    tvEmail = (TextView) headerView.findViewById(R.id.nav_header_email);
    ivLogo = (ImageView) headerView.findViewById(R.id.nav_header_logo);

    ivLogo.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (darkThemeActivated) {
          darkThemeActivated = false;
          navigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
          navigationView.setItemTextColor(getColorStateList(R.color.fontDark));
          Toast.makeText(getBaseContext(), "Normal theme activated", Toast.LENGTH_SHORT).show();
        } else {
          darkThemeActivated = true;
          navigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
          navigationView.setItemTextColor(getColorStateList(R.color.fontLight));
          Toast.makeText(getBaseContext(), "Dark mode for navigation bar activated", Toast.LENGTH_SHORT).show();
        }
        return true;
      }
    });

    Log.d(TAG, "Check firebaseUser");
    if (firebaseUser == null) {
      Toast.makeText(getBaseContext(), "Please sign in to continue using SIM Simulator", Toast.LENGTH_LONG).show();
      Intent intent = new Intent(this, AuthActivity.class);
      startActivity(intent);
      finish();
    } else {
      tvName.setText(firebaseUser.getDisplayName());
      tvEmail.setText(firebaseUser.getEmail());
      User.User(firebaseUser.getUid(), firebaseUser.getEmail());
    }

    Log.d(TAG, "Check Permission");
    if (!Helper.checkLocationPermission(this)) {
      Log.d(TAG, "Check Permission failed");
      showPermissionDialog();
    }

    startService(new Intent(this, LocationService.class));

    locationReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String stringLatitude = intent.getStringExtra("lat");
        String stringLongitude = intent.getStringExtra("long");
        latitude = Double.parseDouble(stringLatitude);
        longitude = Double.parseDouble(stringLongitude);
        Log.d(TAG, "BroadcastReceived latitude " + latitude + " longitude" + longitude);
      }
    };
    filter = new IntentFilter("location");
    registerReceiver(locationReceiver, filter);

    try {
      getSupportActionBar().hide();
    } catch (Exception ex) {
      Log.e(TAG, "Hide SupportActionBar failed");
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    registerReceiver(locationReceiver, filter);
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onDestroy() {
    unregisterReceiver(locationReceiver);
    if (handler != null) {
      handler.removeCallbacks(runnable);
    }
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      if (backPressedOnce) {
        super.onBackPressed();
        return;
      }
      backPressedOnce = true;
      Toast.makeText(this, "Press back again to leave", Toast.LENGTH_SHORT).show();

      handler.postDelayed(runnable, 2000);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_start) {
      Intent intent = new Intent(this, TestActivity.class);
      intent.putExtra("data", jsonData);
      startActivity(intent);
    } else if (id == R.id.nav_options) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
    } else if (id == R.id.nav_help) {
      setTitle("Help");
      HelpFragment fragment = new HelpFragment();
      fragmentManager.beginTransaction()
          .setCustomAnimations(R.anim.right_enter, R.anim.left_exit)
          .replace(R.id.frame, fragment, "HelpFragment")
          .commit();
    } else if (id == R.id.nav_nearest_police) {
      String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude).concat("?q=kantor%20polisi");
      Uri locationUri = Uri.parse(uri);
      Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);
      mapIntent.setPackage("com.google.android.apps.maps");
      Log.d(TAG, "MainActivity open gmap with coordinate " + latitude + ", " + longitude);
      if (mapIntent.resolveActivity(getPackageManager()) != null) {
        startActivity(mapIntent);
      }
    } else if (id == R.id.nav_profile) {
      ProfileFragment fragment = new ProfileFragment();
      fragmentManager.beginTransaction()
          .setCustomAnimations(R.anim.right_enter, R.anim.left_exit)
          .replace(R.id.frame, fragment, "ProfileFragment")
          .commit();
    } else if (id == R.id.nav_sign_out) {
      firebaseAuth.signOut();
      Toast.makeText(getBaseContext(), "Signed out", Toast.LENGTH_LONG).show();
      Intent intent = new Intent(this, AuthActivity.class);
      startActivity(intent);
      finish();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void showPermissionDialog() {
    ActivityCompat.requestPermissions(
        this,
        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
  }
}
