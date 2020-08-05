package cn.reanni.image.temp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.socks.library.KLog;

import java.io.File;

public class XxxActivity extends ToolsActivity {

    Uri cropUri = null;
    Uri uri = null;
    File tempFile = null; //相机拍照生成的图片路径

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE://从相机拍照返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(this, getPackageName(), tempFile);
                        KLog.i(uri);
                        cropUri = Uri.fromFile(getTempFile("-crop"));
                    } else {
                        uri = Uri.fromFile(tempFile);
                        KLog.i(uri);
                        cropUri = Uri.fromFile(getTempFile("-crop"));
                        KLog.i(cropUri);
                    }
                    cropPhoto(uri, cropUri, CROP_REQUEST_CODE);
                }
                break;
            case ALBUM_REQUEST_CODE://从相册返回
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    KLog.i(uri);
                    cropUri = Uri.fromFile(getTempFile("-crop"));
                    KLog.i(cropUri);
                    cropPhoto(uri, cropUri, CROP_REQUEST_CODE);
                }
                break;
            case CROP_REQUEST_CODE://裁剪
                if (data == null) return;
                KLog.i(uri);
                KLog.i(cropUri);
                if (uri != null) {
                    final String filePath = FileUtills.getFileByUri(cropUri, this).getAbsolutePath();
                    KLog.i(filePath);
              /*      GlideApp.with(getActivity()).load(cropUri).apply(RequestOptions.circleCropTransform())
                            .error(R.drawable.author).placeholder(R.drawable.author)
                            .into(private_icon.getCentertIcon());*/
                   /* AliOss.uploading(getActivity(), filePath, new AliOss.UploadingSuccessCallback() {
                        @Override
                        public void onSuccess(PutObjectResult result, String uploadingFileUrl) {
                            headImageUrl = uploadingFileUrl;
                            EventBus.getDefault().post(new UserPortraitChanged(filePath, headImageUrl));
                        }
                    });*/
                }
                break;
        }
    }

}
