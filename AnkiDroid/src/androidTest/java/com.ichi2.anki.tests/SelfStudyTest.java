package com.ichi2.anki.tests;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ichi2.anki.BackupManager;
import com.ichi2.anki.SelfStudyActivity;
import com.ichi2.anki.exception.APIVersionException;
import com.ichi2.anki.exception.ConfirmModSchemaException;
import com.ichi2.libanki.Card;
import com.ichi2.libanki.Collection;
import com.ichi2.libanki.Models;
import com.ichi2.libanki.Note;
import com.ichi2.libanki.Sched;
import com.ichi2.libanki.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 11/17/14.
 */
public class SelfStudyTest extends AndroidTestCase {

    Collection collection;

    private Note addNote(String front, String back) {
        JSONObject model = collection.getModels().current();
        Note note = new Note(collection, model);
        note.setitem("Front", front);
        note.setitem("Back", back);
        assertTrue("cards were created", collection.addNote(note) > 0);

        return note;
    }

    public static final int EASE_FAILED = 1;
    public static final int EASE_HARD = 2;
    public static final int EASE_MID = 3;
    public static final int EASE_EASY = 4;

    public void testAdd() throws Exception {
        collection = com.ichi2.anki.tests.Shared.getEmptyCol();

        JSONObject model = Models.addForwardReverse(collection);

        Note failedNote = addNote("failed", "so fail");
        Note hardNote = addNote("hard", "so hard");
        Note midNote = addNote("mid", "very mid");
        Note easyNote = addNote("easy", "wow easy");

        for(Card c : failedNote.cards()) {
            collection.getSched().answerCard(c, EASE_FAILED);
        }
        for(Card c : hardNote.cards()) {
            collection.getSched().answerCard(c, EASE_HARD);
        }
        for(Card c : midNote.cards()) {
            collection.getSched().answerCard(c, EASE_MID);
        }
        for(Card c : easyNote.cards()) {
            collection.getSched().answerCard(c, EASE_EASY);
        }

        //  SelfStudyActivity picks any note that was marked as failed/hard,
        //  even if it was later picked as non-failed/hard
        for(Card c : failedNote.cards()) {
            collection.getSched().answerCard(c, EASE_EASY);
        }

        long[] homework = null;

        homework = SelfStudyActivity.getHomework(collection, 20);
        assertEquals(2, homework.length);
        assertEquals(homework[0], hardNote.getId());
        assertEquals(homework[1], failedNote.getId());

        homework = SelfStudyActivity.getHomework(collection, 1);
        assertEquals(1, homework.length);
        assertEquals(homework[0], hardNote.getId());

        /*
        for(JSONObject result : collection.getDb().queryJSON("select * from revlog")) {
            Log.d("revlog", result.toString());
        }
        */
    }
}

