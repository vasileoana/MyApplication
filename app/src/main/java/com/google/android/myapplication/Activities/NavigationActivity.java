package com.google.android.myapplication.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.DataBase.Model.Rating;
import com.google.android.myapplication.R;

import java.util.List;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int idUser=-1;
    ImageButton btnSearchProduct, btnSearchIngredient, btnTryOcr, btnAbout;
    UserMethods userMethods=new UserMethods();
    TextView textView;
    View nav_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

               if (getIntent().getExtras().get("tipUtilizator").equals("logat")) {
                   idUser = getIntent().getExtras().getInt("userId");
               }


        btnSearchProduct = (ImageButton) findViewById(R.id.btnSearchProduct);
        btnSearchIngredient = (ImageButton) findViewById(R.id.btnSearchIngredient);
        btnTryOcr = (ImageButton) findViewById(R.id.btnTryOcr);
        btnAbout= (ImageButton) findViewById(R.id.btnAbout);

        btnSearchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchProductActivity.class);
                intent.putExtra("userId", idUser);
                startActivity(intent);
            }
        });

        btnSearchIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchIngredientActivity.class);
                startActivity(intent);
            }
        });

        btnTryOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OcrActivity.class);
                intent.putExtra("tipUtilizator","logat");
                intent.putExtra("userId", idUser);
                startActivity(intent);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        textView= (TextView) header.findViewById(R.id.textView);
        textView.setText(userMethods.selectUserById(idUser).getUsername());

    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.menu_profile) {
            intent = new Intent(getApplicationContext(), ProfileActivity.class);
           intent.putExtra("idUtilizator",idUser);
            startActivity(intent);
        } else if (id == R.id.menu_analyses) {
            intent = new Intent(getApplicationContext(), AnalysesActivity.class);
            intent.putExtra("userId", idUser);
            startActivity(intent);
        } else if (id == R.id.menu_logout) {
            Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent2);        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
