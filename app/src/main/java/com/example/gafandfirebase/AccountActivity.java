package com.example.gafandfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        menuBtn = findViewById(R.id.menu_btn);

        menuBtn.setOnClickListener((v)-> showMenu());
    }

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(AccountActivity.this, menuBtn);
        popupMenu.getMenu().add("Reminders");
        popupMenu.getMenu().add("My notes");
        popupMenu.getMenu().add("About us");
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                    finish();
                    return true;
                } else if (menuItem.getTitle()=="My notes"){
                    startActivity(new Intent(AccountActivity.this, MainActivity.class));
                    return true;
                } else if (menuItem.getTitle()=="Reminders"){
                    startActivity(new Intent(AccountActivity.this, ReminderActivity.class));
                    return true;
                } else if (menuItem.getTitle()=="About us"){
                    startActivity(new Intent(AccountActivity.this, AboutusActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

}