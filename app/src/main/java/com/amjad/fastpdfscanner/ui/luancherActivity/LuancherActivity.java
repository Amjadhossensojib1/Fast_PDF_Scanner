package com.amjad.fastpdfscanner.ui.luancherActivity;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amjad.fastpdfscanner.R;
import com.amjad.fastpdfscanner.ui.dashboardAcivity.DashboardActivity;

public class LuancherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luancher);
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                startActivity(new Intent(LuancherActivity.this, DashboardActivity.class));
                finish();
            }
        };
        handler.postDelayed(r, 1000);

        TextView title = findViewById(R.id.tv_title);

        String text = "Convart Any <font color='#FF0000'>file</font> to PDF";
        title.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
    }
}