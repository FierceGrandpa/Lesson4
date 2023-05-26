package ru.mirea.lukashev_ni.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import ru.mirea.lukashev_ni.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button.setOnClickListener(v -> calculateAveragePairs());

        TextView infoTextView = findViewById(R.id.textView);
        Thread mainThread = Thread.currentThread();
        infoTextView.setText("Имя текущего потока: " + mainThread.getName());
        // Меняем имя и выводим в текстовом поле
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-06-21, НОМЕР ПО СПИСКУ: 15, МОЙ ЛЮБИИМЫЙ ФИЛЬМ: не смотрю фильмы");
        infoTextView.append("\n Новое имя потока: " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));

//        binding.mireaButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                long endTime = System.currentTimeMillis() + 20 * 1000;
//                while (System.currentTimeMillis() < endTime) {
//                    synchronized (this) {
//                        try {
//                            wait(endTime - System.currentTimeMillis());
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//        });

        binding.mireaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format("Запущен поток No %d студентом группы No %s номер по списку No %d ", numberThread, "БСБО-06-21", 15));
                        long endTime = System.currentTimeMillis() + 20 * 1000;
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            Log.d("ThreadProject", "Выполнен поток No " + numberThread);
                        }
                    }
                }).start();
            }
        });

        Log.d(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());
    }

    private void calculateAveragePairs() {
        Thread backgroundThread = new Thread(() -> {
             int totalPairs = 1000;
             int totalSchoolDays = 30;

            double averagePairsPerDay = (double) totalPairs / totalSchoolDays;

            runOnUiThread(() -> binding.textView.setText("Кол-во пар: " + totalPairs + "\nКол-во дней:" + totalSchoolDays  + "\nСреднее количество пар в день: " + averagePairsPerDay));
        });
        backgroundThread.start();
    }
}