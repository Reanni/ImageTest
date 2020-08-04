package cn.reanni.image.temp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


import cn.reanni.image.R;

/**
 * Created by ZL on 2018/1/22.
 * 显示于屏幕底部的对话框
 */

public abstract class BottomDialog extends BaseCustomDialog {

    public BottomDialog(@NonNull Context context) {
        this(context, R.style.dialog_style_normal);
    }

    public BottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BottomDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initWindowParams(Window win) {
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = SizeUtils.getPhoneWidth(getContext());
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
    }

}
