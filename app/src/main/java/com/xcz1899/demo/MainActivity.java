package com.xcz1899.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;


public class MainActivity extends AppCompatActivity {

    private android.widget.SeekBar sbTemp;
    private com.xcz1899.thermometerlib.Thermometer thermometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.thermometer = (com.xcz1899.thermometerlib.Thermometer) findViewById(R.id.thermometer);

        this.sbTemp = (SeekBar) findViewById(R.id.sb_Temp);


        sbTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                thermometer.setCurrentTemp(i-30);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
