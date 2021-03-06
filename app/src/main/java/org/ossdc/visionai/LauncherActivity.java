/*
 * Copyright (C) 2021 Marius Slavescu - OSSDC.org. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ossdc.visionai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.ossdc.visionai.webrtc.RaceOSSDCActivity;

import java.security.SecureRandom;
import java.util.Arrays;

public class LauncherActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SharedPreferences sp;

    private String roomName;
    private String roomPassword;
    boolean useBackCamera = false;
    boolean useDisplayArea  = false;
    boolean hideLocalCamera  = true;
    private String robotMode = "OpenBot";
    String[] ROBOT_MODES = { "OpenBot", "Neato", "SPARK Assistant"};

    private static final String CHAR_LIST =
            "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * This method generates random string
     * @return
     */
    public String generateRandomStringUsingSecureRandom(int length){
        StringBuffer randStr = new StringBuffer(length);
        SecureRandom secureRandom = new SecureRandom();
        for( int i = 0; i < length; i++ )
            randStr.append( CHAR_LIST.charAt( secureRandom.nextInt(CHAR_LIST.length()) ) );
        return randStr.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp=getSharedPreferences("SD", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_launch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spin = findViewById(R.id.robotMode);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ROBOT_MODES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);

        if(savedInstanceState!=null)
        {
            useBackCamera = savedInstanceState.getBoolean("useBackCamera");
            roomName = savedInstanceState.getString("roomName");
            roomPassword = savedInstanceState.getString("roomPassword");
            useDisplayArea = savedInstanceState.getBoolean("useDisplayArea",false);
            hideLocalCamera = savedInstanceState.getBoolean("hideLocalCamera",false);
            robotMode = savedInstanceState.getString("robotMode","OpenBot");
        }
        else
        {
            useBackCamera = sp.getBoolean("useBackCamera",false);
            roomName = sp.getString("roomName","TestRoom-"+generateRandomStringUsingSecureRandom(6));
            roomPassword = sp.getString("roomPassword","123456");
            useDisplayArea = sp.getBoolean("useDisplayArea",false);
            hideLocalCamera = sp.getBoolean("hideLocalCamera",false);
            robotMode = sp.getString("robotMode","OpenBot");
        }

        EditText roomNameField = findViewById(R.id.editTextTextPersonName);
        EditText roomPasswordField = findViewById(R.id.editTextTextPassword);
        Switch backCameraSwitchField = findViewById(R.id.backCameraSwitch);
        Switch displayAreaSwitchField = findViewById(R.id.displayAreaSwitch);
        Switch hideLocalCameraSwitchField = findViewById(R.id.hideLocalCameraSwitch);
        Spinner robotModeField = findViewById(R.id.robotMode);

        backCameraSwitchField.setChecked(useBackCamera);
        roomNameField.setText(roomName);
        roomPasswordField.setText(roomPassword);
        displayAreaSwitchField.setChecked(useDisplayArea);
        hideLocalCameraSwitchField.setChecked(hideLocalCamera);
        robotModeField.setSelection(Arrays.asList(ROBOT_MODES).indexOf(robotMode));
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected robot mode: "+ROBOT_MODES[position] ,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }

    public void openRaceOSSDCActivity(View view) {
        EditText roomNameField = findViewById(R.id.editTextTextPersonName);
        EditText roomPasswordField = findViewById(R.id.editTextTextPassword);
        Switch backCameraSwitchField = findViewById(R.id.backCameraSwitch);
        Switch displayAreaSwitchField = findViewById(R.id.displayAreaSwitch);
        Switch hideLocalCameraSwitchField = findViewById(R.id.hideLocalCameraSwitch);
        Spinner robotModeField = findViewById(R.id.robotMode);

        useBackCamera = backCameraSwitchField.isChecked();
        roomName = roomNameField.getText().toString();
        roomPassword = roomPasswordField.getText().toString();
        useDisplayArea = displayAreaSwitchField.isChecked();
        hideLocalCamera = hideLocalCameraSwitchField.isChecked();
        robotMode = ROBOT_MODES[robotModeField.getSelectedItemPosition()];

        Intent intent = new Intent(this, RaceOSSDCActivity.class);
        intent.putExtra("roomName", roomName);
        intent.putExtra("roomPassword", roomPassword);
        intent.putExtra("useBackCamera", useBackCamera);
        intent.putExtra("useDisplayArea", useDisplayArea);
        intent.putExtra("hideLocalCamera", hideLocalCamera);
        intent.putExtra("robotMode", robotMode);

        sp=getSharedPreferences("SD", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.putString("roomName",roomName);
        ed.putString("roomPassword",roomPassword);
        ed.putBoolean("useBackCamera",useBackCamera);
        ed.putBoolean("useDisplayArea",useDisplayArea);
        ed.putBoolean("hideLocalCamera",hideLocalCamera);
        ed.putString("robotMode",robotMode);

        ed.commit();

        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("useBackCamera", useBackCamera);
        savedInstanceState.putString("roomName", roomName);
        savedInstanceState.putString("roomPassword", roomPassword);
        savedInstanceState.putBoolean("useDisplayArea", useDisplayArea);
        savedInstanceState.putBoolean("hideLocalCamera", hideLocalCamera);
        savedInstanceState.putString("robotMode", robotMode);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        useBackCamera = savedInstanceState.getBoolean("useBackCamera");
        roomName = savedInstanceState.getString("roomName");
        roomPassword = savedInstanceState.getString("roomPassword");
        useDisplayArea = savedInstanceState.getBoolean("useDisplayArea");
        hideLocalCamera = savedInstanceState.getBoolean("hideLocalCamera");
        robotMode = savedInstanceState.getString("robotMode");
    }
}
