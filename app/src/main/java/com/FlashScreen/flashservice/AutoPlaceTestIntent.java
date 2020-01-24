package com.FlashScreen.flashservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class AutoPlaceTestIntent extends AppCompatActivity  implements View.OnClickListener {
    Button btn_placeIntent;
    String apiKey = getString(R.string.mapApi_key);
    String TAG="AutoPlaceTestIntent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_place_test_intent);
        btn_placeIntent=(Button)findViewById(R.id.btn_placeIntent);
  btn_placeIntent.setOnClickListener(this);
       /* if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_placeIntent:
            {
                startActivity(new Intent(AutoPlaceTestIntent.this, AutoPlaceTestIntent.class));

              /*  int AUTOCOMPLETE_REQUEST_CODE = 1;
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);*/
            }

        }
    }
}