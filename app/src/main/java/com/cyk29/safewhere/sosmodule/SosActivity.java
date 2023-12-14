package com.cyk29.safewhere.sosmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cyk29.safewhere.R;
import com.cyk29.safewhere.mapmodule.MapsActivity;

public class SosActivity extends AppCompatActivity {


    Button cancel;
    ImageView panic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        cancel = findViewById(R.id.cancelBT);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        panic = findViewById(R.id.panic_modeIV);
        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.panic_fragment_container, new PanicmodeFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });


    }
}