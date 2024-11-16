package com.example.recoleta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminActivity extends AppCompatActivity {

    private String token;
    private static final String url = "https://recoletaapi.onrender.com/api/users/";
    private ListView listView;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button logoutBtn = findViewById(R.id.buttonSair);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //recuperando os dados
        UserDataSession userData = new UserDataSession(this);
        token = userData.getJwtToken();

        listView = findViewById(R.id.listView);

        client = new OkHttpClient();

        fetchUsers();

    }

    private void fetchUsers() {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("token", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(AdminActivity.this,
                        "Erro " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.code() == 200) {

                    String jsonResponse = response.body().string();

                    // Parse do JSON usando Gson
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    List<User> users = gson.fromJson(jsonResponse, listType);

                    // Atualizar UI na thread principal
                    runOnUiThread(() -> {
                        UserAdapter adapter = new UserAdapter(AdminActivity.this, users);
                        listView.setAdapter(adapter);
                    });
                }
                else {
                    runOnUiThread(() -> Toast.makeText(AdminActivity.this,
                            "Erro: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}