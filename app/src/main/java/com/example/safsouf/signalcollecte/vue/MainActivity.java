package com.example.safsouf.signalcollecte.vue;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.safsouf.signalcollecte.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner locationSpinner;
    EditText locationInput;
    FloatingActionButton addLocation;
    FloatingActionButton showForm;
    Button getInfos;
    List<String> locations = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        locationInput = (EditText) findViewById(R.id.location_input);
        addLocation = (FloatingActionButton) findViewById(R.id.location_add);
        showForm = (FloatingActionButton) findViewById(R.id.show_input);
        getInfos = (Button) findViewById(R.id.get_infos);

        locationInput.setVisibility(View.GONE);
        addLocation.setVisibility(View.GONE);

        locations.add("bus");

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        locationSpinner.setAdapter(adapter);

        showForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForm();
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLocation(locationInput.getText().toString());
            }
        });

        getInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
        }

    public void showForm() {
        //on montre le formulaire
        locationInput.setVisibility(View.VISIBLE);
        addLocation.setVisibility(View.VISIBLE);
    }

    public void addLocation(String newLocation) {
        locations.add(newLocation);
        locationSpinner.setSelection(adapter.getPosition(newLocation));

        //on vide le champs
        locationInput.setText("");
        //on cache le formulaire
        locationInput.setVisibility(View.GONE);
        addLocation.setVisibility(View.GONE);
    } }
