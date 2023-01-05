package com.example.totp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TOTP 생성을 위한 Secret 값 (Base32 인코딩된 값)
        EditText secretKeyText = findViewById(R.id.secretKeyTV);
        String secretKey = secretKeyText.getText().toString();
        //TOTP 생성을 위한 알고리즘 "SHA1", "SHA224", "SHA256", "SHA384", "SHA512"
        EditText algorithmText = findViewById(R.id.algorithmTV);
        String algorithm = algorithmText.getText().toString();
        //TOTP 생성 자릿수 (6~8)
        EditText digitsText = findViewById(R.id.digitsTV);
        String digits = digitsText.getText().toString();
        //TOTP 생성 간격 (단위 : 초)
        EditText periodText = findViewById(R.id.periodTV);
        String period = periodText.getText().toString();
        //UNIX 시간 값 (1970년 1월 1일 UTC 자정 이후 경과된 시간(초))
        //지정되지 않은 경우 Native 에서 취득한 시간 사용
        EditText timeText = findViewById(R.id.timeTV);
        String time = timeText.getText().toString();

        Button generateBtn = findViewById(R.id.generateBtn);
        generateBtn.setOnClickListener(view -> totpGenerate(secretKey, time, digits, algorithm, period));
    }

    private void totpGenerate(String key, String time, String returnDigits, String crypto, String period){
        long T0 = 0;
        long X = Long.parseLong(period);
        long testTime;

        if(time.equals("")){
            testTime = System.currentTimeMillis() / 1000L;
        } else {
            testTime = Long.parseLong(time);
        }

        StringBuilder steps = new StringBuilder("0");
        long T = (testTime - T0) / X;
        steps = new StringBuilder(Long.toHexString(T).toUpperCase());
        while (steps.length() < 16)
            steps.insert(0, "0");

        Totp totp = new Totp();
        totp.generateTOTP(key, steps.toString(), returnDigits, crypto);
    }
}