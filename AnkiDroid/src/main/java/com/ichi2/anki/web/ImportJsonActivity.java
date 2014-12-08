package com.ichi2.anki.web;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;

import com.ichi2.anki.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@TargetApi(11)
public class ImportJsonActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) throws RuntimeException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_json);

        try{
            String importUrlAddress = getIntent().getExtras().getString("importUrl");
            URL importUrl = new URL(importUrlAddress);
            InputStream importStream = importUrl.openStream();


            JsonReader reader = new JsonReader(new InputStreamReader(importStream));
            reader.setLenient(true);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
