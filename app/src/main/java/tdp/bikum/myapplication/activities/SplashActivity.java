package tdp.bikum.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import tdp.bikum.myapplication.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Sử dụng SplashScreen API (cho Android 12+)
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Sử dụng Handler để chuyển sang MainActivity sau 1.5 giây
        new Handler().postDelayed(() -> {
            // Tạo Intent để chuyển sang MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            // Kết thúc SplashActivity để ngăn người dùng quay lại bằng nút Back
            finish();
        }, 1500); // 1500 milliseconds = 1.5 giây
    }
}