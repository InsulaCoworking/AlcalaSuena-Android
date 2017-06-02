package com.triskelapps.alcalasuena.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.News;

/**
 * Created by julio on 31/05/17.
 */

public class DialogShowNews {


    private final Context context;

    public DialogShowNews(Context context) {
        this.context = context;
    }

    public static DialogShowNews newInstace(Context context) {
        return new DialogShowNews(context);
    }

    public void show(final News news) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(news.getTitle());


        ab.setMessage(news.getText());
        if (news.hasValidLinkButton()) {
            ab.setPositiveButton(news.getBtn_text(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String text = String.format(context.getString(R.string.download_app_text_2), news.getBtn_link());
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.setType("text/plain");
                    context.startActivity(intent);

                }
            });
        }
        ab.setNegativeButton(R.string.close, null);
        ab.show();
    }
}
