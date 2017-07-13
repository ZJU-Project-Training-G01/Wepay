package cn.edu.zju.se_g01.nfc_pay;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.se_g01.nfc_pay.Good.Goods;
import cn.edu.zju.se_g01.nfc_pay.Good.GoodsLab;
import cn.edu.zju.se_g01.nfc_pay.fragments.HomeFragment;
import cn.edu.zju.se_g01.nfc_pay.fragments.OrderListFragment;
import cn.edu.zju.se_g01.nfc_pay.fragments.SearchFragment;
import cn.edu.zju.se_g01.nfc_pay.tools.NfcOperator;

public class MainActivity extends AppCompatActivity {

    public NfcOperator nfcOperator = NfcOperator.getInstance();

    public SearchFragment searchFragment;

    private final static String TAG = "Main_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //去掉默认的导航栏
        setContentView(R.layout.activity_main);

        //设置顶部的 toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);//设置导航栏图标
//        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
//        toolbar.setTitle("");//设置主标题
//        toolbar.setSubtitle("Subtitle");//设置子标题

        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
//                if (menuItemId == R.id.action_search) {
//                    Toast.makeText(ToolBarActivity.this , R.string.menu_search , Toast.LENGTH_SHORT).show();
//
//                } else if (menuItemId == R.id.action_notification) {
//                    Toast.makeText(ToolBarActivity.this , R.string.menu_notifications , Toast.LENGTH_SHORT).show();

                if (menuItemId == R.id.user_info_item) {
                    Toast.makeText(MainActivity.this, "个人信息", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, UserSettingActivity.class);
                    startActivity(i);
                }

//                } else if (menuItemId == R.id.logout_item) {
//                    Toast.makeText(MainActivity.this, "item_02", Toast.LENGTH_SHORT).show();
//
//                }
                return true;
            }
        });

        int[] icons = {
                R.drawable.ic_nfc_scan,
                R.drawable.ic_goods_list,
                R.drawable.ic_order_list,
        };
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_tab_content);

        setupViewPager(viewPager);


        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
        tabLayout.getTabAt(0).select();


        //两种情况：1. app没在运行时，onCreate方法手动调用onNewIntent
        onNewIntent(getIntent());
    }

    //2. app在运行时，自动调用onNewIntent
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(getApplicationContext(), "on new intent", Toast.LENGTH_SHORT).show();


        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            //启动NfcOperator
            nfcOperator.initNFCData(this);
            String msg = nfcOperator.processIntent(intent);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            Goods g = GoodsLab.getInstance().getGood(msg);
            if (g != null) {
//                searchFragment.startGoodActivity(this, msg);
                Intent i = new Intent(this, GoodActivity.class);
                i.putExtra(GoodActivity.EXTRA_GOOD_ID, g.getGoodsId());
                startActivity(i);
            }
            else{
                Toast.makeText(getApplicationContext(), "商品" + msg + "不存在!", Toast.LENGTH_SHORT).show();
            }
//            Log.d()


//            List<Goods> list = searchFragment.goodsList;
//            if (msg != null)
//                for (int i = 0; i < list.size(); i++) {
//                    Goods g = list.get(i);
//                    if (g.getGoodsId().equals(msg)){
//                        //Start GoodActivity
//                        Intent intent1 = new Intent(this, GoodActivity.class);
//                        intent1.putExtra(GoodActivity.EXTRA_GOOD_ID, g.getUuid());
//                        startActivity(intent1);
//                    }
//                }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.insertNewFragment(new HomeFragment());
        adapter.insertNewFragment(searchFragment = new SearchFragment());
        adapter.insertNewFragment(new OrderListFragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

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

        public void insertNewFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = this.getMenuInflater();
//        inflater.inflate(R.menu.main_activity, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_settings:
//                Intent settingsIntent = new Intent(this, TabSample.class);
//                startActivity(settingsIntent);
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
