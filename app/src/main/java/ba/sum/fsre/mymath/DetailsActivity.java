package ba.sum.fsre.mymath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import ba.sum.fsre.mymath.fragments.DetailsFragment;
import ba.sum.fsre.mymath.fragments.GameOneFragment;
import ba.sum.fsre.mymath.fragments.LeaderboardFragment;
import ba.sum.fsre.mymath.fragments.ListViewFragment;

public class DetailsActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailsFragment()).commit();

        this.drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.nav_lekcije) {
                    selectedFragment = new ListViewFragment();
                } else if (item.getItemId() == R.id.nav_profil) {
                    selectedFragment = new DetailsFragment();
                } else if (item.getItemId() == R.id.nav_igrica) {
                    selectedFragment = new GameOneFragment();
                } else if (item.getItemId() == R.id.nav_leaderboard) {
                    selectedFragment = new LeaderboardFragment();
                }
                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                drawerLayout.closeDrawers();
                return false;
            }
        });




    }
}