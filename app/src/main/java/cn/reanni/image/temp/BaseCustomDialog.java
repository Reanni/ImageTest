package cn.reanni.image.temp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by ZL on 2018/1/22.
 * 自定义对话框的基类
 */

public abstract class BaseCustomDialog extends android.app.Dialog {
    public BaseCustomDialog(@NonNull Context context) {
        super(context);
    }

    public BaseCustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseCustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

//    View root;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.view.View root = getLayoutInflater().inflate(attachLayoutId(), null);
        setContentView(root);

        Window win = getWindow();
        initWindowParams(win);

        initViews();
    }

    /**
     * 内容布局的id
     *
     * @return
     */
    protected abstract int attachLayoutId();

    /**
     * 初始化对话框显示的位置、大小等属性
     */
    protected abstract void initWindowParams(Window win);

    /**
     * 初始化 Views 设置监听
     */
    protected abstract void initViews();

    /**
     * TextView 的 点击事件 是否是点击的右图标区域
     *
     * @param textView 被点击的TextView
     * @param event    点击事件    PS：这里只拦截 MotionEvent.ACTION_UP 事件,避免点击的方法被多次调用
     * @return
     */
    public boolean isInDrawableRight(android.widget.TextView textView, MotionEvent event) {
        return textView.getCompoundDrawables()[2] != null && event.getAction() == MotionEvent.ACTION_DOWN &&
                event.getX() > textView.getWidth() - textView.getCompoundDrawables()[2].getBounds().width() - textView.getCompoundDrawablePadding();
    }

    public void showToast(CharSequence text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
