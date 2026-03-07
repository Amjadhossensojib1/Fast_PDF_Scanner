package com.amjad.fastpdfscanner.ui.dashboardAcivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amjad.fastpdfscanner.MyfilesFragment;
import com.amjad.fastpdfscanner.R;
import com.amjad.fastpdfscanner.ToolsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton scanFab;

    MyfilesFragment myfilesFragment = new MyfilesFragment();
    ToolsFragment toolsFragment = new ToolsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        scanFab = findViewById(R.id.scanFab);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        loadFragment(myfilesFragment);

        scanFab.setOnClickListener(v -> {

            Toast.makeText(this,"Scan Button Clicked",Toast.LENGTH_SHORT).show();

            // Scanner Activity open করতে পারো
            // startActivity(new Intent(this, ScannerActivity.class));

        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_files) {
            loadFragment(myfilesFragment);
            return true;

        } else if (id == R.id.nav_tools) {
            loadFragment(toolsFragment);
            return true;
        }

        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}