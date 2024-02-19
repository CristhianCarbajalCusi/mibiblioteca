package com.example.mibiblioteca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class comprobante extends AppCompatActivity {
    TextView comprobante;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobante);
        comprobante = findViewById(R.id.txtComprobante);
        Intent toComprobante = getIntent();
        String pedido_comprobante = toComprobante.getStringExtra(MainActivity.COMPROBANTE);
        comprobante.setText(pedido_comprobante.toString());
    }


}