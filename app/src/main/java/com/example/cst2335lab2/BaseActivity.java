package com.example.cst2335lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Set up the purple toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Position the toolbar below the status bar
        int toolbarHeight = (int) (56 * getResources()
                .getDisplayMetrics().density + 0.5f);

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.statusBars()
            );

            v.setPadding(0, bars.top, 0, 0);

            android.view.ViewGroup.LayoutParams params =
                    v.getLayoutParams();

            params.height = toolbarHeight + bars.top;
            v.setLayoutParams(params);

            return insets;
        });

        ViewCompat.requestApplyInsets(toolbar);

        // Set up the navigation drawer
        DrawerLayout drawerLayout =
                findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_open,
                R.string.nav_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =
                findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_one) {
            Toast.makeText(this, R.string.toast_one,
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_two) {
            Toast.makeText(this, R.string.toast_two,
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {

            if (!(this instanceof MainActivity)) {
                startActivity(
                        new Intent(this, MainActivity.class)
                );
                finish();
            }

        } else if (id == R.id.nav_joke) {

            if (!(this instanceof DadJoke)) {
                startActivity(
                        new Intent(this, DadJoke.class)
                );
                finish();
            }

        } else if (id == R.id.nav_exit) {

            finishAffinity();
            return true;
        }

        DrawerLayout drawerLayout =
                findViewById(R.id.drawer_layout);

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
