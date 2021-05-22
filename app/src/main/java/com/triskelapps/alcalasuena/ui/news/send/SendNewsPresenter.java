package com.triskelapps.alcalasuena.ui.news.send;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.triskelapps.alcalasuena.DebugHelper;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.interactor.NewsInteractor;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.util.Util;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class SendNewsPresenter extends BasePresenter {

    private final SendNewsView view;
    private String imagePath;
    public boolean permissionGranted;
    private NewsInteractor newsInteractor;

    public static void launchSendNewsActivity(Context context) {

        Intent intent = new Intent(context, SendNewsActivity.class);

        context.startActivity(intent);
    }

    public static SendNewsPresenter newInstance(SendNewsView view, Context context) {

        return new SendNewsPresenter(view, context);

    }

    private SendNewsPresenter(SendNewsView view, Context context) {
        super(context, view);

        this.view = view;

    }

    public void onCreate() {

        EasyImage.configuration(context)
                .setImagesFolderName("bg-images");

        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {

                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        permissionGranted = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        permissionGranted = false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {

                        new AlertDialog.Builder(context)
                                .setMessage(R.string.need_write_permission)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        token.continuePermissionRequest();
                                    }
                                })
                                .setNeutralButton(R.string.cancel, null)
                                .show();
                    }

                }).check();

        newsInteractor = new NewsInteractor(context, view);

//        sendNotificationPush("prueba", "texto prueba");
    }


    public void onCameraButtonClick() {
        if (!permissionGranted) {
            view.toast(R.string.permission_denied);
            return;
        }

        EasyImage.openCamera((Activity) context, 0);
    }

    public void onGalleryButtonClick() {
        if (!permissionGranted) {
            view.toast(R.string.permission_denied);
            return;
        }

        EasyImage.openGallery((Activity) context, 0);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        EasyImage.handleActivityResult(requestCode, resultCode, data, (Activity) context, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                view.toast("Error image");
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

//                Bitmap b = BitmapFactory.decodeFile(imageFile.getPath());
//                Log.i(TAG, "onImagePicked: size original: " + b.getWidth() + "x" + b.getHeight());

                String resizePath = Util.resizeImage(imageFile.getPath(), 800);
//                Bitmap b2 = BitmapFactory.decodeFile(resizePath);
//                Log.i(TAG, "onImagePicked: size resized: " + b2.getWidth() + "x" + b2.getHeight());


                imagePath = resizePath;
                view.setImageUri(Uri.fromFile(new File(resizePath)));

            }

        });
    }

    public void onSendButtonClick(final String title, final String text, final String link, String linkButtonText) {

        if (text.isEmpty() || title.isEmpty()) {
            view.toast(R.string.write_news);
            return;
        }

        if (!link.isEmpty() && linkButtonText.isEmpty()) {
            linkButtonText = context.getString(R.string.more_info);
        }

        final String finalLinkButtonText = linkButtonText;

        new AlertDialog.Builder(context, R.style.CustomDialogTheme)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(R.string.send_news, (dialog, which) -> sendNews(title, text, link, finalLinkButtonText))
                .setNegativeButton(R.string.send_notification, (dialog, which) -> sendNotificationPush(title, text,link, finalLinkButtonText,null))
                .setNeutralButton(R.string.back, null)
                .show();
    }

    public void sendNews(final String title, final String text, final String link, final String linkButtonText) {

        view.showProgressDialog(context.getString(R.string.sending_news));
        newsInteractor.sendNews(title, text, link, linkButtonText, imagePath, new BaseInteractor.BasePOSTFullEntityCallback<News>() {
            @Override
            public void onSuccess(News news) {
                view.toast(R.string.news_sent);
                sendNotificationPush(title, text, link, linkButtonText, news);
            }

            @Override
            public void onError(String message) {
                view.toast(message);
            }
        });
    }

    private void sendNotificationPush(String title, String text, String link, String linkButtonText, News news) {

        if (DebugHelper.SWITCH_MOCK_NOTIF_NEWS) {
            news = new News();
            news.setTitle(title);
            news.setText(text);
            news.setBtn_link(link);
            news.setBtn_text(linkButtonText);
            news.setId(0);
        }


        view.showProgressDialog(context.getString(R.string.sending_notification));
        newsInteractor.sendNewsNotification(title, text, link, linkButtonText, news, new BaseInteractor.BasePOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                view.toast(R.string.notification_sent);
                ((Activity) context).finish();
            }

            @Override
            public void onError(String message) {
                view.toast(message);
            }
        });
    }


}
