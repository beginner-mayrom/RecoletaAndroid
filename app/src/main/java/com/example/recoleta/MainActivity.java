package com.example.recoleta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recoleta.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.JarException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    TextInputEditText inputEmail;
    EditText inputPasswd;
    Button login;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button register = (Button) findViewById(R.id.buttonCadastro);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainRegister.class);
                startActivity(intent);
            }
        });

        inputEmail = (TextInputEditText) findViewById(R.id.textInputEmail);
        inputPasswd = (EditText) findViewById(R.id.editTextSenha);
        client = new AsyncHttpClient();
        login = (Button) findViewById(R.id.buttonEntrar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputEmail.getText().toString().isEmpty() || inputPasswd.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = new User();
                    user.setEmail(inputEmail.getText().toString());
                    user.setPassword(inputPasswd.getText().toString());
                    login(user);
                }
            }
        });
    }

    public void login(User user){

        String url = "https://recoletaapi.onrender.com/api/auth/login";
        JSONObject prmt = new JSONObject();

        try{
            prmt.put("email", user.getEmail());
            prmt.put("password", user.getPassword());
        } catch (JSONException e){
            e.printStackTrace();
        }
        StringEntity entity = new StringEntity(prmt.toString(), ContentType.APPLICATION_JSON);

        client.post(MainActivity.this, url, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    Toast.makeText(MainActivity.this, "Login Realizado com Sucesso!", Toast.LENGTH_SHORT).show();
                    //criar o intent para a tela home
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Erro " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}