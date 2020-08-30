package com.tanay.thundercipher.leaveapplications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SecurityUserActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth auth;
    FirebaseDatabase database;

    ArrayList<String> pendingStudentID;
    ArrayList<String> pendingStudentName;
    ArrayList<String> pendingStudentRoll;
    ArrayList<String> pendingFromDate;
    ArrayList<String> pendingToDate;
    ArrayList<String> pendingPlace;
    ArrayList<String> pendingPurpose;

    String reviewMode = "Security";

    RecyclerView securityPendingApplicationsRecyclerView;
    RecyclerView.LayoutManager securityPendingApplicationsRecyclerViewManager;
    PendingApplicationsRecyclerAdapter securityPendingApplicationsAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            /*case android.R.id.home :
            {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
             */

            case R.id.menuActionLogout :
            {
                auth.signOut();
                Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SecurityUserActivity.this, MainActivity.class));

                return true;
            }

            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_user);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        pendingStudentID = new ArrayList<String>();
        pendingStudentName = new ArrayList<String>();
        pendingStudentRoll = new ArrayList<String>();
        pendingFromDate = new ArrayList<String>();
        pendingToDate = new ArrayList<String>();
        pendingPlace = new ArrayList<String>();
        pendingPurpose = new ArrayList<String>();

        securityPendingApplicationsRecyclerView = (RecyclerView)findViewById(R.id.securityPendingApplicationsRecyclerView);
        securityPendingApplicationsRecyclerViewManager = new LinearLayoutManager(this);
        securityPendingApplicationsRecyclerView.setLayoutManager(securityPendingApplicationsRecyclerViewManager);

        //add code to store application data in arrayList and then, after notifyDataSetChanged() , write the following code inside the valueEventListener's onSuccess itself:
        securityPendingApplicationsAdapter = new PendingApplicationsRecyclerAdapter(pendingStudentID, pendingStudentName,
                                                                                    pendingStudentRoll, pendingFromDate,
                                                                                    pendingToDate, pendingPlace, pendingPurpose,
                                                                                    reviewMode, this);
        //setHasFixedSize() would be false here
        securityPendingApplicationsRecyclerView.setAdapter(securityPendingApplicationsAdapter);

        toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        /*ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawerLayout = (DrawerLayout)findViewById(R.id.securityDrawerLayout);
        navigationView = (NavigationView)findViewById(R.id.securityNavigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.navigationProfile:
                    {
                        item.setChecked(true);
                        //add code
                        drawerLayout.closeDrawers();
                        return true;
                    }


                    case R.id.navigationProfilePic:
                    {
                        item.setChecked(true);
                        //add code
                        drawerLayout.closeDrawers();
                        return true;
                    }

                    case R.id.navigationContactInfo:
                    {
                        item.setChecked(true);
                        //add code
                        drawerLayout.closeDrawers();
                        return true;
                    }
                }

                return false;
            }
        });
         */
    }
}