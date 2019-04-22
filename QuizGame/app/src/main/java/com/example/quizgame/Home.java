package com.example.quizgame;

import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Home extends AppCompatActivity {

        BottomNavigationView bottomNavigationView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFrag = null;
                    switch(menuItem.getItemId()){
                        case R.id.action_category:
                            selectedFrag = CategoryFragment.newInstance();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout,selectedFrag);
                            transaction.commit();
                            break;
                        case R.id.action_ranking:
                            selectedFrag = RankingFragment.newInstance();
                            FragmentTransaction transactiona = getSupportFragmentManager().beginTransaction();
                            transactiona.replace(R.id.frame_layout,selectedFrag);
                            transactiona.commit();
                            break;
                        case R.id.action_logout:
                        Intent LogOut=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(LogOut);
                            finish();
                             break;
                    }


                    return true;
                }
            });
            setDefaultFragment();
        }

        private void setDefaultFragment() {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,CategoryFragment.newInstance());
            transaction.commit();
        }
    }
