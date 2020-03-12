package com.webianks.hatkemessenger.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.webianks.hatkemessenger.R;

public class Conversation_Settings_For_Each_Contact_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation__settings__for__each__contact_);

        getSupportActionBar().setTitle("Conversation Settings");
    }
}
