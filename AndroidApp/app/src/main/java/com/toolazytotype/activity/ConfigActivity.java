package com.toolazytotype.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.toolazytotype.R;
import com.toolazytotype.archive.Constants;
import com.toolazytotype.archive.ForegroundService;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ConfigActivity", "ConfigActivity Created ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button startButton = findViewById(R.id.btnStartService);
        Button stopButton =  findViewById(R.id.btnStopService);
        Button permissionButton =  findViewById(R.id.btnPermission);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        permissionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartService:
                Intent startIntent = new Intent(ConfigActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                break;
            case R.id.btnStopService:
                Intent stopIntent = new Intent(ConfigActivity.this, ForegroundService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);
                break;
            case R.id.btnPermission:
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, 0);
                break;

            default:
                break;
        }

    }
}
