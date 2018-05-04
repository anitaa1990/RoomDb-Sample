package com.an.room.util;

import android.app.Activity;
import android.content.Intent;

import com.an.room.AppConstants;
import com.an.room.model.Note;
import com.an.room.ui.activity.AddNoteActivity;
import com.an.room.ui.activity.PwdActivity;

public class NavigatorUtils implements AppConstants {


    public static void redirectToPwdScreen(Activity activity,
                                           Note note) {
        Intent intent = new Intent(activity, PwdActivity.class);
        intent.putExtra(INTENT_TASK, note);
        activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }


    public static void redirectToEditTaskScreen(Activity activity,
                                               Note note) {
        Intent intent = new Intent(activity, AddNoteActivity.class);
        intent.putExtra(INTENT_TASK, note);
        activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    public static void redirectToViewNoteScreen(Activity activity,
                                                Note note) {
        Intent intent = new Intent(activity, AddNoteActivity.class);
        intent.putExtra(INTENT_TASK, note);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
        activity.finish();
    }
}
