package com.example.additionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID ="Notification";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addBtn = findViewById(R.id.addBtn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notification", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNum = findViewById(R.id.firstNum);
                EditText secondNum = findViewById(R.id.secondNum);
                EditText result = findViewById(R.id.result);

                int num1 = Integer.parseInt(firstNum.getText().toString());
                int num2 = Integer.parseInt(secondNum.getText().toString());
                int sum = num1 + num2;
                result.setText(sum +"");

                NotificationCompat.Builder build = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID);
                build.setContentTitle("Successful Addition");
                build.setContentText("Added "+num1 + " and " + num2 + " which equals "+sum);
                build.setSmallIcon(R.drawable.ic_launcher_background);
                build.setAutoCancel(true);

                NotificationManagerCompat manager = NotificationManagerCompat.from(MainActivity.this);
                manager.notify(1, build.build());
            }
        });
    }
}