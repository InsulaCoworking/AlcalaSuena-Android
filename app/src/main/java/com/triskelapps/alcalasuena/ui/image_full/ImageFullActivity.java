package com.triskelapps.alcalasuena.ui.image_full;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.databinding.ActivityImageFullBinding;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageFullActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE_URL = "extra_image_url";
    private ActivityImageFullBinding binding;

    public static Intent newImageFullActivity(Context context, String imageUrl) {

        Intent intent = new Intent((context), ImageFullActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityImageFullBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgClose.setOnClickListener(v -> finish());

        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);

        binding.imgZoomable.setMediumScale(2.5f);
        binding.imgZoomable.setMaximumScale(6f);

        Picasso.get()
                .load(Uri.parse(imageUrl))
                .into(binding.imgZoomable);

    }

}
