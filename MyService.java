package com.software1004developer.gps;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.location.*;
import android.media.*;
import android.os.*;
import android.provider.*;
import androidx.core.app.*;
import com.software1004developer.*;
import java.util.concurrent.*;
import com.software1004developer.MainActivity.*;
import android.widget.*;
import android.view.*;

public class MyService extends Service implements SharedConstants
{



    // Идентификатор канала
    //private static String CHANNEL_ID = "Cat channel";
	PendingIntent resultPendingIntent;
	NotificationManager notificationManager;

	//private LocationManager mLocationManager;
    private LocationListener mLocationListener;
	public static Location locationGPS;
	public static Location locationNetwork;
    ExecutorService es;

	String lat,lon,sat;
	String message1="";
	String message2="";
	String message3="";

	//private Handler h;
    //private Runnable r;
	int counter = 0;




	private void createNotificationChannel(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//найти табл. Каналы появились в API 26, но старые устройства будут просто игнорировать данный параметр при вызове конструктора NotificationCompat.Builder.
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Example Service Channel", NotificationManager.IMPORTANCE_DEFAULT);


			AudioAttributes audioAttributes = new AudioAttributes.Builder()
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.setUsage(AudioAttributes.USAGE_NOTIFICATION)
				.build();


			serviceChannel.setSound(null, audioAttributes);
			//создаём канал:
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
		}
	}




	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}



	@Override
    public void onCreate() {
        super.onCreate();
		createNotificationChannel();///
		es = Executors.newFixedThreadPool(1);
		
    }




	/*
	public Notification getNotification(){
		// Create Notification
		//Используем билдер, в котором указываем иконку, 
		//заголовок и текст для уведомления. 
		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(this,CHANNEL_ID)
			.setSmallIcon(R.drawable.truiton_short)
			.setContentText(MainActivity.countUsedFix!=0?LocationData.latitudeGPS+"   "+LocationData.longitudeGPS:  "Ожидание связи с \"GPS\" " +"("+MainActivity.countUsedFix+"\\"+MainActivity.count+")")
			//.setUsesChronometer(true)
			.setContentIntent(resultPendingIntent)//вот интент в активити
			//.setContentTitle("Foreground Service")
			.setColor(Color.parseColor("#ff4f00"));

        //Методом build получаем готовое уведомление.
		Notification notification = builder.build();
		
		return notification;
	}
	*/
	



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


		// Create PendingIntent
		//Чтобы выполнить какое-либо действие по нажатию на уведомление,
		//необходимо использовать PendingIntent. PendingIntent - это контейнер для Intent. 
		//Этот контейнер может быть использован для последующего запуска вложенного в него Intent.
		//https://startandroid.ru/ru/uroki/vse-uroki-spiskom/509-android-notifications-osnovy.html
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
																	  PendingIntent.FLAG_UPDATE_CURRENT);
        // Create Notification
		//Используем билдер, в котором указываем иконку, 
		//заголовок и текст для уведомления. 
		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(this,CHANNEL_ID)
			.setSmallIcon(R.drawable.truiton_short)
			.setContentText(MainActivity.provider.equals("")?  "«GPS выключен»" : "")
			//.setUsesChronometer(true)
			.setContentIntent(resultPendingIntent)//вот интент в активити
			//.setContentTitle("Foreground Service")
			.setColor(Color.parseColor("#ff4f00"));

        //Методом build получаем готовое уведомление.
		Notification notification = builder.build();

        //чтобы показать уведомление и запустить startForeground
		startForeground(NOTIFY_ID, notification);




		MyRun mr = new MyRun();
		es.execute(mr);

		
		// return super.onStartCommand(intent, flags, startId);
		return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
		MainActivity. mLocationManager.removeUpdates(mLocationListener);
		//mLocationManager.unregisterGnssStatusCallback(gnssStatusCallback);
		
    }

	
	
	
	
	


	class MyRun implements Runnable
	{

		public void run() {//public void run() {
			Looper.prepare();//Инициализировать текущий дочерний поток как цикл.
			//____________________________________________________________________




			//LocationListener//LocationListener//LocationListener//LocationListener
			//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		    mLocationListener = new LocationListener() {



				@Override
				synchronized public void onLocationChanged(Location location) {



					//<<<<<<<в дочернем потоке>>>>>>

					//получаем сведения о последнем известном местоположении
					//при этом смотрим двух провайдеров
					//GPS:
					locationGPS = MainActivity. mLocationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					//NETWORK:
					locationNetwork = MainActivity. mLocationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);




					if(locationGPS!=null){
						LocationData.setLatitudeGPS(locationGPS.getLatitude());
						LocationData.setLongitudeGPS(locationGPS.getLongitude());
						LocationData.setAltitudeGPS(locationGPS.getAltitude());
						LocationData.setAccuracyGPS(locationGPS.getAccuracy());
						LocationData.setTimeGPS(locationGPS.getTime());
						LocationData.setSpeedGPS( (locationGPS.getSpeed() *60*60/1000) );//переведем сразу "м/с" в привычные нам "км/ч"
					}

					if(locationNetwork!=null){
						LocationData.setLatitudeNETWORK(locationNetwork.getLatitude());
						LocationData.setLongitudeNETWORK(locationNetwork.getLongitude());
						LocationData.setAltitudeNETWORK(locationNetwork.getAltitude());
						LocationData.setAccuracyNETWORK(locationNetwork.getAccuracy());
						LocationData.setTimeNETWORK(locationNetwork.getTime());
						LocationData.setSpeedNETWORK( (locationNetwork.getSpeed() *60*60/1000) );//переведем сразу "м/с" в привычные нам "км/ч"
					}




					//Toaster.toast(Thread.currentThread().getName());
					//обновлять элементы GUI из дочернего потока нельзя
					//мы используем Handler
					MainActivity. handler.sendEmptyMessage(0);
					//MainActivity. tone.tone();
					
					//mHandler.sendEmptyMessage(0);//обновляем уведомление в строке состояния
					//notificationManager.notify(NOTIFY_ID, builder.build());
					
					
				}




				@Override
				synchronized public void onStatusChanged(String s, int i, Bundle b) {

				}


				synchronized public void onProviderDisabled(String s) {
					
				}



				synchronized public void onProviderEnabled(String s) {

				}


			};

// Регистрируемся для обновлений
			//mLocationManager.registerGnssStatusCallback(gnssStatusCallback);
			MainActivity. mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_FOR_UPDATES, mLocationListener);
			MainActivity. mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_FOR_UPDATES, mLocationListener);										


			//_________________________________________________________
			Looper.loop();//Запустите очередь сообщений в этом потоке.
		}//закрылся public void run() {




	}




}
