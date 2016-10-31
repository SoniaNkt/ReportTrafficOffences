package localhost.traffic_offences.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.HashMap;

import localhost.traffic_offences.MapsActivity;
import localhost.traffic_offences.R;
import localhost.traffic_offences.helper.SQLiteHandler;
import localhost.traffic_offences.helper.SessionManager;

public class MainActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private SessionManager session;
    public static Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("T", "Activity Started!");
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        Log.d("T", "SqlHandler");
        // session manager
        session = new SessionManager(getApplicationContext());
        Log.d("T", "Just created session!");

        if (!session.isLoggedIn()) {
            Log.d("", "Log out!");
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        Log.d("T", "Gotten users!");

        String name = user.get("name");
        String email = user.get("email");
        Log.d("T", "gotten name and email!");
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewpager);

        setSupportActionBar(mainToolbar);
        Log.d("T", "set toolbar!");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter (getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                result.closeDrawer();
                switch (position) {
                    case 2:
                        Log.d("","Map selected");

                }
            }
        });

        new DrawerBuilder().withActivity(this).build();
        Log.d("T", "set redundant drawer!");

         //Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.wallpaper)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(email).withIcon(R.drawable.profile)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem report = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_report);
        PrimaryDrawerItem map = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_map);
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_logout);
        Log.d("T", "Set logout item!");
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mainToolbar)
                .addDrawerItems(
                        report,
                        map,
                        logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("T", "Item clicked: " + position);
                        switch (position) {
                            case 1:
                                viewPager.setCurrentItem(1);
                                break;
                            case 2:

                                Intent mapPage = new Intent(MainActivity.this, MapsActivity.class);
                                startActivity(mapPage);
//                                viewPager.setCurrentItem(2);
                                break;
                            case 3:
                                Log.d("T", "case 1!");
                                if (session.isLoggedIn()) {
                                    Log.d("", "Log out!");
                                    logoutUser();
                                } else {
                                    Log.d("T", "not true");
                                }
                                break;
                            default:
                                Log.d("T", "default");
                                break;

                        }
                        return true;
                    }
                })
                .withAccountHeader(headerResult)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        result.setSelection(1);
        result.closeDrawer();
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        Log.d("T", "Logout user fn!");
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}