package edu.fje.dam2.abel.sopadelletres;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


public class Puntuacions extends Activity {


private final String BASE_DADES = "Puntuacions";
private final String TAULA = "usuari";
private String userName;
private String time;

@Override
public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent getUserData=getIntent();
        userName= getUserData.getStringExtra("username");
        time= getUserData.getStringExtra("playtime");


        ArrayList<String> resultats = new ArrayList<String>();

        SQLiteDatabase baseDades = null;

            try {

                baseDades = this.openOrCreateDatabase(BASE_DADES, MODE_PRIVATE, null);

                baseDades.execSQL("CREATE TABLE IF NOT EXISTS "
                        + TAULA
                        + " (puntuacio VARCHAR, nom INT(8));");

                 if(userName!=null) {
                     baseDades.execSQL("INSERT INTO "
                             + TAULA
                             + " (nom, puntuacio)"
                             + " VALUES ('" + time + "', '" + userName + "');");
                 }

                     Cursor c = baseDades.rawQuery("SELECT nom, puntuacio FROM " + TAULA + " WHERE puntuacio > 1 ORDER BY nom;",
                             null);


                     int columnaNom = c.getColumnIndex("nom");
                     int columnaEdat = c.getColumnIndex("puntuacio");


                     if (c.getCount()!=0) {

                         if (c.isBeforeFirst()) {
                             c.moveToFirst();
                             int i = 0;

                             do {
                                 i++;
                                 String nom = c.getString(columnaNom);
                                 String edat = c.getString(columnaEdat);
                                 resultats.add(i + ". " + edat + "                     " + nom);
                             } while (c.moveToNext());
                         }
                     }

            } finally {
                if (baseDades != null) {
                    baseDades.close();
                }
            }


        Intent intent= new Intent(this,MainActivity.class);
        intent.putExtra("resultats",resultats );
        startActivity(intent);
        }


        }

