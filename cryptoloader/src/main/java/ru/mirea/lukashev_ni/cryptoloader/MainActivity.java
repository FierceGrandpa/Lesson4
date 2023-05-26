package ru.mirea.lukashev_ni.cryptoloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private final String TAG = this.getClass().getSimpleName();
    private final int LoaderID = 1234;
    private EditText editText;
    private Button sendButton;
    private SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        sendButton = findViewById(R.id.sendButton);

        secretKey = generateKey();

        sendButton.setOnClickListener(view -> {
            String phrase = editText.getText().toString();
            byte[] encryptedData = encryptMsg(phrase, secretKey);
            Bundle bundle = new Bundle();
            bundle.putByteArray("encryptedData", encryptedData);
            LoaderManager.getInstance(MainActivity.this).initLoader(LoaderID, bundle, MainActivity.this);
        });
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d(TAG, "onLoaderReset");
    }
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (i == LoaderID) {
            Toast.makeText(this, "onCreateLoader:" + i, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, bundle.getByteArray("encryptedData"), secretKey);
        }
        throw new InvalidParameterException("Invalid loader id");
    }
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String decryptedPhrase) {
        if (loader.getId() == LoaderID) {
            Log.d(TAG, "Decrypted phrase: " + decryptedPhrase);
            Toast.makeText(this, "Decrypted phrase: " + decryptedPhrase, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Decryption failed", Toast.LENGTH_SHORT).show();
        }
    }

    private SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec(kg.generateKey().getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private byte[] encryptMsg(String message, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}