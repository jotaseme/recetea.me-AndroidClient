package es.upm.miw.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.myapplication.Fragments.FiveFragment;
import es.upm.miw.myapplication.Fragments.FourFragment;
import es.upm.miw.myapplication.Fragments.OneFragment;
import es.upm.miw.myapplication.Fragments.SixFragment;
import es.upm.miw.myapplication.Fragments.ThreeFragment;
import es.upm.miw.myapplication.Fragments.TwoFragment;


public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "TFM2017";
    public static final String CLAVE = LOG_TAG;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.mipmap.ic_home_white,
            R.mipmap.ic_search_white,
            R.mipmap.ic_shopping_cart_white,
            R.mipmap.ic_tab_favourite,
            R.mipmap.ic_shuffle_white,
            R.mipmap.ic_account_circle
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        tabLayout.getTabAt(5).setIcon(tabIcons[5]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Inicio");
        adapter.addFragment(new TwoFragment(), "Buscar");
        adapter.addFragment(new ThreeFragment(), "Cesta");
        adapter.addFragment(new FourFragment(), "Favoritos");
        adapter.addFragment(new FiveFragment(), "Recetea");
        adapter.addFragment(new SixFragment(), "Otro");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return null;
        }
    }
}