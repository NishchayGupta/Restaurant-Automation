package com.hexamind.uniquorestaurant.Retrofit;

import com.hexamind.uniquorestaurant.Data.BookTableSuccess;
import com.hexamind.uniquorestaurant.Data.CheckAvailabilitySuccess;
import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.Data.OrderSuccess;
import com.hexamind.uniquorestaurant.Data.Person;
import com.hexamind.uniquorestaurant.Data.RegisterPost;
import com.hexamind.uniquorestaurant.Data.RegisterSuccess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("person/login")
    Call<CustomerSuccess> getCustomerDetails(@Query("email") String email, @Query("password") String password);

    @POST("customer/registration")
    Call<RegisterSuccess> registerUser(@Body RegisterPost newUser);

    @GET("table/checkAvailability")
    Call<CheckAvailabilitySuccess> checkAvailability();

    @PUT("customer/bookTable/{tableId}")
    Call<BookTableSuccess> bookTable(@Path("tableId") Long tableId, @Body Person person);

    @GET("table/updateWaitingTime")
    Call<GeneralError> checkAvailabilityCheck();

    @GET("foodItems")
    Call<List<FoodItems>> getAllFoodItems();

    @POST("orderFood")
    Call<OrderSuccess> createOrder(@Body Order order);

    @GET("order/customer/{customerId}")
    Call<OrderSuccess> getCustomerTableExists(@Path("customerId") Long customerId);

    @PUT("customer/payment/{orderId}")
    Call<GeneralError> getPaymentConfirmation(@Path("orderId") Long orderId);

    @GET("customer/emailCheck/{email}")
    Call<GeneralError> checkEmailExists(@Path("email") String email);

    @GET("chef/existingOrders")
    Call<List<ChefOrders>> getAllOrdersForChef();

    @GET("order/customer/allOrders/{customerId}")
    Call<List<ChefOrders>> getAllOrders(@Path("customerId") Long customerId);
}
