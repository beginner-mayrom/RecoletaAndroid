package com.example.recoleta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {

    //recuperando os dados
    UserDataSession userData = new UserDataSession(this);
    private final String id = userData.getUserId();
    private final String token = userData.getJwtToken();

    private String selectedRadioButton = "";
    private ImageView returnImg;
    private TextInputEditText nameText, lastNameText;

    private final String url = "https://recoletaapi.onrender.com/api/users/" + id;

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        client = new OkHttpClient();

        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioButton collectRB = (RadioButton) findViewById(R.id.radioButton4);
        RadioButton produceRB = (RadioButton) findViewById(R.id.radioButton3);
        nameText = (TextInputEditText) findViewById(R.id.textInputEditNome3);
        lastNameText = (TextInputEditText) findViewById(R.id.textInputEditSobrenome3);
        Button updateButton = (Button) findViewById(R.id.buttonCadastrar3);
        Button deleteButton = (Button) findViewById(R.id.buttonExcluir);

        selectedRadioButton = userData.getUserType();

        if(Objects.equals(selectedRadioButton, "Coletor")){
            collectRB.setChecked(true);
        } else if (Objects.equals(selectedRadioButton, "Gerador")) {
            produceRB.setChecked(true);
        }

        nameText.setText(userData.getFirstName());
        lastNameText.setText(userData.getLastName());

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.radioButton4){
                    selectedRadioButton = "Coletor";
                }
                else if(checkedId == R.id.radioButton3){
                    selectedRadioButton = "Gerador";
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String lastName = lastNameText.getText().toString();

                if(name.isEmpty() || lastName.isEmpty()){
                    Toast.makeText(UserActivity.this,
                            "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    update(name, lastName, selectedRadioButton);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    private void update(String name, String lastName, String selectedRadioButton) {

        String json = "{ " +
                "\"firstName\": \"" + name + "\", " +
                "\"lastName\": \"" + lastName + "\", " +
                "\"userType\": \"" + selectedRadioButton + "\"" +
                "}";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("token", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(UserActivity.this,
                        "Erro " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.code() == 200){
                    runOnUiThread(() -> Toast.makeText(UserActivity.this,
                            "Cadastro Atualizado com Sucesso!", Toast.LENGTH_SHORT).show());
                }
                else{
                    runOnUiThread(() -> Toast.makeText(UserActivity.this,
                            "Falha ao Atualizar o Cadastro: "
                                    + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void delete(){

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("token", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(UserActivity.this,
                        "Erro " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.code() == 200){
                    runOnUiThread(() -> Toast.makeText(UserActivity.this,
                            "Conta Deletada com Sucesso!", Toast.LENGTH_SHORT).show());

                    Intent intent = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    runOnUiThread(() -> Toast.makeText(UserActivity.this,
                            "Falha ao Atualizar o Cadastro: "
                                    + response.message(), Toast.LENGTH_SHORT).show());
                }

            }
        });

    }


}