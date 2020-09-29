package br.unip.ads.pim.service;

import android.util.Log;

import com.google.gson.Gson;

import br.unip.ads.pim.BuildConfig;
import br.unip.ads.pim.model.ErrorResponse;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static final String TAG = RetrofitSingleton.class.getSimpleName();

    private static RetrofitSingleton INSTANCE = new RetrofitSingleton();

    private final Gson gson;
    public final ApiService apiService;

    private RetrofitSingleton() {
        gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                // Adicionar o suporte para serialização/deserialização de JSON
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static RetrofitSingleton get() {
        return RetrofitSingleton.INSTANCE;
    }

    public <T> String parseErrorMessage(Response<T> response) {
        try {
            if (response.errorBody() != null) {
                return gson.fromJson(response.errorBody().charStream(), ErrorResponse.class).mensagem;
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao recuperar a mensagem de negócio da API.", e);
        }
        return null;
    }
}
