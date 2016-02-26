package toy.hellozs.com.coolpicshow;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AlbumActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public CoordinatorLayout rootLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Resources mResources;
    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mResources = this.getResources();
        rootLayout = (CoordinatorLayout) findViewById(R.id.id_coordinator);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootLayout, mResources.getString(R.string.responsibility), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        titles = new String[]{mResources.getString(R.string.cat_weimei),
                                     mResources.getString(R.string.cat_qingxin),
                                     mResources.getString(R.string.cat_sheying),
                                     mResources.getString(R.string.cat_gaoxiao),
                                     mResources.getString(R.string.cat_mingxing),
                                     mResources.getString(R.string.cat_meinv),
                                     mResources.getString(R.string.cat_keai)};
        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                AlbumFragment fragment = AlbumFragment.newInstance(position);
                return fragment;
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // 侧边栏选择栏目
        if (id == R.id.nav_weimei) {
            mViewPager.setCurrentItem(0, false);
        } else if (id == R.id.nav_qingxin) {
            mViewPager.setCurrentItem(1, false);

        } else if (id == R.id.nav_sheying) {
            mViewPager.setCurrentItem(2, false);

        } else if (id == R.id.nav_gaoxiao) {
            mViewPager.setCurrentItem(3, false);

        } else if (id == R.id.nav_mingxing) {
            mViewPager.setCurrentItem(4, false);

        } else if (id == R.id.nav_meinv) {
            mViewPager.setCurrentItem(5, false);

        } else if (id == R.id.nav_keai) {
            mViewPager.setCurrentItem(6, false);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
