package com.example.gafandfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//this class is to take the reminders from the user and inserts into the database
public class ReminderDetailsActivity extends AppCompatActivity {

    Button mSubmitbtn, mDatebtn, mTimebtn;
    EditText mTitledit;
    String timeTonotify;
    ImageView deleteReminderImageViewBtn;
    TextView pageTitleTextView;
    String title, date, time, docId;
    boolean isEditMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        mTitledit = (EditText) findViewById(R.id.editTitle);
        mDatebtn = (Button) findViewById(R.id.btnDate);                                             //assigned all the material reference to get and set data
        mTimebtn = (Button) findViewById(R.id.btnTime);
        mSubmitbtn = (Button) findViewById(R.id.btnSubmit);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteReminderImageViewBtn = findViewById(R.id.delete_reminder_img_view);

        // receive data
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        docId = getIntent().getStringExtra("docId");

        if (docId!=null && !docId.isEmpty()){
            isEditMode = true;
            deleteReminderImageViewBtn.setVisibility(View.GONE);
        }

        if (isEditMode){
            pageTitleTextView.setText("Edit your reminder");
            deleteReminderImageViewBtn.setVisibility(View.VISIBLE);
        }
        mTitledit.setText(title);
        mDatebtn.setText(date);
        mTimebtn.setText(time);

        deleteReminderImageViewBtn.setOnClickListener((v)-> deleteReminderFromFirebase());


        mTimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();                                                                       //when we click on the choose time button it calls the select time method
            }
        });

        mDatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }                                        //when we click on the choose date button it calls the select date method
        });

        mSubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitledit.getText().toString().trim();                               //access the data from the input field
                String date = mDatebtn.getText().toString().trim();                                 //access the date from the choose date button
                String time = mTimebtn.getText().toString().trim();                                 //access the time from the choose time button

                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter text", Toast.LENGTH_SHORT).show();   //shows the toast if input field is empty
                } else {
                    if (time.equals("time") || date.equals("date")) {                                               //shows toast if date and time are not selected
                        Toast.makeText(getApplicationContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
                    } else {
                        processinsert(title, date, time);

                    }
                }
                Note note = new Note();
                note.setTitle(title);
                note.setDate(date);
                note.setTime(time);
                note.setTimestamp(Timestamp.now());
                saveReminderToFirebase(note);
            }
        });
    }

    /************************************************/
    void saveReminderToFirebase(Note note){
        DocumentReference documentReference;
        if (isEditMode){
            // update the reminder
            documentReference = Utility.getCollectionReferenceForReminders().document(docId);
        } else{
            //create new reminder
            documentReference = Utility.getCollectionReferenceForReminders().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // reminder is added
                    finish();
                } else{
                    Utility.showToast(ReminderDetailsActivity.this, "Failed while adding reminder");
                }
            }
        });
    }

    void deleteReminderFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForReminders().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // reminder is deleted
                    Utility.showToast(ReminderDetailsActivity.this, "Reminder deleted successfully");
                    finish();
                } else{
                    Utility.showToast(ReminderDetailsActivity.this, "Failed while deleting reminder");
                }
            }
        });
    }
    /************************************************/


    private void processinsert(String title, String date, String time) {
        String result = new dbManager(this).addreminder(title, date, time);                  //inserts the title,date,time into sql lite database
        setAlarm(title, date, time);                                                                //calls the set alarm method to set alarm
        mTitledit.setText("");

    }

    private void selectTime() {                                                                     //this method performs the time picker task
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;                                                        //temp variable to store the time to set alarm
                mTimebtn.setText(FormatTime(i, i1));                                                //sets the button text as selected time
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private void selectDate() {                                                                     //this method performs the date picker task
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mDatebtn.setText(day + "/" + (month + 1) + "/" + year);                             //sets the selected date as test for button
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {                                                //this method converts the time into 12hr format and assigns am or pm

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }


    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   //assigning alarm manager object to set alarm

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event", text);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d/M/yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Reminder successfully added", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intentBack = new Intent(getApplicationContext(), ReminderActivity.class);                //this intent will be called once the setting alarm is complete
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);                                                                  //navigates from adding reminder activity to mainactivity

    }
}