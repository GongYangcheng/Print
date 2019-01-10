package com.xuanhuai.print.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xuanhuai.print.R;
import com.xuanhuai.print.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

//
public class TaskListDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private static final String TAG = "TaskListActivity";
    private HorizontalListView listView;
    private ImageView iv_close_dialog, iv_select_menu;
    private TextView tv_toolbar;
    private List<Moudle> baseArrayList;
    private MyBaseAdapter myBaseAdapter;
    private int indexPosition = 0;
    private ListView pagerListView;
    private TaskListDialog taskListDialog;

    public TaskListDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mContext = context;
        taskListDialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        // 按空白处不能取消动画
        setCanceledOnTouchOutside(false);
//        初始化弹出框标题
        initDialogToolbar();
        // 初始化界面控件
        initView();
//         初始化界面数据
        initData();
//         初始化界面控件的事件
        initEvent();
    }

    private void initDialogToolbar() {
        iv_close_dialog = findViewById(R.id.iv_close_dialog);
        iv_select_menu = findViewById(R.id.iv_select_menu);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_close_dialog.setOnClickListener(this);
        iv_select_menu.setOnClickListener(this);
        tv_toolbar.setText("任务列表");
    }

    private void initView() {
        listView = findViewById(R.id.lv_btn_list);
        pagerListView = findViewById(R.id.list_tasks);
        baseArrayList = new ArrayList<>();
        initBaseData();
        myBaseAdapter = new MyBaseAdapter(indexPosition);//TODO 更改position
        pagerListView.setAdapter(myBaseAdapter);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                R.layout.item_simple_layout,
                R.id.text,
                new String[]{"普通任务", "波次任务", "手动合任务", "智能合任务"});
        listView.setAdapter(adapter);
        listView.post(new Runnable() {
            @Override
            public void run() {
//                默认第一条选中状态
                ((TextView) listView.getChildAt(0).findViewById(R.id.text)).setBackgroundResource(R.drawable.rectangle_circular_bg);
            }
        });
    }

    private void initData() {
    }

    private void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indexPosition = position;
                isDisplayCheckBox = false;
                myBaseAdapter.notifyDataSetChanged();
                for (int i = 0; i < listView.getChildCount(); i++) {
                    TextView tv = ((TextView) listView.getChildAt(i).findViewById(R.id.text));
                    if (i == position) {
                        tv.setBackgroundResource(R.drawable.rectangle_circular_bg);
                    } else {
                        tv.setBackground(null);
                    }
                }
            }
        });

        pagerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            long lastClickTime = 0;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long time = System.currentTimeMillis();
                if (time - lastClickTime > 0 && time - lastClickTime < 500) {
//                      双击操作
                    CustomToast.showToastTest(mContext, "双击" + position + "条目");
                } else {
//                  单击-> 进入下个dialog
                    lastClickTime = time;
                    taskListDialog.dismiss();

                    CustomToast.showToastTest(mContext, "点击" + position + "条目");
                }

            }
        });
        pagerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isDisplayCheckBox = !isDisplayCheckBox;
                myBaseAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    boolean isDisplayCheckBox = false;

    private void initBaseData() {
//        模拟Viewpager内部的数据；
        for (int i = 0; i < 18; i++) {
            baseArrayList.add(new Moudle("0", "1", "2564852"
                    , "25/36", "紧急", "2018-06-12 23:59", "1"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_dialog:
//                关闭弹出框
                this.dismiss();
                break;
            case R.id.iv_select_menu:
//                小菜单选项 TODO
                ConfirmPopWindow tld = new ConfirmPopWindow(mContext);
                tld.showAtBottom(iv_select_menu);
                break;
        }
    }

    class MyBaseAdapter extends BaseAdapter {

        int pagerPosition;

        public MyBaseAdapter(int position) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyBaseAdapter.ViewHolder vh = null;
            pagerPosition = indexPosition;
            if (convertView == null) {
                vh = new MyBaseAdapter.ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_list_four_layout, null);
                vh.ck_select = convertView.findViewById(R.id.ck_select);
                vh.tv_order_num = convertView.findViewById(R.id.tv_order_num);
                vh.tv_odd_num = convertView.findViewById(R.id.tv_odd_num);
                vh.tv_complete_rate = convertView.findViewById(R.id.tv_complete_rate);
                vh.tv_urgen_sign = convertView.findViewById(R.id.tv_urgen_sign);
                vh.tv_project_time = convertView.findViewById(R.id.tv_project_time);
                vh.iv_print = convertView.findViewById(R.id.iv_print);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            if (pagerPosition == 2 || pagerPosition == 3) {
                vh.iv_print.setVisibility(View.GONE);
            } else {
                vh.iv_print.setVisibility(View.VISIBLE);
            }
//           长按显示隐藏
            if (isDisplayCheckBox) {
                vh.ck_select.setVisibility(View.VISIBLE);
            } else {
                vh.ck_select.setVisibility(View.GONE);
//                隐藏之后都设置未选中
                vh.ck_select.setChecked(false);
            }
//            记录选中状态；
//            if (vh.ck_select.isChecked()){
//                baseArrayList.get(position).setCk_select("1");
//            }else{
//                baseArrayList.get(position).setCk_select("0");
//            }
            vh.tv_order_num.setText((position + 1) + "");
            vh.tv_odd_num.setText(baseArrayList.get(position).getTv_odd_num());
            vh.tv_complete_rate.setText(baseArrayList.get(position).getTv_complete_rate());
            vh.tv_urgen_sign.setText(baseArrayList.get(position).getTv_urgen_sign());
            vh.tv_project_time.setText(baseArrayList.get(position).getTv_project_time());
            vh.iv_print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomToast.showToastTest(mContext, "打印");
                }
            });
            return convertView;
        }


        class ViewHolder {
            private CheckBox ck_select;
            private TextView tv_order_num;
            private TextView tv_odd_num;
            private TextView tv_complete_rate;
            private TextView tv_urgen_sign;
            private TextView tv_project_time;
            private ImageView iv_print;
        }
    }
}
