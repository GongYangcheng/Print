package com.xuanhuai.print.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;
import com.xuanhuai.print.R;
import com.xuanhuai.print.utils.CustomToast;

public class ConfirmPopWindow extends PopupWindow{
    private Context mContext;
    private ListView lv_task_list;
    public interface onItemContentListener {
        void onContentClick(String text);
    }

    public onItemContentListener mOnItemContentListener;

    public void setOnItemContextClickListener(onItemContentListener mOnItemContentListener) {
        this.mOnItemContentListener = mOnItemContentListener;
    }


    String[] taskContent = {"普通任务", "波次任务", "手动合任务", "智能合任务"};

    public ConfirmPopWindow(Context context,String[] taskContent) {
        super(context);
        this.mContext = context;
        this.taskContent = taskContent;
        this.
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.confirm_dialog_layout, null);
        lv_task_list = view.findViewById(R.id.lv_task_list);//发起群聊
//        lv_task_list.setOnClickListener(this);
        setContentView(view);
        initWindow();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                R.layout.item_list_five_layout,
                R.id.tv_task_content,
                taskContent);
        lv_task_list.setAdapter(adapter);
        lv_task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnItemContentListener.onContentClick(taskContent[position]);
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

}
