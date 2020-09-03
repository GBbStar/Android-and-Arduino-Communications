package org.techtown.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.LineChart;

import java.io.Serializable;
import java.util.ArrayList;


public class SecondActivity extends AppCompatActivity implements Serializable {
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Intent intent = getIntent();
        ArrayList<Object> templist = (ArrayList<Object>) intent.getSerializableExtra("temp");
        ArrayList<Object> humidlist = (ArrayList<Object>) intent.getSerializableExtra("humid");


        chart = findViewById(R.id.lineChart);
        ArrayList <Entry> tempvalues = new ArrayList<>();
        ArrayList <Entry> humidvalues = new ArrayList<>();

        for (int i = 0; i < templist.size(); i++) {
            float val = Float.parseFloat(templist.get(i).toString());
            tempvalues.add(new Entry(i, val)); }

        for (int i = 0; i < humidlist.size(); i++) {
            float val = Float.parseFloat(humidlist.get(i).toString());
            humidvalues.add(new Entry(i, val)); }

        LineData chartData = new LineData();
        LineDataSet set1 = new LineDataSet(tempvalues, "온도");
        set1.setCircleColor(Color.RED);
        set1.setColor(Color.RED);
        chartData.addDataSet(set1);

        LineDataSet set2 = new LineDataSet(humidvalues, "습도");
        set2.setCircleColor(Color.BLUE);
        set2.setColor(Color.BLUE);
        chartData.addDataSet(set2);

        chart.setData(chartData);


        chart.invalidate();



    }

}

