package com.jmindel.flixster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class MovieViewActivity extends AppCompatActivity {
    TextView tvTitle, tvOverview, tvReleaseDate;
    ImageView ivBackdropImage;
    LinearLayout llRating;
    static final int MAX_RATING = 10;

    public static final int TRAILER_REQUEST_CODE = 30;
    public static final String MOVIE_ID = "movieId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        // Set all views
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        ivBackdropImage = findViewById(R.id.ivBackdropImage);
        llRating = findViewById(R.id.llRating);

        Intent i = getIntent();

        // Set title and synopsis
        tvTitle.setText(i.getStringExtra(MovieAdapter.MOVIE_TITLE));
        tvOverview.setText(i.getStringExtra(MovieAdapter.MOVIE_OVERVIEW));

        // Set and format date
        String date = i.getStringExtra(MovieAdapter.MOVIE_RELEASE_DATE);
        tvReleaseDate.setText(reformatDate(date));

        // Set up image
        insertWithGradientOverlay(i.getStringExtra(MovieAdapter.MOVIE_BACKDROP_URL), ivBackdropImage);

        // Set correct number of stars
        setStars(i.getDoubleExtra(MovieAdapter.MOVIE_RATING, 0));
    }

    private void insertWithGradientOverlay(String imageUrl, final ImageView into) {
        // Load bitmap from url
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(imageUrl, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Bitmap b = null;
                try {
                    /** @source https://stackoverflow.com/questions/33981384/showing-a-bitmap-url-with-asynctask */
                    b = BitmapFactory.decodeStream(new FileInputStream((file)));
                    /** @source https://stackoverflow.com/questions/4918079/android-drawing-a-canvas-to-an-imageview */
                    // Create canvas
                    Bitmap tempBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(tempBitmap);
                    // Add bitmap to canvas
                    canvas.drawBitmap(b, 0, 0, null);
                    // Add gradient to canvas with overlay blend mode
                    Paint p = new Paint();
                    p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
                    p.setShader(new LinearGradient(0, 0, 0, b.getHeight(), new int[] {
                            ResourcesCompat.getColor(MovieViewActivity.this.getResources(), R.color.colorNeonPink, null),
                            ResourcesCompat.getColor(MovieViewActivity.this.getResources(), R.color.colorNeonBlue, null)
                    }, null, Shader.TileMode.CLAMP));
                    p.setDither(true);
                    canvas.drawRect(new Rect(0, 0, b.getWidth(), b.getHeight()), p);
                    // Attach the canvas to the ImageView
                    into.setImageDrawable(new BitmapDrawable(MovieViewActivity.this.getResources(), tempBitmap));
                } catch (IOException e) {
                    Toast.makeText(MovieViewActivity.this, "Error decoding image", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(MovieViewActivity.this, "Error loading image", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    // FIXME: This is such ugly code...
    private void setStars(double rating) {
        int numStars = (int) Math.round(rating / MAX_RATING * 5);

        ImageView ivStar1 = findViewById(R.id.ivStar1);
        ImageView ivStar2 = findViewById(R.id.ivStar2);
        ImageView ivStar3 = findViewById(R.id.ivStar3);
        ImageView ivStar4 = findViewById(R.id.ivStar4);
        ImageView ivStar5 = findViewById(R.id.ivStar5);

        switch (numStars) {
            case 5:
                ivStar5.setImageResource(R.drawable.star_full);
            case 4:
                ivStar4.setImageResource(R.drawable.star_full);
            case 3:
                ivStar3.setImageResource(R.drawable.star_full);
            case 2:
                ivStar2.setImageResource(R.drawable.star_full);
            case 1:
                ivStar1.setImageResource(R.drawable.star_full);
            default:
                break;
        }
    }

    /**
     * Reformats a date from YYYY-MM-DD to (M)M/(D)D/YYYY.
     * @param rawDate
     * @return
     */
    // FIXME: This method probably has awful runtime complexity. Its input size is constant, but still.
    private String reformatDate(String rawDate) {
        String date = "";
        Pattern pattern = Pattern.compile("0*(\\d+)");
        Matcher matcher = pattern.matcher(rawDate);
        int i = 0;
        String year = "";
        while (matcher.find()) {
            String s = matcher.group(1);
            if (i > 0) date += s + "/";
            else       year = s;
            i++;
        }
        date += year;
        return date;
    }

    public void onPreview(View view) {
        Intent i = new Intent(MovieViewActivity.this, MovieTrailerActivity.class);
        i.putExtra(MovieViewActivity.MOVIE_ID, getIntent().getIntExtra(MovieViewActivity.MOVIE_ID, 0));
        startActivityForResult(i, TRAILER_REQUEST_CODE);
    }
}
