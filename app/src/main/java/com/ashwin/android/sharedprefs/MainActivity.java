package com.ashwin.android.sharedprefs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    private static final String SUB_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveToNormalSharedPrefs("key-1", "val-1");
        saveToEncryptedSharedPrefs("key-1", "val-1");

        readEncryptedSharedPrefs("key-1");
    }

    private void saveToNormalSharedPrefs(String key, String value) {
        SharedPreferences sharedPrefs = this.getSharedPreferences("normal_shared_prefs", Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(key, value).apply();
    }

    private void saveToEncryptedSharedPrefs(String key, String value) {
        try {
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences sharedPrefs = EncryptedSharedPreferences.create(
                    this,
                    "secret_shared_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            sharedPrefs.edit().putString(key, value).apply();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readEncryptedSharedPrefs(String key) {
        try {
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences sharedPrefs = EncryptedSharedPreferences.create(
                    this,
                    "secret_shared_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            String value = sharedPrefs.getString(key, null);
            Log.d(Constant.APP_TAG, SUB_TAG + ": readEncryptedSharedPrefs(" + key + "): " + value);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
