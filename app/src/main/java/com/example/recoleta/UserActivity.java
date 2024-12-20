package com.example.recoleta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {

    private String token;
    private Boolean isAdmin1, isAdmin2;
    private String selectedRadioButton;
    private String url;
    private TextInputEditText nameText, lastNameText;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        UserDataSession userData = new UserDataSession(this);
        isAdmin1 = userData.isAdmin();
        isAdmin2 = intent.getBooleanExtra("is_admin", false);

        String id;
        String firstName;
        String lastName;

        if(isAdmin1){
            token = userData.getJwtToken();

            id = intent.getStringExtra("user_id");
            firstName = intent.getStringExtra("first_name");
            lastName = intent.getStringExtra("last_name");
            selectedRadioButton = intent.getStringExtra("user_type");
        }
        else{
            id = userData.getUserId();
            token = userData.getJwtToken();
            selectedRadioButton = userData.getUserType();
            firstName = userData.getFirstName();
            lastName = userData.getLastName();
        }


        client = new OkHttpClient();
        url = "https://recoletaapi.onrender.com/api/users/" + id;

        ImageView returnImg = (ImageView) findViewById(R.id.imageView2);

        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                if(isAdmin1){
                    intent = new Intent(UserActivity.this, AdminActivity.class);
                }
                else{
                    intent = new Intent(UserActivity.this, HomeActivity.class);
                }
                startActivity(intent);
            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioButton collectRB = (RadioButton) findViewById(R.id.radioButton4);
        RadioButton produceRB = (RadioButton) findViewById(R.id.radioButton3);
        nameText = (TextInputEditText) findViewById(R.id.textInputEditNome3);
        lastNameText = (TextInputEditText) findViewById(R.id.textInputEditSobrenome3);
        Button updateButton = (Button) findViewById(R.id.buttonCadastrar3);
        Button deleteButton = (Button) findViewById(R.id.buttonExcluir);


        if(Objects.equals(selectedRadioButton, "Coletor")){
            collectRB.setChecked(true);
        } else if (Objects.equals(selectedRadioButton, "Gerador")) {
            produceRB.setChecked(true);
        }

        nameText.setText(firstName);
        lastNameText.setText(lastName);

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
                String firstName = nameText.getText().toString();
                String lastName = lastNameText.getText().toString();

                if(firstName.isEmpty() || lastName.isEmpty()){
                    Toast.makeText(UserActivity.this,
                            "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    update(firstName, lastName, selectedRadioButton);
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

    private void update(String firstName, String lastName, String selectedRadioButton) {

        String json = "{ " +
                "\"firstName\": \"" + firstName + "\", " +
                "\"lastName\": \"" + lastName + "\", " +
                "\"userType\": \"" + selectedRadioButton + "\"" +
                "}";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("authorization", "Bearer " + token)
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

                    // Atualizar os dados em SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("firstName", firstName);
                    editor.putString("lastName", lastName);
                    editor.putString("user_type", selectedRadioButton);
                    editor.apply();

                }
                else if (response.code() == 403) {
                    runOnUiThread(() -> Toast.makeText(UserActivity.this,
                            "Por favor, Realize o Login Novamente!", Toast.LENGTH_SHORT).show());

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

    private void delete(){

        if(isAdmin2){

            runOnUiThread(() -> Toast.makeText(UserActivity.this,
                    "O usuário administrador não pode ser deletado.", Toast.LENGTH_SHORT).show());
            return;
        }

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("authorization", "Bearer " + token)
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

                    if(!isAdmin1){
                        Intent intent = new Intent(UserActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }
                else if (response.code() == 403) {
                    runOnUiThread(() -> Toast.makeText(UserActivity.this,
                            "Por favor, Realize o Login Novamente!", Toast.LENGTH_SHORT).show());

                    Intent intent = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    runOnUiThread(() -> Toast.makeText(UserActivity.this,
                            "Falha ao Deletar o Cadastro: "
                                    + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}