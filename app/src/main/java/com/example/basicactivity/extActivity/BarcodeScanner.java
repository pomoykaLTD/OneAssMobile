package com.example.basicactivity.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basicactivity.databinding.BarcodeScannerBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarcodeScannerBinding binding=BarcodeScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //refValue=getIntent().getExtras().getString(AssemblyOperation.REF);

        // Создаем объект IntentIntegrator и вызываем метод initiatieScan() для запуска сканирования
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    // Метод, который будет вызван после сканировании
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Получаем результат сканирования
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            String contents="";
            if (result.getContents() == null) {
                Toast.makeText(this, "Результат сканирования пустой", Toast.LENGTH_LONG).show();
            } else {
                contents = result.getContents();
                Toast.makeText(this, "Содержимое штрих-кода: " + contents, Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent();
            intent.putExtra("RESULT", String.valueOf(result));
            intent.putExtra("BARCODE", contents);
            //intent.putExtra(AssemblyOperation.REF, refValue);
            setResult(Activity.RESULT_OK, intent);
            finish();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
