package com.example.gafandfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class AboutusActivity extends AppCompatActivity {

    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        menuBtn = findViewById(R.id.menu_btn);

        menuBtn.setOnClickListener((v)-> showMenu());
    }

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(AboutusActivity.this, menuBtn);
        popupMenu.getMenu().add("Reminders");
        popupMenu.getMenu().add("My account");
        popupMenu.getMenu().add("My notes");
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AboutusActivity.this, LoginActivity.class));
                    finish();
                    return true;
                } else if (menuItem.getTitle()=="My account"){
                    startActivity(new Intent(AboutusActivity.this, AccountActivity.class));
                    return true;
                } else if (menuItem.getTitle()=="Reminders"){
                    startActivity(new Intent(AboutusActivity.this, ReminderActivity.class));
                    return true;
                } else if (menuItem.getTitle()=="My notes"){
                    startActivity(new Intent(AboutusActivity.this, MainActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}