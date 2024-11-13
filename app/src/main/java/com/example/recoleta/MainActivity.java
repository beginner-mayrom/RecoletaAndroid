package com.example.recoleta;

import okhttp3.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextInputEditText inputEmail;
    EditText inputPasswd;
    Button loginButton;

    private static final String url = "https://recoletaapi.onrender.com/api/auth/login";

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button register = (Button) findViewById(R.id.buttonCadastro);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        inputEmail = (TextInputEditText) findViewById(R.id.textInputEmail);
        inputPasswd = (EditText) findViewById(R.id.editTextSenha);
        loginButton = (Button) findViewById(R.id.buttonEntrar);

        client = new OkHttpClient();

        loginButton.setOnClickListener(view -> {
            String email = inputEmail.getText().toString();
            String password = inputPasswd.getText().toString();
            login(email, password);
        });
    }

    private void login(String email, String password) {

        String json = "{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Erro " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.code() == 200){
                    String responseData = response.body().string();
                    try{
                        // Extrair o token JWT do JSON da resposta
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String token = jsonResponse.getString("accessToken");

                        // Salvar o token em SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("jwt_token", token);
                        editor.apply();

                        Log.d("Login", "Token salvo: " + token);

                        runOnUiThread(() -> Toast.makeText(MainActivity.this,
                                "Login Realizado com Sucesso!", Toast.LENGTH_SHORT).show());


                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);

                    }catch (JSONException e){
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(MainActivity.this,
                                "Erro " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }else{
                    Log.e("Login", "Falha no login: " + response.message());
                    runOnUiThread(() -> Toast.makeText(MainActivity.this,
                            "Falha no login: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // Função para recuperar o token JWT armazenado
    public String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("jwt_token", null);
    }
}