package com.example.recoleta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //recuperando os dados
        UserDataSession userData = new UserDataSession(this);

        ImageView profileImgView = (ImageView) findViewById(R.id.imageView2);
        ImageView altImgView = (ImageView) findViewById(R.id.imageView3);

        profileImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(HomeActivity.this,
                        "Bem-Vindo, " + userData.getFirstName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        altImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }
}