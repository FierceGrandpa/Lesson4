package ru.mirea.lukashev_ni.cryptoloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MyLoader extends AsyncTaskLoader<String> {
    private byte[] encryptedData;
    private SecretKey secretKey;

    public MyLoader(@NonNull AppCompatActivity activity, byte[] encryptedData, SecretKey secretKey) {
        super(activity);
        this.encryptedData = encryptedData;
        this.secretKey = secretKey;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Override
    public String loadInBackground() {
        return decryptMsg(encryptedData, secretKey);
    }

    public String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            byte[] decryptedData = cipher.doFinal(cipherText);
            return new String(decryptedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}
