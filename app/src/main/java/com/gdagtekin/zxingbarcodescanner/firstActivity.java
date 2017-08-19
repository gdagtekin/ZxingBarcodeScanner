package com.gdagtekin.zxingbarcodescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class firstActivity extends AppCompatActivity {
    private Button buttonScan;
    private TextView tvData;

    private ZXingScannerView scannerView;
    public final int MY_PERMISSIONS_REQUEST_CAMERA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        buttonScan=(Button)findViewById(R.id.buttonScan);
        tvData=(TextView)findViewById(R.id.textView);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startScan();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String[] permissionRequested ={Manifest.permission.CAMERA};
                        requestPermissions(permissionRequested, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }
            }
        });
    }
    public void startScan(){
        scannerView=new ZXingScannerView(this);
        scannerView.setResultHandler(new firstActivity.ZXingScannerResultHandler());
        setContentView(scannerView);
        scannerView.startCamera();
    }
    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler{
        @Override
        public void handleResult(Result result) {

            String resultData=result.getText();
            Intent intent=new Intent(getApplicationContext(), firstActivity.class);
            intent.putExtra("data",resultData);
            startActivity(intent);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //İzin alındı
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] permissionRequested ={Manifest.permission.CAMERA};
                requestPermissions(permissionRequested, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
        tvData.setText(getIntent().getStringExtra("data"));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScan();
                } else {
                    Toast.makeText(this, "Kamera izni vermezseniz tarama yapılamaz.", Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }
}
