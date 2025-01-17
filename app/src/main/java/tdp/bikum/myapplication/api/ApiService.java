package tdp.bikum.myapplication.api;

import tdp.bikum.myapplication.models.User;
import tdp.bikum.myapplication.models.OtpRequest;
import tdp.bikum.myapplication.models.SendOtpRequest; // Thêm lớp này nếu cần

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/register")
    Call<Void> register(@Body User user);

    @POST("/api/verify-otp")
    Call<Void> verifyOtp(@Body OtpRequest otpRequest);

    @POST("/api/login")
    Call<Void> login(@Body User user);

    @POST("/api/forgot-password")
    Call<Void> forgotPassword(@Body User user);

    @POST("/api/reset-password")
    Call<Void> resetPassword(@Body OtpRequest otpRequest);

    // Thêm phương thức sendOtp
    @POST("/api/send-otp")
    Call<Void> sendOtp(@Body SendOtpRequest sendOtpRequest);
}