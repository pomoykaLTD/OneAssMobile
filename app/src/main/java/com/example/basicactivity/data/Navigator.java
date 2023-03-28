package com.example.basicactivity.data;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.basicactivity.R;
import com.example.basicactivity.ui.AssemblyOperation;
import com.example.basicactivity.ui.LogIn;
import com.example.basicactivity.ui.Selector;
import com.example.basicactivity.ui.Setting;

public class Navigater {
    private static final Selector frSelector = new Selector();
    private static final LogIn frLogIn = new LogIn();
    private static final Setting frSetting = new Setting();
    private static final AssemblyOperation frAssemblyOperation = new AssemblyOperation();
    public static FragmentManager fragmentManager;
    private static Fragment currentFragment;

    public static Selector getSelector(String task, String strJSONdata){ return frSelector.setContextData(task,strJSONdata); }
    public static LogIn getFrLogIn(){ return frLogIn; }
    public static Setting getSetting(){ return frSetting; }
    public static AssemblyOperation getAssemblyOperation(){ return frAssemblyOperation; }

    private static void setCurrentFragment() {
        //авторизация и настройка не могут быть текущими фреймами, они располагаются сверху.
        if (currentFragment != null && !currentFragment.equals(frLogIn) && !currentFragment.equals(frSetting))
            currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
    }

    public static void addFrame(Fragment fragment) {
        setCurrentFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void replaceFrame(Fragment fragment) {
        setCurrentFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                //.addToBackStack(null)
                .commit();
    }

    public static void removeFrame(Fragment fragment) {
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    public static void onBackPressed(Activity activity) {
        if (currentFragment == null) {
            new AlertDialog.Builder(activity)
                    .setTitle("Подтверждение выхода")
                    .setMessage("Вы уверены, что хотите выйти из приложения?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Здесь можно добавить код для завершения приложения
                            activity.finish();
                        }
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        } else {
            Navigater.replaceFrame(currentFragment);
            currentFragment = null;
        }
    }
}
