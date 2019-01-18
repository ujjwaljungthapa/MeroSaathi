package com.electronics.invento.merosaathi;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Fragment fragment = null;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton ignite_search = findViewById(R.id.ignite_search);  // fab replaced with ignite_search
        ignite_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Open path", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        NavigationView navigationView1 = findViewById(R.id.nav_view1);
        navigationView.setItemIconTintList(null);   //needed to add this in order to use png in item tag of navigation drawer
        navigationView1.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView1.setNavigationItemSelectedListener(this);

        //for state change i.e. onPause due to screen rotation, background gone.... app will reset so to prevent it(i.e. reset)
        if (savedInstanceState == null) {
            /*
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ftransaction = fm.beginTransaction();
                ftransaction.replace(R.id.fragment_container, new HomeFragment());
                ftransaction.addToBackStack(null);
                ftransaction.commit();
            */
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (count != 0) {
            //RAKHE PANI NARAKHEY PANI ITS WORKING. SO ACTUALLY NOT NEEDED
            getSupportFragmentManager().popBackStack();
        } else {
           /* if (checkNavigationMenuItem() !=0){
                navigationView.setCheckedItem(R.id.nav_home);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }*/
            //else{
            super.onBackPressed();  //close navigation drawer
            //}
        }
    }

    //USED TO RETURN TO HOMEFRAGMENT ON BACKPRESSED ABOVE
    /*private int checkNavigationMenuItem() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked())
                return i;
        }
        return -1;
    }*/

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String text;

        if (id == R.id.nav_home) {
            // CREATE A NEW FRAGMENT
            fragment = new HomeFragment();
        } else if (id == R.id.nav_path) {
            fragment = new PathFragment();
        } else if (id == R.id.nav_transport) {
            fragment = new TransportFragment();
        } else if (id == R.id.nav_information) {
            fragment = new InformationFragment();
        } else if (id == R.id.nav_share) {
            //this link is for test
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_contact_us) {

        } else if (id == R.id.sudeep) {
            fragment = new ChatFragment();
            text = "Chat with Sudeep Rayamajhi";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.jiv) {
            fragment = new ChatFragment();
            text = "Chat with Jiv Raj Gurung";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nippon) {
            fragment = new ChatFragment();
            text = "Chat with Nippon Maharjan";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.bishal) {
            fragment = new ChatFragment();
            text = "Sorry not online!";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.arjun) {
            fragment = new ChatFragment();
            text = "Sorry not online!";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.sandip) {
            fragment = new ChatFragment();
            text = "Sorry not online!";
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }

        if (fragment != null) {
            //CREATE A TRANSACTION THROUGH FRAGMENTMANAGER
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            //REPLACE WHATEVER IS IN THE "fragment_container" VIEW WITH THIS FRAGMENT
            ft.replace(R.id.fragment_container, fragment);

            //"khoi k vannu aba. yo rakhda communicate and contact us item click garda ni return to
            // home fragment possible vayo using checkNavigationMenuItem() but addToBackStack ko kaam vayena"
            ft.addToBackStack(null);

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            //COMMIT THE TRANSACTION
            ft.commit();
        }

        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
