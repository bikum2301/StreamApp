package tdp.bikum.myapplication.api;

import tdp.bikum.myapplication.models.LoginRequest;
import tdp.bikum.myapplication.models.OtpResetPasswordRequest;
import tdp.bikum.myapplication.models.OtpVerificationRequest;
import tdp.bikum.myapplication.models.User;
import tdp.bikum.myapplication.models.SendOtpRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/register")
    Call<Void> register(@Body User user);

    @POST("/api/verify-otp")
    Call<Void> verifyOtp(@Body OtpVerificationRequest otpVerificationRequest);

    @POST("/api/login")
    Call<Void> login(@Body LoginRequest loginRequest); // Sử dụng LoginRequest thay vì User

    @POST("/api/forgot-password")
    Call<Void> forgotPassword(@Body User user);

    @POST("/api/reset-password")
    Call<Void> resetPassword(@Body OtpResetPasswordRequest otpResetPasswordRequest);

    @POST("/api/send-otp")
    Call<Void> sendOtp(@Body SendOtpRequest sendOtpRequest);
}