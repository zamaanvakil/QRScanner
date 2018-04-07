package com.example.android.qrscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Objects;

public class ScanInfoActivity extends AppCompatActivity {

    String result;
    String type;
    String date;
    int id;

    TextView result_tv;
    TextView date_tv;
    ImageView type_icon;

    Button openurl_btn;
    Button delete_btn;

    ImageView qrcode_image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_info);

        result = getIntent().getStringExtra("result");
        type = getIntent().getStringExtra("type");
        date = getIntent().getStringExtra("date");
        id = getIntent().getIntExtra("id",0);

        result_tv = findViewById(R.id.result_tv);
        date_tv = findViewById(R.id.date_tv);
        delete_btn = findViewById(R.id.delete_btn);
        openurl_btn = findViewById(R.id.openurl_btn);
        type_icon = findViewById(R.id.type_icon);
        qrcode_image_view = findViewById(R.id.qrcode_image_view);

        result_tv.setText(result);
        date_tv.setText(date);

        openurl_btn.setVisibility(100);

        if(Objects.equals(type, "link")){
            type_icon.setImageResource(R.drawable.link_icon);
            openurl_btn.setVisibility(0);
            openurl_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(result));
                    startActivity(i);
                }
            });
        }


        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper myDb = new DatabaseHelper(ScanInfoActivity.this);
                if(myDb.deleteData(Integer.toString(id))==1)
                    Toast.makeText(ScanInfoActivity.this, "Deletion Successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ScanInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(result, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcode_image_view.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScanInfoActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
