package com.software1004developer.gps;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.software1004developer.*;
import com.software1004developer.gps.view.*;
import java.util.*;

public class SatelliteAdapter extends SimpleAdapter
{
	
	LayoutInflater lInflater;
	Context context;
	int holo_orange_dark;

	LinearLayout linearLayout;
	RelativeLayout relativeLayout;
	TextView textViewSvid;
	TextView textViewCountry1;
	TextView textViewBaseband;
	MyViewBasebandCn0DbHz myViewBasebandCn0DbHz;
	MyViewElevation myViewElevation;
	MyViewAzimuth myViewAzimuth;
	ImageView imageView;
	
	
	public SatelliteAdapter(Context context, ArrayList data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);

		this.context=context;
		lInflater= lInflater.from(context);
		holo_orange_dark=context. getResources().getColor(android.R.color.holo_orange_dark);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = lInflater.inflate(R.layout.satellite_item, null);
		
		
		linearLayout=convertView.findViewById(R.id.iteitemLinearLayout);
		//relativeLayout=convertView.findViewById(R.id.iteitemRelativeLayout);
		textViewSvid=convertView.findViewById(R.id.textViewSvid);
		textViewCountry1=convertView.findViewById(R.id.textViewCountry1);
		textViewBaseband=convertView.findViewById(R.id.textViewBaseband);
		myViewBasebandCn0DbHz=convertView.findViewById(R.id.myViewBasebandCn0DbHz);
		myViewElevation=convertView.findViewById(R.id.myViewElevation);
		myViewAzimuth=convertView.findViewById(R.id.myViewAzimuth);
		imageView=convertView.findViewById(R.id.imageViewCountry);
		//relativeLayout.setBackgroundDrawable(context. getDrawable(R.drawable.states_shapes_header));
		
		
		myViewBasebandCn0DbHz.setProgress(MainActivity.basebandCn0DbHz[position]);
		
		
		if(MainActivity.usedInFix[position]){
			
			//linearLayout.setBackgroundColor(Color.parseColor("#2e2e2e"));
			textViewSvid.setBackgroundColor(Color.parseColor("#2e2e2e"));
			textViewCountry1.setBackgroundColor(Color.parseColor("#2e2e2e"));
			textViewBaseband.setBackgroundColor(Color.parseColor("#2e2e2e"));
			myViewBasebandCn0DbHz.setBackgroundColor(Color.parseColor("#2e2e2e"));
			myViewElevation.setBackgroundColor(Color.parseColor("#2e2e2e"));
			myViewAzimuth.setBackgroundColor(Color.parseColor("#2e2e2e"));
			myViewElevation.setAngleUsed(MainActivity.elevation[position], true);
			myViewAzimuth.setAzimuth(MainActivity.azimuth[position],true);
			imageView.setBackgroundColor(Color.parseColor("#2e2e2e"));
			textViewSvid.setTextColor(Color.parseColor("#448AFF"));
			textViewCountry1.setTextColor(Color.parseColor("#448AFF"));
			textViewBaseband.setTextColor(Color.parseColor("#448AFF"));
		}else{
			
		    //linearLayout.setBackgroundColor(Color.parseColor("#252525"));
			textViewSvid.setBackgroundColor(Color.parseColor("#252525"));
			textViewCountry1.setBackgroundColor(Color.parseColor("#252525"));
			textViewBaseband.setBackgroundColor(Color.parseColor("#252525"));
			myViewBasebandCn0DbHz.setBackgroundColor(Color.parseColor("#252525"));
			myViewElevation.setBackgroundColor(Color.parseColor("#252525"));
			myViewElevation.setAngleUsed(MainActivity.elevation[position], false);
			myViewAzimuth.setAzimuth(MainActivity.azimuth[position],false);
			myViewAzimuth.setBackgroundColor(Color.parseColor("#252525"));
			imageView.setBackgroundColor(Color.parseColor("#252525"));
			textViewSvid.setTextColor(Color.parseColor("#FE5723"));
			textViewCountry1.setTextColor(Color.parseColor("#FE5723"));
			textViewBaseband.setTextColor(Color.parseColor("#FE5723"));
		}
		
		
		
		
		return super.getView(position, convertView, parent);
	}
	
	
}
