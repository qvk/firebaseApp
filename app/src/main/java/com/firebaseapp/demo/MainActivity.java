package com.firebaseapp.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkUserLogin()) {
            // proceed
            setupNavDrawer();
        }
    }

    private boolean checkUserLogin() {
        // if no user, go to LoginCheckActivity
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "user null", Toast.LENGTH_SHORT).show();
            goToLoginActivity();
            return false;
        }
        return true;
    }

    private void setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.home:
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.movie1:
                        Toast.makeText(getApplicationContext(),"Movies",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        signOut();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        // set user name
        TextView nameView = (TextView)navigationView.getHeaderView(0).findViewById(R.id.display_name);
        String name = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getDisplayName() : null;
        if (name != null) nameView.setText(name);

        // set up action bar drawer toggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        updateNavMovies(new String[]{"M1", "M2"});
    }

    private void updateNavMovies(String[] movies) {
        Menu menu = navigationView.getMenu();
        menu.removeGroup(R.id.group_movies);
        SubMenu subMenu = menu.addSubMenu(R.id.group_movies, Menu.NONE, Menu.NONE, "Movies");
        for (String movie : movies) {
            subMenu.add(R.id.group_movies, Menu.NONE, Menu.NONE, movie).setIcon(R.drawable.ic_movie_black_24dp);
        }
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "signed out", Toast.LENGTH_SHORT).show();
                        goToLoginActivity();
                    }
                });
    }

    private void goToLoginActivity() {
        startActivity(new Intent(this, LoginCheckActivity.class));
        finish();
    }

}
