package com.tanay.thundercipher.leaveapplications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.CDATASection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class StudentUserActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ActionBar actionBar;

    static TextView fileApplicationFromDateTextView, fileApplicationToDateTextView;
    EditText fileApplicationPlaceEditText, fileApplicationPurposeEditText;
    Button fileApplicationButton, fileApplicationFromDateButton, fileApplicationToDateButton;

    static String fileApplicationFromDate, fileApplicationToDate;
    String fileApplicationName = "", fileApplicationRoll = "", userID = "", checkDate = "", currentDate = "";
    int counter = 0;

    public void fileApplication(String applicationName, String applicationRoll,
                                String applicationFrom, String applicationTo,
                                String applicationPlace, String applicationPurpose, String studentID,
                                boolean wardenApproval, boolean securityApproval)
    {
        //code to file the application
        counter++;
        HashMap<String, Object> applicationData = new HashMap<>();
        applicationData.put("Name", applicationName);
        applicationData.put("Roll Number", applicationRoll);
        applicationData.put("From Date", applicationFrom);
        applicationData.put("To Date", applicationTo);
        applicationData.put("Place", applicationPlace);
        applicationData.put("Purpose", applicationPurpose);
        applicationData.put("Student ID", studentID);
        applicationData.put("Warden Approval", wardenApproval);
        applicationData.put("Security Approval", securityApproval);

        database.getReference().child("Users").child(userID).child("Application").updateChildren(applicationData);

        Intent i = new Intent(getApplicationContext(), StudentApplicationStatusActivity.class);
        startActivity(i);
    }

    ValueEventListener valueEventListenerForStatusNavigation = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            if(snapshot.getKey().equals("To Date"))
            {
                checkDate = snapshot.getValue().toString();

                //code to compare dates and navigate to StudentApplicationStatusActivity if necessary
                if (currentDate.compareTo(checkDate) <= 0)
                {
                    Intent i = new Intent(getApplicationContext(), StudentApplicationStatusActivity.class);
                    startActivity(i);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home :
            {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }

            case R.id.menuActionLogout :
            {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(StudentUserActivity.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StudentUserActivity.this, MainActivity.class));

                return true;
            }

            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public static class DatePickerFragment1 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
        {
            fileApplicationFromDate = dayOfMonth + "/" + (month+1) + "/" + year;
            fileApplicationFromDateTextView.setText(fileApplicationFromDate);
        }
    }

    public static class DatePickerFragment2 extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public DatePickerDialog onCreateDialog(Bundle savedInstanceState)
        {
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
        {
            fileApplicationToDate = dayOfMonth + "/" + (month+1) + "/" + year;
            fileApplicationToDateTextView.setText(fileApplicationToDate);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_user);

        fileApplicationFromDateTextView = (TextView)findViewById(R.id.fileApplicationFromDateTextView);
        fileApplicationToDateTextView = (TextView)findViewById(R.id.fileApplicationToDateTextView);
        fileApplicationPlaceEditText = (EditText)findViewById(R.id.fileApplicationPlaceEditText);
        fileApplicationPurposeEditText = (EditText)findViewById(R.id.fileApplicationPurposeEditText);
        fileApplicationButton = (Button)findViewById(R.id.fileApplicationButton);
        fileApplicationFromDateButton = (Button)findViewById(R.id.fileApplicationFromDateButton);
        fileApplicationToDateButton = (Button)findViewById(R.id.fileApplicationToDateButton);

        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userID = auth.getCurrentUser().getUid();
        reference = database.getReference().child("Users").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot snap : snapshot.getChildren())
                {
                    if(snap.getKey().equals("Name"))
                    {
                        fileApplicationName = snap.getValue().toString();
                    }

                    else if(snap.getKey().equals("Roll Number"))
                    {
                        fileApplicationRoll = snap.getValue().toString();
                    }

                    else if(snap.getKey().equals("Application"))
                    {
                        database.getReference().child("Users").child(userID).child("Application").addListenerForSingleValueEvent(valueEventListenerForStatusNavigation);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getApplicationContext(), "Failed to retrieve information!", Toast.LENGTH_SHORT).show();
            }
        });

        fileApplicationFromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DialogFragment datePicker = new DatePickerFragment1();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        fileApplicationToDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DialogFragment datePicker = new DatePickerFragment2();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });


        fileApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                fileApplication(fileApplicationName, fileApplicationRoll,
                        fileApplicationFromDateTextView.getText().toString(),
                        fileApplicationToDateTextView.getText().toString(),
                        fileApplicationPlaceEditText.getText().toString(),
                        fileApplicationPurposeEditText.getText().toString(),
                        userID, false, false);
            }
        });

        //toolbar and navigation drawer
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Application Authorizer");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = (DrawerLayout)findViewById(R.id.studentDrawerLayout);
        navigationView = (NavigationView)findViewById(R.id.studentNavigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.navigationProfile:
                    {
                        item.setChecked(true);

                        Intent i = new Intent(getApplicationContext(), StudentProfileActivity.class);
                        startActivity(i);

                        drawerLayout.closeDrawers();
                        return true;
                    }


                    case R.id.navigationProfilePic:
                    {
                        item.setChecked(true);

                        Intent i = new Intent(getApplicationContext(), StudentProfileActivity.class);
                        startActivity(i);

                        drawerLayout.closeDrawers();
                        return true;
                    }

                    case R.id.navigationContactInfo:
                    {
                        item.setChecked(true);

                        Intent i = new Intent(getApplicationContext(), WardenListActivity.class);
                        startActivity(i);

                        drawerLayout.closeDrawers();
                        return true;
                    }
                }

                return false;
            }
        });
    }
}