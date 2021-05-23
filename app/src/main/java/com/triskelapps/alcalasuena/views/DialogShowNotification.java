package com.triskelapps.alcalasuena.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.util.WebUtils;

/**
 * Created by julio on 31/05/17.
 */

public class DialogShowNotification {


    private final Context context;

    public DialogShowNotification(Context context) {
        this.context = context;
    }

    public static DialogShowNotification newInstace(Context context) {
        return new DialogShowNotification(context);
    }

    public void show(String title, String message, final String btnText, final String btnLink) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        if (title != null) {
            ab.setTitle(title);
        }

        ab.setMessage(message);
        String buttonText = btnText != null ? btnText : context.getString(R.string.continue_str);
        ab.setPositiveButton(buttonText, (dialog, which) -> {

            if (btnText != null && btnLink != null) {
                WebUtils.openCustomTab(context, btnLink);
            }
        });
        ab.show();
    }
}
