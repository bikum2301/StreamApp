package tdp.bikum.myapplication.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.api.ApiService;
import tdp.bikum.myapplication.api.RetrofitClient;
import tdp.bikum.myapplication.databinding.ActivityOtpBinding;
import tdp.bikum.myapplication.models.OtpRequest;
import tdp.bikum.myapplication.models.SendOtpRequest;
import tdp.bikum.myapplication.models.User;

public class OtpActivity extends AppCompatActivity {

    private ActivityOtpBinding binding;
    private String email, password;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận email và password từ RegisterActivity
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        // Bắt đầu đếm ngược thời gian
        startTimer();

        // Xử lý sự kiện khi nhấn nút Xác thực OTP
        binding.btnVerifyOtp.setOnClickListener(v -> verifyOtp());

        // Xử lý sự kiện khi nhấn nút Gửi lại OTP
        binding.btnResendOtp.setOnClickListener(v -> resendOtp());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) { // 1 phút
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText("Thời gian còn lại: " + millisUntilFinished / 1000 + " giây");
            }

            @Override
            public void onFinish() {
                binding.tvTimer.setText("Mã OTP đã hết hạn");
                binding.btnVerifyOtp.setEnabled(false);
            }
        }.start();
    }

    private void verifyOtp() {
        String otp = binding.etOtp.getText().toString().trim();

        if (otp.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        // Gọi API xác thực OTP
        OtpRequest otpRequest = new OtpRequest(email, otp);
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.verifyOtp(otpRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    // Xác thực thành công, đăng ký tài khoản
                    registerUser();
                } else {
                    Toast.makeText(OtpActivity.this, "Xác thực OTP thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(OtpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser() {
        User user = new User(email, password);
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.register(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OtpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng OtpActivity và quay về RegisterActivity
                } else {
                    Toast.makeText(OtpActivity.this, "Đăng ký thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(OtpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendOtp() {
        binding.progressBar.setVisibility(View.VISIBLE);

        // Gọi API gửi lại mã OTP
        SendOtpRequest sendOtpRequest = new SendOtpRequest(email);
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.sendOtp(sendOtpRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(OtpActivity.this, "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show();
                    startTimer(); // Bắt đầu lại đếm ngược
                } else {
                    Toast.makeText(OtpActivity.this, "Gửi lại mã OTP thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(OtpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}