package tdp.bikum.myapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tdp.bikum.myapplication.R;
import tdp.bikum.myapplication.api.ApiService;
import tdp.bikum.myapplication.api.RetrofitClient;
import tdp.bikum.myapplication.databinding.ActivityForgotPasswordBinding;
import tdp.bikum.myapplication.models.User;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Xử lý sự kiện khi nhấn nút Gửi yêu cầu
        binding.btnSubmit.setOnClickListener(v -> forgotPassword());
    }

    private void forgotPassword() {
        String email = binding.etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền email", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        User user = new User(email, "");
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.forgotPassword(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Yêu cầu đã được gửi! Vui lòng kiểm tra email.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Gửi yêu cầu thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}