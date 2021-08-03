package com.fake.android.torchlight

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.fake.android.torchlight.v1.ITorchlight
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import timber.log.Timber
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private var fab: FloatingActionButton? = null
    private var torchlight: ITorchlight? = null
    private var drawer: DrawerLayout? = null

    override fun onDestroy() {
        super.onDestroy()
        try {
            torchlight!!.release()
        } catch (e: RemoteException) {
            Timber.e(e)
        }

        torchlight = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.size != 1 || permissions[0] == Manifest.permission.CAMERA) {
            Timber.e("Unknown permissions were requested: %s", permissions.contentToString())
        }
    }

    private fun requestPerm(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA)
            return true
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        torchlight = Common.blockingTorchlightBind(this)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
            (this.findViewById<View>(R.id.perm_info_text) as TextView).setText(R.string.perm_info_text)
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        fab = findViewById(R.id.fab)
        fab!!.setOnClickListener(View.OnClickListener {
            if (requestPerm()) {
                return@OnClickListener
            }
            try {
                torchlight!!.set(!torchlight!!.get())
            } catch (e: RemoteException) {
                Timber.e(e)
                throw RuntimeException(e)
            }

            updateImageButton()
        })

        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        updateImageButton()
    }

    private fun updateImageButton() {
        try {
            if (torchlight!!.get()) {
                fab!!.setImageResource(R.drawable.ic_sunny_white)
            } else {
                fab!!.setImageResource(R.drawable.ic_sunny_black)
            }
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        val intent: Intent

        when (id) {

            R.id.nav_manage -> {
                intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }

            // TODO: this won't work with split APKs
            R.id.nav_share -> try {
                intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(this@MainActivity.applicationInfo.sourceDir)))
                intent.type = "application/vnd.android.package-archive"
                startActivity(Intent.createChooser(intent, getString(R.string.share)))
            } catch (e: ActivityNotFoundException) {
                Common.toast(this, R.string.error_no_share_app)
            }

            R.id.nav_send_github -> {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_issue_url)))
                startActivity(intent)
            }
        }

        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_CAMERA = 1
    }
}
