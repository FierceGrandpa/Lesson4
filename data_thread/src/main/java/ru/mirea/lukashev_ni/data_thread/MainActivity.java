package ru.mirea.lukashev_ni.data_thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.concurrent.TimeUnit;

import ru.mirea.lukashev_ni.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final Runnable runn1 = () -> binding.tvInfo.setText("runn1");
        final Runnable runn2 = () -> binding.tvInfo.setText("runn2");
        final Runnable runn3 = () -> binding.tvInfo.setText("runn3");
        Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                runOnUiThread(runn1);
                TimeUnit.SECONDS.sleep(1);
                binding.tvInfo.postDelayed(runn3, 2000);
                binding.tvInfo.post(runn2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
}