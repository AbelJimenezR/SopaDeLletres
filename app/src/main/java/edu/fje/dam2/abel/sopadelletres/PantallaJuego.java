package edu.fje.dam2.abel.sopadelletres;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import android.widget.Chronometer;


public class PantallaJuego extends menu {

    private GridView letterBoard;
    private TextView textSelected;
    private ListView listWords;
    private String[] items;
    private ArrayList<String> its2 = new ArrayList<String>();
    private String[] its = {"MANOLO", "MARMOL", "MARIA", "MARRON", "MALAZO", "MANAZA", "MANTON"};
    private ArrayList<String> paraulesTrobades = new ArrayList<String>();
    private static int numColumns = 10;
    private ArrayList<Integer> selectedPositions;
    private String orientation;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<Integer> find;
    private Chronometer crono;
    private int comptaParaules;
    private String userName;
    private int color;
    private int color2;
    private int color3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent getUserName = getIntent();
        userName = getUserName.getStringExtra("username");

        Cursor mCursor = obtenirContactes();

        mCursor.moveToFirst();

        while (mCursor.moveToNext()) {
            if (mCursor.getString(1).length() < 6 && !mCursor.getString(1).contains(" ")) {
                its2.add(mCursor.getString(1));
            }
        }

        Random random = new Random();
        int randnum = random.nextInt(its2.size() - 1) + 1;


        for (int x = 0; x < 7; x++, randnum++) {
            if (randnum == its2.size()) {
                randnum = 0;
            }
            its[x] = its2.get(randnum);


        }

        setContentView(R.layout.activity_pantalla_juego);
        textSelected = findViewById(R.id.textSelected);
        letterBoard = findViewById(R.id.gridView);
        listWords = findViewById(R.id.listView);
        letterBoard.setAdapter(new LetterAdapter(this, its));
        letterBoard.setNumColumns(numColumns);
        selectedPositions = new ArrayList<>();
        find = new ArrayList<>();
        crono = (Chronometer) findViewById(R.id.crono);
        crono.start();
        items = its; //LetterAdapter.getWords();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, its);
        comptaParaules = items.length;
        listWords.setAdapter(listAdapter);

        letterBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                Drawable background = v.getBackground();
                if (background instanceof ColorDrawable)
                    color2 = ((ColorDrawable) background).getColor();

                if (selectedPositions.contains(position)) {

                    if (color2 != color3) {
                        v.setBackgroundColor(color3);
                    } else {
                        v.setBackgroundColor(Color.TRANSPARENT);

                    }

                    int index = -1;
                    for (int i = 0; i < selectedPositions.size(); i++) {
                        if (selectedPositions.get(i).equals(position)) {
                            index = i;
                        }
                    }
                    if (index != -1) {
                        selectedPositions.remove(index);
                    }
                } else if (selectedPositions.isEmpty()
                        || (selectedPositions.size() == 1 && isAdjacentToAll(position))) {
                    if (selectedPositions.size() == 1) {
                        orientation = checkCurrentOrientation(position);
                    }
                    color3 = color2;
                    v.setBackgroundColor(Color.GREEN);

                    selectedPositions.add(position);

                } else if (selectedPositions.size() >= 2 && checkAllowed(position)) {
                    color3 = color2;
                    v.setBackgroundColor(Color.GREEN);
                    selectedPositions.add(position);

                }

                String printThis = "";
                for (int i = 0; i < selectedPositions.size(); i++) {
                    printThis += LetterAdapter.getLetter(selectedPositions.get(i));

                }
                textSelected.setText(printThis);


                for (int i = 0; i < items.length; i++) {
                    if (printThis.equals(items[i].toUpperCase()) && !paraulesTrobades.contains(items[i])) {

                        Toast.makeText(PantallaJuego.this, "Has trobat " + textSelected.getText(), Toast.LENGTH_SHORT).show();
                        paraulesTrobades.add(items[i]);
                        comptaParaules--;
                        Log.i("p", String.valueOf(comptaParaules));
                        View view = listWords.getChildAt(i);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setTextColor(Color.GREEN);
                        Random rnd = new Random();
                        color = Color.argb(200, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                        for (int l = 0; l < selectedPositions.size(); l++) {

                            View viewBoard = letterBoard.getChildAt(selectedPositions.get(l));
                            viewBoard.setBackgroundColor(color);
                        }

                        for (Integer next : selectedPositions) {
                            find.add(next);
                        }

                        selectedPositions.clear();
                        textSelected.setText("");
                    }
                }

                if (comptaParaules == 0) {
                    crono.stop();
                    enviarMissatge();
                    finish();
                }

            }

        });

    }

    private static boolean isAdjacent(int a, int b) {
        int ax = a % numColumns, ay = a / numColumns, bx = b % numColumns, by = b / numColumns;
        return Math.abs(ax - bx) <= 1 && Math.abs(ay - by) <= 1;
    }

    private boolean isAdjacentToAll(int position) {
        boolean adjacent = false;
        for (Integer next : selectedPositions) {
            if (isAdjacent(next, position)) {
                adjacent = true;
                break;
            }
        }
        return adjacent;
    }

    private boolean checkAllowed(int position) {
        int check = 0;
        switch (orientation) {
            case "horizontal":
                check = 1;
                break;
            case "vertical":
                check = numColumns;
                break;
            case "diagonalDown":
                check = numColumns + 1;
                break;
            case "diagonalUp":
                check = numColumns - 1;
                break;
            default:
                Toast.makeText(PantallaJuego.this, "Error", Toast.LENGTH_SHORT).show();
        }

        boolean allow = false;
        for (Integer next : selectedPositions) {
            if (Math.abs(next - position) == check) {
                allow = true;
                break;
            }
        }
        return allow;
    }

    private String checkCurrentOrientation(int position) {
        int test = Math.abs(selectedPositions.get(selectedPositions.size() - 1) - position);

        if (test == 1) {
            orientation = "horizontal";
        } else if (test == numColumns) {
            orientation = "vertical";
        } else if (test == numColumns + 1) {
            orientation = "diagonalDown";
        } else if (test == numColumns - 1) {
            orientation = "diagonalUp";
        } else {
            Toast.makeText(PantallaJuego.this, "Error", Toast.LENGTH_SHORT).show();
        }
        return orientation;
    }


    private Cursor obtenirContactes() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projeccio = new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME};
        return getContentResolver().query(uri, projeccio, null, null, null);
    }

    public void enviarMissatge() {
        Intent intent = new Intent(this, FinalScreen.class);
        String missatge = crono.getText().toString();
        intent.putExtra("playtime", missatge);
        intent.putExtra("username", userName);
        startActivity(intent);
    }


}


