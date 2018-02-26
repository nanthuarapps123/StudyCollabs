package com.bma.studycollabs;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class TabNotifi extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_notifi);



        Bundle bu_not_sc = getIntent().getExtras();
        String str_token = bu_not_sc.getString("toksept");

        TabHost tabHost = getTabHost();

        TabHost.TabSpec photospec = tabHost.newTabSpec("Schedule alert");
        photospec.setIndicator("Schedule alert", getResources().getDrawable(R.drawable.mail));
        Intent photosIntent = new Intent(this, Notification.class);
        Bundle bu_not_sche = new Bundle();
        bu_not_sche.putString("toksept",str_token);
        photosIntent.putExtras(bu_not_sche);
        photospec.setContent(photosIntent);

        TabHost.TabSpec songspec = tabHost.newTabSpec("Tutor Alert");
        songspec.setIndicator("Tutor Alert", getResources().getDrawable(R.drawable.bell));
        Intent songsIntent = new Intent(this, NotifyPostquestion.class);
        Bundle bu_tutor_alert = new Bundle();
        bu_tutor_alert.putString("toksept",str_token);
        songsIntent.putExtras(bu_tutor_alert);
        songspec.setContent(songsIntent);

        tabHost.addTab(photospec);
        tabHost.addTab(songspec);

    }
}
