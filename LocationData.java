package com.software1004developer.gps;

import android.icu.text.DecimalFormat;

public class LocationData
{
	static Double latitudeNETWORK;
	static Double longitudeNETWORK;
	static Double altitudeNETWORK;
	static Double accuracyNETWORK;
	static Double timeNETWORK;
	static Double speedNETWORK;
	public static Double latitudeGPS;
	public static Double longitudeGPS;
	static Double altitudeGPS;
	public static Double accuracyGPS;
	static Double timeGPS;
	static Double speedGPS;
	static Double d;


	//set-методы выполняться только в случае если locationGPS!=null
	public static void setLatitudeNETWORK(double latNETWORK){latitudeNETWORK=latNETWORK;}
	public static void setLongitudeNETWORK(double longNETWORK){longitudeNETWORK=longNETWORK;}
	public static void setAltitudeNETWORK(double altNETWORK){altitudeNETWORK=altNETWORK;}
	public static void setAccuracyNETWORK(double accNETWORK){accuracyNETWORK=accNETWORK;}
	public static void setTimeNETWORK(double tNETWORK){timeNETWORK=tNETWORK;}
	public static void setSpeedNETWORK(double sNETWORK){speedNETWORK=sNETWORK;}
	public static void setLatitudeGPS(double latGPS){latitudeGPS=latGPS;}
	public static void setLongitudeGPS(double lonGPS){longitudeGPS=lonGPS;}
	public static void setAltitudeGPS(double altGPS){altitudeGPS=altGPS;}
	public static void setAccuracyGPS(double accGPS){accuracyGPS=accGPS;}
	public static void setTimeGPS(double tGPS){timeGPS=tGPS;}
	public static void setSpeedGPS(double sGPS){speedGPS=sGPS;}


	//public static double getLatitudeNETWORK(){return latitudeNETWORK;}


	//метод будет возвращать null, пока хотя бы один раз locationGPS и locationNetwork не перестанут быть null
	public static synchronized Double getParam(String param){


		switch (param){

			case "latitudeNETWORK":
				d= latitudeNETWORK;
				break;

			case "longitudeNETWORK":
				d= longitudeNETWORK;
				break;

			case "altitudeNETWORK":
				d= altitudeNETWORK;
				break;

			case "accuracyNETWORK":
				d= accuracyNETWORK;
				break;

			case "timeNETWORK":
				d= timeNETWORK;
				break;

			case "speedNETWORK":
				d= speedNETWORK;
				break;


			case "latitudeGPS":
				d= latitudeGPS;
				break;

			case "longitudeGPS":
				d= longitudeGPS;
				break;

			case "altitudeGPS":
				d= altitudeGPS;
				break;

			case "accuracyGPS":
				d= accuracyGPS;
				break;

			case "timeGPS":
				d= timeGPS;
				break;

			case "speedGPS":
				d= speedGPS;
				break;

		}


		return d;
	}


}
