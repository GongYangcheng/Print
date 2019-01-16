package com.xuanhuai.print.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xuanhuai.print.R;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private static final String TAG = "TaskListActivity";
    private HorizontalListView listView;
    private Context mContext;
    private List<Moudle> baseArrayList;
//    private MyBaseAdapter myBaseAdapter;
    private int indexPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_layout);
        mContext = this;
        Button btn = findViewById(R.id.btn_dialog);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskListDialog tld = new TaskListDialog(mContext);
                tld.show();
            }
        });
//        baseArrayList = new ArrayList<>();
//        initBaseData();
//        listView = findViewById(R.id.lv_btn_list);
//        final ListView pagerListView = findViewById(R.id.list_tasks);
//            myBaseAdapter = new MyBaseAdapter(indexPosition);//TODO 更改position
//            pagerListView.setAdapter(myBaseAdapter);
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
//                R.layout.item_simple_layout,
//                R.id.text,
//                new String[]{"普通任务","波次任务","手动合任务","智能合任务"});
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                indexPosition = position;
//                myBaseAdapter.notifyDataSetChanged();
//                for(int i = 0;i<listView.getChildCount();i++){
//                    if (i == position){
//                        ((TextView)listView.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.parseColor("#FFFFFF"));
//                    }else{
//                        ((TextView)listView.getChildAt(i).findViewById(R.id.text)).setTextColor(Color.parseColor("#000000"));
//                    }
//                }
//            }
//        });
//
//        pagerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            long lastClickTime = 0;
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    long time = System.currentTimeMillis();
//                    if (time - lastClickTime > 0 && time - lastClickTime < 500){
////                      两次点击不超过200毫秒默认是双击 双击操作
//                        CustomToast.showToastTest(mContext,"双击"+position+"条目");
//                    }else{
////单击选中
//                        lastClickTime = time;
//                        CustomToast.showToastTest(mContext,"点击"+position+"条目");
//                    }
//
//                }
//            });
//        final boolean isLongClick = true;
//        pagerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////                    CheckBox cb = parent.getChildAt(position).findViewById(R.id.ck_select);
//                    for (int i=0;i<myBaseAdapter.getCount();i++){
//                        LinearLayout relativeLayout= (LinearLayout) myBaseAdapter.getView(i,null,null);//RelativeLayout是listview的item父布局
//                        CheckBox cb = relativeLayout.findViewById(R.id.ck_select);
//                        if (isLongClick){
//                            cb.setVisibility(View.VISIBLE);
//                        }else{
//                            cb.setVisibility(View.GONE);
//                        }
//                    }
////                    CustomToast.showToastTest(mContext,"长按"+;+"条目");
//                    return true;
//                }
//            });
        //ListView item 中的删除按钮的点击事件
//        ta.setOnItemDeleteClickListener(new onItemDeleteListener() {
//            @Override
//            public void onDeleteClick(int i) {
////                执行打印操作 TODO
//                CustomToast.showToastTest(mContext,"删除");
////                baseArrayList.remove(i);
////                myBaseAdapter.notifyDataSetChanged();
//            }
//        });

    }

//    private void initBaseData() {
////        模拟Viewpager内部的数据；
//        for (int i = 0 ; i < 18 ; i++ ){
//            baseArrayList.add(new Moudle("1","1","2564852"
//            ,"25/36","紧急","2018-06-12","1"));
//        }
//    }

//    class MyBaseAdapter extends BaseAdapter{
//
//        int pagerPosition;
//
//        public MyBaseAdapter(int position){
//            this.pagerPosition = position;
//        }
//        @Override
//        public int getCount() {
//            return baseArrayList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return baseArrayList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            ViewHolder vh = null;
//            pagerPosition = indexPosition;
//            if (convertView == null){
//                vh = new ViewHolder();
//                convertView = View.inflate(mContext,R.layout.item_list_four_layout,null);
//                vh.ck_select = convertView.findViewById(R.id.ck_select);
//                vh.tv_order_num = convertView.findViewById(R.id.tv_order_num);
//                vh.tv_odd_num = convertView.findViewById(R.id.tv_odd_num);
//                vh.tv_complete_rate = convertView.findViewById(R.id.tv_complete_rate);
//                vh.tv_urgen_sign = convertView.findViewById(R.id.tv_urgen_sign);
//                vh.tv_project_time = convertView.findViewById(R.id.tv_project_time);
//                vh.iv_print = convertView.findViewById(R.id.iv_print);
//                convertView.setTag(vh);
//            }else{
//                vh = (ViewHolder) convertView.getTag();
//            }
//            if (pagerPosition == 2||pagerPosition == 3){
//                vh.iv_print.setVisibility(View.GONE);
//            }else{
//                vh.iv_print.setVisibility(View.VISIBLE);
//            }
////            vh.ck_select.setText(baseArrayList.get(position).getCk_select());
//            vh.tv_order_num .setText(baseArrayList.get(position).getTv_order_num());
//            vh.tv_odd_num .setText(baseArrayList.get(position).getTv_odd_num());
//            vh.tv_complete_rate.setText(baseArrayList.get(position).getTv_complete_rate());
//            vh.tv_urgen_sign .setText(baseArrayList.get(position).getTv_urgen_sign());
//            vh.tv_project_time.setText(baseArrayList.get(position).getTv_project_time());
//            vh.iv_print.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    CustomToast.showToastTest(mContext,"删除");
//                    mOnItemDeleteListener.onDeleteClick(position);
//                }
//            });
//            return convertView;
//        }
//
//
//        class ViewHolder{
//            private CheckBox ck_select;
//            private TextView tv_order_num;
//            private TextView tv_odd_num;
//            private TextView tv_complete_rate;
//            private TextView tv_urgen_sign;
//            private TextView tv_project_time;
//            private ImageView iv_print;
//        }
//    }
//    public interface onItemDeleteListener {
//        void onDeleteClick(int i);
//    }
//
//    private onItemDeleteListener mOnItemDeleteListener;
//
//    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
//        this.mOnItemDeleteListener = mOnItemDeleteListener;
//    }
}
