package com.example.day01_topic1.fram;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.day01_topic1.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CFragment extends Fragment implements View.OnClickListener {


    private static final int CAMERA_CODE = 2;
    private static final int ALBUM_CODE = 1;
    private Button pai;
    private Button xiang;
    private ImageView img;
    private File mFile;
    private Uri mImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_c, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        pai = (Button) inflate.findViewById(R.id.pai);
        xiang = (Button) inflate.findViewById(R.id.xiang);
        img = (ImageView) inflate.findViewById(R.id.img);

        pai.setOnClickListener(this);
        xiang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pai://拍照
                paizhao();
                break;
            case R.id.xiang://相册

                xiagnce();
                break;
        }
    }
    //相册授权
    private void xiagnce() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            //打开相册
            openAlbum();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},200);
        }
    }
    //相机授权
    private void paizhao() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            //打开相机
//            openAlbum();
            openCamera();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if (requestCode == 100){//相机
                openCamera();
            }else if (requestCode==200){
                openAlbum();
            }
        }
    }
//打开相册
    private void openAlbum() {
        //打开相册
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, CAMERA_CODE);
    }

    //打开相机
    private void openCamera() {

        //1创建临时保存位置
        mFile = new File(getActivity().getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        if (!mFile.exists()){
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //2, //2.转换为Uri路径  (对7.0版本兼容)
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
            mImageUri = Uri.fromFile(mFile);

        }else {
            //第二个参数要和清单文件中的配置保持一致                          com.example.day01_topic1
            mImageUri = FileProvider.getUriForFile(getActivity(), "com.example.day01_topic1", mFile);
        }
//        //开启相机
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将拍照图片存入mImageUri
        startActivityForResult(intent, CAMERA_CODE);
    }
    //重写
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == CAMERA_CODE){
                //拍照成功显示图片
//                Uri data1 = data.getData();
//                img.setImageURI(data1);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(mImageUri));
                    img.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if (requestCode == CAMERA_CODE){
                Uri data1 = data.getData();
                img.setImageURI(data1);
            }
        }
    }
}
