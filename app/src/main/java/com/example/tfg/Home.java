package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.tfg.fragments.FiltrarFragment;
import com.example.tfg.fragments.ChatFragment;
import com.example.tfg.fragments.HomeFragment;
import com.example.tfg.fragments.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    BottomNavigationView mBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");



        mBottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        showFragment(new HomeFragment());   //Siempre que abras la aplicacion en la activity home, se mostrara el fragmaent home

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home){    //cuando pulses cualquiera de los fragmentos del menu se mostrara ese fragment
                    showFragment(new HomeFragment());//Fragment Home
                    getSupportActionBar().setTitle("Home");
                }

                if (menuItem.getItemId() == R.id.menu_filtrar){
                    showFragment(new FiltrarFragment());//Fragment Filtrar
                    getSupportActionBar().setTitle("Filtros");
                }

                if (menuItem.getItemId() == R.id.menu_chat){
                    showFragment(new ChatFragment());//Fragment Chat
                    getSupportActionBar().setTitle("Chat");
                }

                if (menuItem.getItemId() == R.id.menu_perfil){
                    showFragment(new PerfilFragment());//Fragment Perfil
                    getSupportActionBar().setTitle("Perfi");
                }

                return true;
            }
        });
    }
    private void  showFragment (Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }
}