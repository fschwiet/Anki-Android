package com.ichi2.anki;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ichi2.libanki.Collection;
import com.ichi2.libanki.Note;
import com.ichi2.libanki.Utils;

import java.util.ArrayList;
import java.util.List;


public class SelfStudyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_study);

        int noteCount = getIntent().getExtras().getInt("noteCount");
        Collection collection = AnkiDroidApp.getCol();

        Button closeButton = (Button)findViewById(R.id.button_close_selfstudy);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelfStudyActivity.this.finish();
            }
        });

        ArrayList<Note> loadedNotes = new ArrayList<Note>();

        long[] noteIds = SelfStudyActivity.getHomework(collection, noteCount);

        for(long noteId : noteIds)  {
            loadedNotes.add(collection.getNote(noteId));
        }

        StringBuilder contents = new StringBuilder();
        String delimiter = "";

        for(Note note : loadedNotes) {
            for(String[] e : note.items()) {
                contents.append(delimiter + e[0] + ": " + e[1]);
                delimiter = ", ";
            }
            delimiter = "\n";
        }

        TextView tv = (TextView)findViewById(R.id.textView_selfStudy);
        tv.setText(contents);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    static public long[] getHomework(Collection collection, int count) {

        long[] ids = Utils
                .arrayList2array(collection.getDb().queryColumn(Long.class,
                        "SELECT cards.nid, MAX(revlog.time) as time FROM revlog " +
                                "JOIN cards ON cards.id = revlog.cid " +
                                "WHERE (revlog.ease = 1 OR revlog.ease = 2) " +
                                "GROUP BY cards.nid " +
                                "ORDER BY time DESC LIMIT " + count, 0));

        return ids;
    }
}
