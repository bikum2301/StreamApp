package tdp.bikum.myapplication.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import de.hdodenhof.circleimageview.CircleImageView;
import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CircleImageView ivMusicIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ánh xạ CircleImageView
        ivMusicIcon = binding.ivMusicIcon;

        // Bắt đầu hiệu ứng xoay
        startRotationAnimation();

        // Xử lý sự kiện khi nhấn nút Đăng ký
        binding.btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện khi nhấn nút Đăng nhập
        binding.btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Xử lý padding cho hệ thống thanh điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void startRotationAnimation() {
        // Tạo ObjectAnimator để xoay CircleImageView
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(ivMusicIcon, "rotation", 0f, 360f);
        rotationAnimator.setDuration(10000); // Thời gian xoay (10 giây)
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE); // Lặp vô hạn
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART); // Lặp lại từ đầu
        rotationAnimator.setInterpolator(null); // Đảm bảo xoay đều, không có hiệu ứng tăng tốc hoặc giảm tốc
        rotationAnimator.start(); // Bắt đầu hiệu ứng
    }
}