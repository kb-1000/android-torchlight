package com.fake.android.torchlight;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.fake.android.torchlight.core.TorchlightFallback;
import com.fake.android.torchlight.core.TorchlightControl;
import com.fake.android.torchlight.v1.ITorchlight;
import timber.log.Timber;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    @SuppressWarnings("FieldCanBeLocal")
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private FloatingActionButton fab;
    private ITorchlight torchlight;
    private DrawerLayout drawer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            torchlight.release();
        } catch (RemoteException e) {
            Timber.e(e);
        }
        torchlight = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length != 1 || permissions[0].equals(Manifest.permission.CAMERA)) {
            Timber.e("Unknown permissions were requested: %s", Arrays.toString(permissions));
        }
    }

    private boolean requestPerm() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        torchlight = Common.blockingTorchlightBind(this);
        setContentView(R.layout.activity_main);
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ((TextView) this.findViewById(R.id.perm_info_text)).setText(R.string.perm_info_text);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TorchlightControl.hasFlash()) {
                    if (requestPerm()) {
                        return;
                    }
                    try {
                        torchlight.set(!torchlight.get());
                    } catch (RemoteException e) {
                        Timber.e(e);
                        throw new RuntimeException(e);
                    }
                }
                if (!TorchlightControl.hasFlash()) {
                    Intent intent = new Intent(MainActivity.this, TorchlightFallback.Activity.class);
                    startActivity(intent);
                }
                updateImageButton();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateImageButton();
    }

    private void updateImageButton() {
        try {
            if (torchlight.get()) {
                fab.setImageResource(R.drawable.ic_sunny_white);
            } else {
                fab.setImageResource(R.drawable.ic_sunny_black);
            }
        } catch (RemoteException e) {
            Timber.e(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;

        switch (id) {

        case R.id.nav_manage:
            intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            break;

        case R.id.nav_share:
            try {
                intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(MainActivity.this.getApplicationInfo().sourceDir)));
                intent.setType("application/vnd.android.package-archive");
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
            } catch (ActivityNotFoundException e) {
                Common.toast(this, R.string.error_no_share_app);
            }
            break;
        case R.id.nav_send_github:
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_issue_url)));
            startActivity(intent);
            break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
