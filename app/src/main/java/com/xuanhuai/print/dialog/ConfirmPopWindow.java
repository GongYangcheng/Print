package com.xuanhuai.print.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

import com.xuanhuai.print.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ConfirmPopWindow extends PopupWindow {
    private Context mContext;
    private ListView lv_task_list;
    private ArrayList<String> indexArrayList;
    ConfirmPopWindow confirmPopWindow;

    public interface onItemContentListener {
        void onContentClick(String text);
    }

    public onItemContentListener mOnItemContentListener;

    public void setOnItemContextClickListener(onItemContentListener mOnItemContentListener) {
        this.mOnItemContentListener = mOnItemContentListener;
    }

    String jsonText = "{0:" + "[{\"1\":\"合并任务\",\"2\":\"拆分任务\",\"03\":\"取消任务\"," +
            "\"04\":\"打印标签\",\"05\":\"查看缺货\"}]," +
            "1:" +
            "[{\"01\":\"打印物料\",\"02\":\"查看缺货\",\"03\":\"取消任务\"," +
            "\"04\":\"打印标签\"}]," +
            "2:" +
            "[{\"01\":\"打印打算\",\"02\":\"查看缺货\",\"03\":\"取消任务\"}]" +
            "3:" +
            "[{\"01\":\"大数据库\",\"02\":\"疏塞数数\",\"03\":\"无额度诶\"}]" +
            "}";//问题：json下标是根据条目来的，不是
    JSONObject jsonObj;

    public ConfirmPopWindow(Context context, JSONObject jsonObj) {
        super(context);
        this.mContext = context;
        confirmPopWindow = this;
        this.jsonObj = jsonObj;
        indexArrayList = new ArrayList<>();
//        String jsonText = "{\"01\":\"合并任务\",\"02\":\"拆分任务\",\"03\":\"取消任务\"," +
//                "\"04\":\"打印标签\",\"05\":\"查看缺货\"}";
//            jsonObj = new JSONObject(jsonText);
        Iterator<String> it = jsonObj.keys();
        while (it.hasNext()) {
            // 获得key
            String key = it.next();
            indexArrayList.add(key);
        }
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.confirm_dialog_layout, null);
        lv_task_list = view.findViewById(R.id.lv_task_list);//发起群聊
        setContentView(view);
        initWindow();
        MyArrayBaseAdapter adapter = new MyArrayBaseAdapter();
        lv_task_list.setAdapter(adapter);
        lv_task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnItemContentListener.onContentClick(indexArrayList.get(position));//TODO 要改参数
                confirmPopWindow.dismiss();
            }
        });
    }

    private void initWindow() {
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        this.setWidth((int) (d.widthPixels * 0.35));
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.8f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext, 1f);
            }
        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 10);
        //showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, 110);//有偏差
    }

    class MyArrayBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return jsonObj.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return jsonObj.get(indexArrayList.get(position));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_list_five_layout, null);
                vh = new ViewHolder();
                vh.tv_order_num = convertView.findViewById(R.id.tv_task_content);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
//            记录选中状态；
            try {
                vh.tv_order_num.setText(jsonObj.get(indexArrayList.get(position))+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            private TextView tv_order_num;
        }
    }
}
