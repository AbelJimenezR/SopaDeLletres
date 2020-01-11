package edu.fje.dam2.abel.sopadelletres;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;


public class MainActivity extends menu implements View.OnClickListener {

    private static String userName;
    private Button botoP;
    private Button botoS;
    private ArrayAdapter<String> listAdapter;
    private ListView listUsers;
    private ArrayList<String> items = new ArrayList<String>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent result=getIntent();
        items= result.getStringArrayListExtra("resultats");

        setContentView(R.layout.activity_main);
        listUsers= findViewById(R.id.listPoints);
        listAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        listUsers.setAdapter(listAdapter);
        botoP = findViewById(R.id.buttonPlay);
        botoP.setOnClickListener(this);
        botoS = findViewById(R.id.buttonExit);
        botoS.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this ,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                askUser();
                break;
            case R.id.buttonExit:
                this.finishAffinity();
                System.exit(0);
                break;
        }
    }

    public void askUser(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introdueix el teu nom");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                userName = input.getText().toString();
                Intent intent = new Intent(MainActivity.this, PantallaJuego.class);
                intent.putExtra("username",userName);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}