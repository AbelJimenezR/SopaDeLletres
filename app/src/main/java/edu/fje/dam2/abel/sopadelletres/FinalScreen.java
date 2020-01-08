package edu.fje.dam2.abel.sopadelletres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalScreen extends menu implements View.OnClickListener{
    private TextView textView;
    private TextView textView4;
    private Button boto;
    private String userName;
    private  String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_final_screen);

        boto=findViewById(R.id.button3);
        boto.setOnClickListener(this);

        Intent getData=getIntent();
        time= getData.getStringExtra("playtime");
        userName= getData.getStringExtra("username").toUpperCase();

        textView= findViewById(R.id.textView2);
        textView4= findViewById(R.id.textView4);
        textView.setText(time);
        textView4.setText("ENHORABONA " + userName + ", EL TEU TEMPS HA ESTAT DE:");
    }


    public void onBackPressed() {
        guardaPuntuacio();
    }

    @Override
    public void onClick(View view) {
        guardaPuntuacio();
    }

    public void guardaPuntuacio(){
        Intent intent = new Intent(this, Puntuacions.class);
        intent.putExtra("playtime", time);
        intent.putExtra("username",userName);
        startActivity(intent);
        finish();
    }
}