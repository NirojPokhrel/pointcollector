package com.niroj.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.niroj.marriagepointcollector.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


public class SimpleXYPlotActivity extends Activity {
	private XYPlot mPlot;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                                 WindowManager.LayoutParams.FLAG_SECURE);
        
        setContentView(R.layout.simple_x_y_plot);
        setTitle("Performance Graph");
        mPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        Intent intent = getIntent();
        
        ArrayList<String> playerList = intent.getStringArrayListExtra(PLAYERS_NAME_LIST);
        XYSeries[] xySeriesArray = new XYSeries[playerList.size()];
        for( int i=0; i<playerList.size(); i++ ) {
        	String playerIdentifier = PLAYERS_NUMBER_INITIAL + (i+1);
        	ArrayList<Integer> listInt = intent.getIntegerArrayListExtra(playerIdentifier);
        	xySeriesArray[i] = new SimpleXYSeries( listInt, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, playerList.get(i));
        }
//     // Create a couple arrays of y-values to plot:
//        Number[] series1Numbers = {1, 8, 5, 2, 7, 4, 9, 3, 7, 6, 10, 25, 7, 2, 9, 10};
//        Number[] series2Numbers = {4, 6, 3, 8, 2, 10, 2, 1, 3, 4, 9, 4, 2, 5, 9, 0 };
//     // Turn the above arrays into XYSeries':
//        XYSeries series1 = new SimpleXYSeries(
//                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
//                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
//                "Bikalpa");                             // Set the display title of the series
// 
//        // same as above
//        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Niroj");
 
        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
    	String nameInitial = "series";
    	String configureTypeInitial = "line_point_formatter_with_plf";
        for( int i=0; i<playerList.size(); i++ ) {
        	String name = nameInitial + (i+1);
        	LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
        	seriesFormat.setPointLabelFormatter( new PointLabelFormatter());
        	String configureType = configureTypeInitial + (i+1);
        	Class res = R.xml.class;
        	Field field;
			try {
				field = res.getField(configureType);
	        	int drawableId;
				drawableId = field.getInt(null);
	        	seriesFormat.configure(getApplicationContext(), drawableId );
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mPlot.addSeries(xySeriesArray[i], seriesFormat);
        }
//        mPlot.setTicksPerRangeLabel(3);
//        mPlot.getGraphWidget().setDomainLabelOrientation(-45);
	}
	
	public static final String PLAYERS_NAME_LIST = "players_list";
	public static final String PLAYERS_NUMBER_INITIAL = "player_number";
}
