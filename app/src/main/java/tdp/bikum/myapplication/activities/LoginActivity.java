package tdp.bikum.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import tdp.bikum.myapplication.databinding.ActivityLoginBinding;
import tdp.bikum.myapplication.models.LoginRequest;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Check for existing login
        if (isUserLoggedIn()) {
            navigateToMusicPlayer();
            return;
        }

        // Setup click listeners
        setupClickListeners();

        // Check for remembered user
        checkAutoLogin();
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> loginUser());

        binding.tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));

        binding.tvSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void checkAutoLogin() {
        boolean isRemembered = sharedPreferences.getBoolean("isRemembered", false);
        if (isRemembered) {
            String savedUsername = sharedPreferences.getString("username", "");
            binding.etUsernameOrEmail.setText(savedUsername);
            binding.cbRememberMe.setChecked(true);
        }
    }

    private void loginUser() {
        String usernameOrEmail = binding.etUsernameOrEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        LoginRequest loginRequest = new LoginRequest(usernameOrEmail, password);
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.login(loginRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    saveLoginPreferences(usernameOrEmail);
                    navigateToMusicPlayer();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Đăng nhập thất bại: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginPreferences(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);

        if (binding.cbRememberMe.isChecked()) {
            editor.putBoolean("isRemembered", true);
            editor.putString("username", username);
        } else {
            editor.remove("isRemembered");
            editor.remove("username");
        }

        editor.apply();
    }

    private void navigateToMusicPlayer() {
        startActivity(new Intent(this, MusicPlayerActivity.class));
        finish();
    }
}