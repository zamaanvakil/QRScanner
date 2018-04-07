package com.example.android.qrscanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ReaderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReaderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);

        setContentView(scannerView);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                Toast.makeText(ReaderActivity.this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                requestPermission();
            }
        }
    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(ReaderActivity.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[])
    {
        switch(requestCode)
        {
            case REQUEST_CAMERA :
                if(grantResults.length > 0)
                {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted)
                    {
                        Toast.makeText(ReaderActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ReaderActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if(shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displayAlertMessage("You need to allow access for both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                if(scannerView == null)
                {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else
            {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listner)
    {
        new AlertDialog.Builder(ReaderActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",listner)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        final String scanResult = result.getText();
        String type;
        String date;
        DatabaseHelper myDb = new DatabaseHelper(this);

        if(URLUtil.isValidUrl(scanResult))
            type = "link";
        else
            type = "text";

        date = DateFormat.getDateInstance().format(new Date());

        boolean isInserted = myDb.insertData(date,type,scanResult);
        if(isInserted){
            //Toast.makeText(ReaderActivity.this, "Insertion successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ReaderActivity.this, ScanInfoActivity.class);
            intent.putExtra("result", scanResult);
            intent.putExtra("type",type);
            intent.putExtra("date",date);
            intent.putExtra("id",myDb.getithID(0));
            startActivity(intent);
        }
        else
            Toast.makeText(ReaderActivity.this, "Failed", Toast.LENGTH_SHORT).show();

    }
}
