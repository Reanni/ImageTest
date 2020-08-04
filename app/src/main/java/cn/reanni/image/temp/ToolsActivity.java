package cn.reanni.image.temp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;

import java.io.File;

/**
 * Created by ZL on 2018/1/16.
 * 基类Activity
 * 封装通用工具类方法
 */

public abstract class ToolsActivity extends AppCompatActivity {

    //相机请求码
    public static final int CAMERA_REQUEST_CODE = 1;
    //相册请求码
    public static final int ALBUM_REQUEST_CODE = 2;
    //剪裁请求码
    public static final int CROP_REQUEST_CODE = 3;


/******************************************************** 工具类方法 **************************************************************************/

    /**
     * 进入另一个Activity
     *
     * @param clz 要进入的Activity
     */
    public void startActivity(Class clz) {
        startActivity(new Intent(getApplicationContext(), clz));
    }

    /**
     * TextView 的 点击事件 是否是点击的右图标区域
     *
     * @param textView 被点击的TextView
     * @param event    点击事件    PS：这里只拦截 MotionEvent.ACTION_UP 事件,避免点击的方法被多次调用
     * @return
     */
    public boolean isInDrawableRight(TextView textView, MotionEvent event) {
        return textView.getCompoundDrawables()[2] != null && event.getAction() == MotionEvent.ACTION_UP &&
                event.getX() > textView.getWidth() - textView.getCompoundDrawables()[2].getBounds().width() - textView.getCompoundDrawablePadding();

    }

    /**
     * TextView 变成可倒计时动态文字
     * 使用CountDownTimer.start() 开启倒计时
     *
     * @param textView          要倒计时的TextView
     * @param millisInFuture    总计时时间
     * @param countDownInterval 每次减少的时间，时间间隔
     * @param onTick            倒计时时显示的文字
     * @return 倒计时器
     */
    public CountDownTimer timerTextView(final TextView textView, long millisInFuture, long countDownInterval, final CharSequence onTick, final CharSequence onFinish) {

        CountDownTimer timer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setEnabled(false);
                textView.setText((millisUntilFinished / 1000) + onTick.toString());
            }

            @Override
            public void onFinish() {
                textView.setEnabled(true);
                textView.setText(onFinish);
            }

        };
        return timer;
    }

    /**
     * 吐司
     *
     * @param text
     */
    public void showToast(CharSequence text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示键盘
     */
    public void showKeyboard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive() && getCurrentFocus() != null) {
//            if (getCurrentFocus().getWindowToken() != null) {
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
    }

    /**
     * 从相机获取图片
     *
     * @param requestCode
     * @return
     */
    public void getPicFromCamera(int requestCode, File tempFile) {
        KLog.i(tempFile);
        //用于保存调用相机拍照后所生成的文件
//        File tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            KLog.i(tempFile.getAbsolutePath());
            KLog.i(getPackageName());
            Uri contentUri = FileProvider.getUriForFile(this, getPackageName(), tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        intent.putExtra("file", tempFile);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 从相册获取图片
     */
    public void getPicFromAlbm(int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, requestCode);
    }

    /**
     * 裁剪图片
     */
    public void cropPhoto(Uri sourceUri, Uri outputUri, int requestCode) {
//        KLog.i(sourceUri);
//        KLog.i(outputUri);
        Intent intent = new Intent("com.android.camera.action.CROP");
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
*/
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(sourceUri, "image/*");//可以选择图片类型，如果是*表明所有类型的图片
        intent.putExtra("crop", "true");// crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("scale", false);//裁剪时是否保留图片的比例，这里的比例是1:1
        intent.putExtra("aspectX", 1);// aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputY", 300);
//        intent.putExtra("circleCrop", true);//是否是圆形裁剪区域，设置了也不一定有效
        intent.putExtra("circleCrop", false);//是否是圆形裁剪区域，设置了也不一定有效
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//设置输出的格式
//        intent.putExtra("return-data", true);//是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri); //设置返回的uri
        startActivityForResult(intent, requestCode);
    }


    File tempFile;

    public File getTempFile(String extra) {
  /*      if (null == tempFile)
//            tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + extra + ".jpg");
            tempFile = new File(getExternalCacheDir(), System.currentTimeMillis() + extra + ".jpg");
//            tempFile =  new File(getExternalFilesDir("head"),System.currentTimeMillis() + extra + ".jpg");
        return tempFile;
        */
        return new File(getExternalCacheDir(), System.currentTimeMillis() + extra + ".jpg");
    }

}



