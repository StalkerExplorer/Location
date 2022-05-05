package com.software1002developer.gps;

import com.software1002developer.*;

public interface SharedConstants
{
    long MINIMUM_DISTANCE_FOR_UPDATES = 0; // в метрах
    long MINIMUM_TIME_BETWEEN_UPDATES = 500; // в мс

	int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION=11;
	int PERMISSION_REQUEST_CODE=222;
	int SETTINGS_CODE = 123;

	float NO_DATA = 0.0f;

	// Идентификатор уведомления
    int NOTIFY_ID = 101;
	String CHANNEL_ID_1 = "channel_1";
	String CHANNEL_ID_2 = "channel_2";
	// Идентификатор канала
	String CHANNEL_ID = "Cat channel";
	
	String PRIVAC_POLIC="PRIVAC_POLIC";
	
    //массив разметок
	int[] layout=new int[]{
		R.layout.mylocation,//getLastKnownLocation
		R.layout.satellite_list,//список спутников
	    R.layout.sky
	};

	int BEIDOU=0;
	int GALILEO=1;
	int GLONASS=2;
	int GPS=3;
	int IRNSS=4;
	int QZSS=5;
	int SBAS=6;
	int UNKNOWN=7;

}
