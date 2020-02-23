package com.hexamind.uniquorestaurant.Retrofit;

import com.hexamind.uniquorestaurant.Data.BookTableSuccess;
import com.hexamind.uniquorestaurant.Data.CheckAvailabilitySuccess;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.Person;
import com.hexamind.uniquorestaurant.Data.RegisterPost;
import com.hexamind.uniquorestaurant.Data.RegisterSuccess;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("person/login")
    Call<CustomerSuccess>  getCustomerDetails(@Query("email") String email, @Query("password") String password);

    @POST("customer/registration")
    Call<RegisterSuccess> registerUser(@Body RegisterPost newUser);

    @GET("table/checkAvailability")
    Call<CheckAvailabilitySuccess> checkAvailability();

    @PUT("customer/bookTable/{tableId}")
    Call<BookTableSuccess> bookTable(@Path("tableId") Long tableId, @Body Person person);
}
