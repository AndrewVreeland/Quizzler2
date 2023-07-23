package com.study.quizzler2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;


public class MainActivity extends AppCompatActivity {

    CircleMenu circleMenu;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout=findViewById(R.id.constraint_layout);
        circleMenu=findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.mipmap.menu,R.mipmap.cancel)
                .addSubMenu(Color.parseColor("#88bef5"),R.mipmap.home)
                .addSubMenu(Color.parseColor("#83e85a"),R.mipmap.animals)
                .addSubMenu(Color.parseColor("#FF4B32"),R.mipmap.history)
                .addSubMenu(Color.parseColor("#ba53de"),R.mipmap.games)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.mipmap.music)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        switch(index)
                        {
                            case 0:
                                Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();

                                break;
                            case 1:
                                Toast.makeText(MainActivity.this, "animals", Toast.LENGTH_SHORT).show();

                                break;
                            case 2:
                                Toast.makeText(MainActivity.this, "history", Toast.LENGTH_SHORT).show();

                                break;
                            case 3:
                                Toast.makeText(MainActivity.this, "games", Toast.LENGTH_SHORT).show();

                                break;
                            case 4:
                                Toast.makeText(MainActivity.this, "music", Toast.LENGTH_SHORT).show();

                                break;
                        }
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
    }
}