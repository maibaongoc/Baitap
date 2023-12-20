package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public int luaChon=0;
    ImageView imageView;

    ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        CharSequence optionMenu[]={"Chụp ảnh","Chọn ảnh","Thoát"};
                        builder.setItems(optionMenu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (optionMenu[i]=="Chụp ảnh"){
                                    Intent takePicture= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    setResult(RESULT_OK, takePicture);
                                    luaChon=1;
                                    //thực hiện chức năng mà người dùng vừa chọn lựa
                                    getData.launch(takePicture);
                                } else if (optionMenu[i]=="Chọn ảnh") {
                                    Intent pickPhoto= new Intent();
                                    pickPhoto.setType("image/*");
                                    pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                                    setResult(RESULT_OK, pickPhoto);
                                    luaChon=2;
                                    getData.launch(pickPhoto);
                                }
                                else if (optionMenu[i]=="Thoát"){
                                    dialog.dismiss();
                                }
                            }
                        });
                builder.show();
            }
        });
    }



    ActivityResultLauncher<Intent> getData = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), o -> {
        if (o.getResultCode()== Activity.RESULT_OK){
            Intent data = o.getData();
            Bitmap selectedImage = null;
            if (luaChon==1){
                selectedImage= (Bitmap) data.getExtras().get("data");
            }else if(luaChon==2){
                Uri selectedImageUrl = data.getData();
                try{
                    selectedImage= MediaStore.Images.Media.getBitmap(this.
                            getContentResolver(), selectedImageUrl);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            imageView.setImageBitmap(selectedImage);
        }
    });



}