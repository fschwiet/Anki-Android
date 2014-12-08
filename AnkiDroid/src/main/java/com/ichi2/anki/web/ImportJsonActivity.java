package com.ichi2.anki.web;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;

import com.ichi2.anki.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@TargetApi(11)
public class ImportJsonActivity extends ActionBarActivity {

    static final int REQUESTCODE_HAVE_DOCUMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws RuntimeException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_json);

        String importUrlAddress = getIntent().getExtras().getString("importUrl");

        if (importUrlAddress == null) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUESTCODE_HAVE_DOCUMENT);
            return;
        }

        try{
            URL importUrl = new URL(importUrlAddress);
            InputStream importStream = importUrl.openStream();


            JsonReader reader = new JsonReader(new InputStreamReader(importStream));
            reader.setLenient(true);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_HAVE_DOCUMENT) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = new Bundle();
                bundle.putString("importUrl", data.getDataString());
                Intent importJsonIntent = new Intent(ImportJsonActivity.this, ImportJsonActivity.class);
                importJsonIntent.putExtras(bundle);
                ImportJsonActivity.this.startActivity(importJsonIntent);

            } else {
                throw new RuntimeException("Intent.ACTION_OPEN_DOCUMENT failed with resultCode:" + resultCode);
            }
        }
    }
}
