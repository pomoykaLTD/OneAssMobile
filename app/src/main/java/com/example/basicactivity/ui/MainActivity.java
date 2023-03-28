package com.example.basicactivity.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.basicactivity.R;
import com.example.basicactivity.data.ConnTask;
import com.example.basicactivity.data.Init;
import com.example.basicactivity.data.ServConnector;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.basicactivity.data.StorageData;
import com.example.basicactivity.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Base64;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ServConnector {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;
    private String logIn;
    public Boolean status;
    public String result;
    //private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //вспоминаем сохраненные значения
        StorageData.pref=getSharedPreferences(Init.sharedKey,MODE_PRIVATE);
        logIn=StorageData.pref.getString(Init.sharedKey_LogIn,"");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //фреймы
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //проверка сохраненной авторизации и автопереход к следующему фрейму
        if(!Objects.equals(logIn, "")){
            //Асихронный поток подключения к сервису
            new ConnTask(this,Init.servURL){
                @Override
                public void onPostExecute() {
                    if(status){
                        Toast.makeText(getApplicationContext(),R.string.goodLogIn, Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_FirstFragment_to_fragmentOperation);
                    }
                    else Toast.makeText(getApplicationContext(),R.string.badLogIn, Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }

        //плавающая кнопка
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(navController.getCurrentDestination().getId()!=R.id.FirstFragment){
            //noinspection SimplifiableIfStatement
            if (id == R.id.LogOn) {
                navController.navigate(R.id.FirstFragment);
                //navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
                return true;
            } else if (id == R.id.LogOff) {

                //удаляет данные авторизации
                logIn="";
                StorageData.removeKey(Init.sharedKey_LogIn);
//                SharedPreferences.Editor editor = StorageData.pref.edit();
//                editor.remove(Init.sharedKey_LogIn);
//                editor.apply();

                navController.navigate(R.id.FirstFragment);
                //navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void setLogIn(String name,String pass) {
        String userpass = name + ":" + pass;
        //String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            logIn = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        }
    }

    @Override
    public String getLogIn() { return logIn; }

    @Override
    public void setStatus(Boolean status) { this.status =status; }

    @Override
    public Boolean getStatus() { return status; }

    @Override
    public void setResult(String result) { this.result=result; }

    @Override
    public String getResult() { return result; }

    private void saveData(){
        StorageData.saveData(Init.sharedKey_LogIn,getLogIn());
//        SharedPreferences.Editor editor=pref.edit();
//        editor.putString(Init.sharedKey_LogIn,getLogIn());
//        editor.apply();
    }

}