package com.example.gafandfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class ReminderActivity extends AppCompatActivity {

    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        menuBtn = findViewById(R.id.menu_btn);

        menuBtn.setOnClickListener((v)-> showMenu());

    }

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(ReminderActivity.this, menuBtn);
        popupMenu.getMenu().add("My notes");
        popupMenu.getMenu().add("My account");
        popupMenu.getMenu().add("About us");
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ReminderActivity.this, LoginActivity.class));
                    finish();
                    return true;
                } else if (menuItem.getTitle()=="My account"){
                    startActivity(new Intent(ReminderActivity.this, AccountActivity.class));
                    return true;
                } else if (menuItem.getTitle()=="My notes"){
                    startActivity(new Intent(ReminderActivity.this, MainActivity.class));
                    return true;
                } else if (menuItem.getTitle()=="About us"){
                    startActivity(new Intent(ReminderActivity.this, AboutusActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}