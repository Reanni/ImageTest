package cn.reanni.image.temp;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import cn.reanni.image.R;
import io.reactivex.functions.Consumer;

/**
 * Created by ZL on 2018/1/22.
 */

public class ChildIconDialog extends BottomDialog implements View.OnClickListener {
    ToolsActivity activity;

    public ChildIconDialog(@NonNull Context context, File cameraTempFile) {
        super(context);
        this.activity = (ToolsActivity) context;
        this.cameraTempFile = cameraTempFile;
    }

/*    public ChildIconDialog(@NonNull Context context) {
        super(context);
        this.activity = (ToolsActivity) context;
    }

    public ChildIconDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ChildIconDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }*/

    @Override
    protected int attachLayoutId() {
        return R.layout.dialog_private_icon;
    }

    @Override
    protected void initViews() {
        TextView take_photo = findViewById(R.id.take_photo);
        take_photo.setOnClickListener(this);
        TextView choose_album = findViewById(R.id.choose_album);
        choose_album.setOnClickListener(this);
        TextView cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    File cameraTempFile;

    @Override
    public void onClick(View v) {
        RxPermissions rxPermission;
        switch (v.getId()) {
            case R.id.take_photo:
                rxPermission = new RxPermissions(activity);
                rxPermission.request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {//true表示获取权限成功（注意这里在android6.0以下默认为true）
                                    activity.getPicFromCamera(ToolsActivity.CAMERA_REQUEST_CODE, cameraTempFile);

//                                    Toast.makeText(getContext(), "打开相机", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "打开相机需要权限", Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            }
                        });
                break;
            case R.id.choose_album:
                rxPermission = new RxPermissions(activity);
                rxPermission.request(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {//true表示获取权限成功（注意这里在android6.0以下默认为true）
                                    activity.getPicFromAlbm(ToolsActivity.ALBUM_REQUEST_CODE);
                                } else {
                                    Toast.makeText(getContext(), "打开相册需要权限", Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            }
                        });
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

/*    private File mImageFile;

    //选择拍照
    private void selectCamera() {
        createImageFile();
        if (!mImageFile.exists()) {
            return;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, mImageFile.getAbsolutePath());
        Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        activity.startActivityForResult(cameraIntent, REQUEST_CAMERA);
        dismiss();
    }

    public void selectAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        activity.startActivityForResult(intent, REQUEST_ALBUM);
        dismiss();
    }

    private void createImageFile() {
        mImageFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            mImageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "出错了", Toast.LENGTH_SHORT).show();
        }
    }*/


}
