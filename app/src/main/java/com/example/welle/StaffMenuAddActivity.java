package com.example.welle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.welle.ui.MainActivity;
import com.example.welle.ui.staff.StaffBookActivity;
import com.example.welle.ui.staff.StaffMainActivity;
import com.example.welle.ui.staff.StaffMenuActivity;


public class StaffMenuAddActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_menu_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button btn = findViewById(R.id.btnback);
        Button btn2 = findViewById(R.id.btnstaffconfirmmenu);
        Button btn3 = findViewById(R.id.btnstaffnotice);
        Button btn5 = findViewById(R.id.button5);
        Button btn6 = findViewById(R.id.button6);
        Button btn7 = findViewById(R.id.button7);
        Button btn8 = findViewById(R.id.btnstaffconfirmmenu);
        Button btn9 = findViewById(R.id.btnstaffcancelmenu);
        Button btn10 = findViewById(R.id.btnstaffclear);
        Button popupButton = findViewById(R.id.btnstaffullmenu);





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(StaffMenuAddActivity.this, StaffMenuActivity.class);
                startActivity(intent);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ItemFragment01());
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ItemFragment02());
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ItemFragment03());
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();

               Intent intent = new Intent(StaffMenuAddActivity.this, StaffMenuActivity.class);
               startActivity(intent);
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(StaffMenuAddActivity.this, StaffNoticeActivity.class);
                startActivity(intent);
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Toast.makeText(StaffMenuAddActivity.this, "Record is updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StaffMenuAddActivity.this, StaffMenuActivity.class);
                startActivity(intent);
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Toast.makeText(StaffMenuAddActivity.this, "Record is no change!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StaffMenuAddActivity.this, StaffMenuActivity.class);
                startActivity(intent);
            }
        });

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show message is clear
                Toast.makeText(StaffMenuAddActivity.this, "Record is Clear!", Toast.LENGTH_SHORT).show();

            }
        });


        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(StaffMenuAddActivity.this, v);

                MenuInflater inflater = popup.getMenuInflater();

                inflater.inflate(R.menu.staffmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.staffmainch) {
                            Intent intent = new Intent(StaffMenuAddActivity.this, StaffMainActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffnoticech) {
                            Intent intent = new Intent(StaffMenuAddActivity.this, StaffNoticeActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffbookch) {
                            Intent intent = new Intent(StaffMenuAddActivity.this, StaffBookActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffmenuch) {
                            Intent intent = new Intent(StaffMenuAddActivity.this, StaffMenuActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffhelpch) {
                            Toast.makeText(StaffMenuAddActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.staffsignoutch) {
                            Intent intent = new Intent(StaffMenuAddActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    public void loadFragment(Fragment fragment){
        // responsible for all runtime management of fragments
        // including adding, removing, hiding, showing
        // and navigating between fragments
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        // Replace the framelayout with new fragment
        ft.replace(R.id.StaffFrame, fragment);
        ft.commit();
    }


}

