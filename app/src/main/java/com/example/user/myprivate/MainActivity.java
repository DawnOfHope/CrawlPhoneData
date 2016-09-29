package com.example.user.myprivate;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private TelephonyManager telephonyManager;
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Android 6+
        if (Build.VERSION.SDK_INT >= 23 ){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},1);
            }
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},1);
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},1);
            }
        }

        telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

        String linenum = telephonyManager.getLine1Number();
        String imei = telephonyManager.getDeviceId();
        String imsi = telephonyManager.getSubscriberId();
        if (linenum != null)
            Log.d("test",linenum);
        Log.d("test",imei);
        Log.d("test",imsi);

        telephonyManager.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);

        accountManager = (AccountManager)getSystemService(ACCOUNT_SERVICE);
        Account[] accounts = accountManager.getAccounts();
        for (Account account :accounts){
            String accountName = account.name;
            String accountType = account.type;
            Log.d("test",accountName + ":" + accountType);
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_IDLE){
                //change to idel
                Log.d("test","off");
            }else if (state == TelephonyManager.CALL_STATE_RINGING){
                Log.d("test",incomingNumber);

            }else if (state == TelephonyManager.CALL_STATE_OFFHOOK){
                Log.d("test","talk");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
