package com.example.recoleta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText inputEmail, inputName, inputLastName;
    private EditText inputPasswd;
    private Button registerButton;
    private RadioGroup group;

    private String selectedRadioButton = "";

    private static final String url = "https://recoletaapi.onrender.com/api/auth/register";

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (TextInputEditText) findViewById(R.id.textInputEditEmail2);
        inputName = (TextInputEditText) findViewById(R.id.textInputNome);
        inputLastName = (TextInputEditText) findViewById(R.id.textInputSobrenome);
        inputPasswd = (EditText) findViewById(R.id.editTextSenha);
        registerButton = (Button) findViewById(R.id.buttonCadastrar);
        group = (RadioGroup) findViewById(R.id.radioGroup);
        client = new OkHttpClient();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.radioButton){
                    selectedRadioButton = "Coletor";
                }
                else if(checkedId == R.id.radioButton2){
                    selectedRadioButton = "Gerador";
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String name = inputName.getText().toString();
                String lastName = inputLastName.getText().toString();
                String password = inputPasswd.getText().toString();

                if(email.isEmpty() || name.isEmpty() || lastName.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,
                            "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    register(email, name, lastName, password, selectedRadioButton);
                }
            }
        });
    }

    private void register(String email, String name, String lastName,
                          String password, String selectedRadioButton) {

        String json = "{ " +
                "\"firstName\": \"" + name + "\", " +
                "\"lastName\": \"" + lastName + "\", " +
                "\"email\": \"" + email + "\", " +
                "\"password\": \"" + password + "\", " +
                "\"userType\": \"" + selectedRadioButton + "\"" +
                "}";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this,
                        "Erro " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.code() == 200) {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this,
                            "Cadastro Realizado com Sucesso!", Toast.LENGTH_SHORT).show());

                }
                else{
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this,
                            "Falha ao Realizar o Cadastro: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}