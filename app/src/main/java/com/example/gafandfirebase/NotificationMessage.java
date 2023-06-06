package com.example.gafandfirebase;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//this class creates the Reminder Notification Message

public class NotificationMessage extends AppCompatActivity {
    TextView textView1, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        textView1 = findViewById(R.id.message);
        Bundle bundle = getIntent().getExtras();                                                    //call the data which is passed by another intent
        textView1.setText(bundle.getString("message"));

    }
}