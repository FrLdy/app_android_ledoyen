package fac.app.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import fac.app.Controller.InternetConnexion;
import fac.app.Controller.Menu;
import fac.app.R;
import fac.app.model.annonce.Annonce;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
/**
 * Base d'une activité avec un menu permettant de naviguer vers
 * les différentes parties de l'app.
 * Menu accessible sur une toolbar et un navigation drawer.
 * **/
public class ActivityWithMenu extends AppCompatActivity implements
NavigationView.OnNavigationItemSelectedListener{
    public static final int EXTERNAL_STORAGE_READ = 1;
    protected InternetConnexion internetConnexion;
    protected Menu menu;
    public android.support.v7.widget.Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.menu = new Menu(findViewById(android.R.id.content));
        this.internetConnexion = menu.internetConnexion;
    }

    public void configureToolbar(String titre){
        this.toolbar = findViewById(R.id.topToolbar);
        this.toolbar.setTitle(titre);
        setSupportActionBar(toolbar);
    }

    protected void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menuAdd :
                menu.displaySaveAnnonce();
                break;
            case R.id.menuListe :
                menu.displayListeAnnonces();
                break;
            case R.id.menuProfil :
                menu.displayProfilActivity();
                break;
            case R.id.menuRandom :
                menu.displayRandomAnnonce();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menuAdd :
                menu.displaySaveAnnonce();
                break;
            case R.id.menuListe :
                menu.displayListeAnnonces();
                break;
            case R.id.menuProfil :
                menu.displayProfilActivity();
                break;
            case R.id.menuRandom :
                menu.displayRandomAnnonce();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    protected void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
