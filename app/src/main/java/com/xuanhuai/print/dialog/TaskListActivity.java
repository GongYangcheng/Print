package com.xuanhuai.print.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xuanhuai.print.R;
import com.xuanhuai.print.utils.CustomToast;
import com.xuanhuai.print.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private static final String TAG = "TaskListActivity";
    private HorizontalListView listView;
    private NoScrollViewPager  viewPager;
    private Context mContext;
    private PagerAdapter pagerAdapter;
    private List<String> arrayList;
    private List<Moudle> baseArrayList;
    private MyBaseAdapter myBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        mContext = this;
        arrayList = new ArrayList<>();
        baseArrayList = new ArrayList<>();
        initListData();
        initBaseData();
        listView = findViewById(R.id.lv_btn_list);

        viewPager = findViewById(R.id.view_pager);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                R.layout.item_simple_layout,
                R.id.text,
                new String[]{"普通任务","波次任务","手动合任务","智能合任务"});
        listView.setAdapter(adapter);
        pagerAdapter = new MyPagerAdapter(mContext,arrayList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);//当前条目
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                listView.setSelection(position);
                myBaseAdapter.notifyDataSetChanged();//TODO 每次活动到此位置刷新一次新数据；
                for(int i = 0;i<listView.getChildCount();i++){
                    if (i == position){
                        ((TextView)listView.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.parseColor("#FFFFFF"));
                    }else{
                        ((TextView)listView.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.parseColor("#000000"));
                    }
                }

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPager.setCurrentItem(position);
                myBaseAdapter.notifyDataSetChanged();//TODO 每次活动到此位置刷新一次新数据
                for(int i = 0;i<listView.getChildCount();i++){
                    if (i == position){
                        ((TextView)listView.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.parseColor("#FFFFFF"));
                    }else{
                        ((TextView)listView.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.parseColor("#000000"));
                    }
                }
            }
        });
    }

    private void initBaseData() {
//        模拟Viewpager内部的数据；
        for (int i = 0 ; i < 18 ; i++ ){
            baseArrayList.add(new Moudle("1","1","2564852"
            ,"25/36","紧急","2018-06-12","1"));
        }
    }

    private void initListData() {
        for (int i = 1;i <= 4 ;i++){
            arrayList.add("第"+i+"页面");
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        private Context mContext;
        private List<String> mData;

        public MyPagerAdapter(Context context ,List<String> list) {
            mContext = context;
            mData = list;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        long lastClickTime = 0;
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(mContext, R.layout.item_base,null);
            ListView pagerListView = view.findViewById(R.id.list_task);
            myBaseAdapter = new MyBaseAdapter(position);
            pagerListView.setAdapter(myBaseAdapter);
            container.addView(view);
            CustomToast.showToastTest(mContext,"双击"+position+"条目");
            pagerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long time = System.currentTimeMillis();
                    if (time - lastClickTime > 0 && time - lastClickTime < 200){
//                      两次点击不超过200毫秒默认是双击
                        CustomToast.showToastTest(mContext,"双击"+position+"条目");
                    }else{
                        lastClickTime = time;
                        CustomToast.showToastTest(mContext,"点击"+position+"条目");
                    }

                }
            });

            pagerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    CustomToast.showToastTest(mContext,"长按"+position+"条目");
                    return true;
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container,position,object); 这一句要删除，否则报错
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    class MyBaseAdapter extends BaseAdapter{

        int pagerPosition;

        public MyBaseAdapter(int position){
            this.pagerPosition = position;
        }
        @Override
        public int getCount() {
            return baseArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return baseArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null){
                vh = new ViewHolder();
                convertView = View.inflate(mContext,R.layout.item_list_four_layout,null);
                vh.ck_select = convertView.findViewById(R.id.ck_select);
                vh.tv_order_num = convertView.findViewById(R.id.tv_order_num);
                vh.tv_odd_num = convertView.findViewById(R.id.tv_odd_num);
                vh.tv_complete_rate = convertView.findViewById(R.id.tv_complete_rate);
                vh.tv_urgen_sign = convertView.findViewById(R.id.tv_urgen_sign);
                vh.tv_project_time = convertView.findViewById(R.id.tv_project_time);
                vh.iv_print = convertView.findViewById(R.id.iv_print);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            if (pagerPosition == 2||pagerPosition == 3){
                vh.iv_print.setVisibility(View.GONE);
            }else{
                vh.iv_print.setVisibility(View.VISIBLE);
            }
//            vh.ck_select.setText(baseArrayList.get(position).getCk_select());
            vh.tv_order_num .setText(baseArrayList.get(position).getTv_order_num());
            vh.tv_odd_num .setText(baseArrayList.get(position).getTv_odd_num());
            vh.tv_complete_rate.setText(baseArrayList.get(position).getTv_complete_rate());
            vh.tv_urgen_sign .setText(baseArrayList.get(position).getTv_urgen_sign());
            vh.tv_project_time.setText(baseArrayList.get(position).getTv_project_time());
            return convertView;
        }

        class ViewHolder{
            private CheckBox ck_select;
            private TextView tv_order_num;
            private TextView tv_odd_num;
            private TextView tv_complete_rate;
            private TextView tv_urgen_sign;
            private TextView tv_project_time;
            private ImageButton iv_print;
        }
    }
}
