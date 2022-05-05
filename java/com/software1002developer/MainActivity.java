package com.software1002developer;

import android.*;
import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.content.pm.*;
import android.graphics.*;
import android.location.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.text.*;
import android.text.TextUtils.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import androidx.core.app.*;
import androidx.core.content.*;
import androidx.viewpager2.widget.*;
import com.software1002developer.gps.*;
import com.software1002developer.gps.view.*;
import java.util.*;
import java.util.concurrent.*;


import android.view.View.OnClickListener;

public class MainActivity extends Activity implements SharedConstants
{

	
	//поля класса
    // это будет именем файла настроек:
	public static final String APP_PREFERENCES = "mysettings"; 
	SharedPreferences mSettings;
	Editor editor;
	boolean privacy_policy_Boolean;
	//NotificationManager — системный сервис Android, 
	//который управляет всеми уведомлениями
	NotificationManager mNotificationManager;
	PendingIntent resultPendingIntent;
	static Uri soundUriFixGPS;
	static Uri soundUriLostGPS;
	static Uri soundUriPoorGPS;
	static Uri soundError_spaceship;
	static Ringtone r;
	Thread thread;
	MyRun worker;

	SkyView skyView;
	static Context mContext;
	ViewPager2 viewPager;
	ViewPagerAdapter viewPagerAdapter;
	public static LocationManager mLocationManager;
	GnssStatusCallback gnssStatusCallback;
	boolean a,b,c,d;
	boolean poor_gps_boolean;
	ExecutorService executor;
	boolean myBoolean;
	

	public static String provider=new String("");
	static Tone tone;
	public static ArrayList <MySatellite> arraySat;
	StringBuffer sBuilder=new StringBuffer();
	public static int countUsedFix=0;
	static int page=-1;
	public int sort=3;
	int i=0;
	//int j=0;
	int myi;
	int countNO_DATA=0;
	public static int count=0;//количество видимых спутников
	int countText=0;
	float averageCn0DbHz=0;
	float summCn0DbHz=0;
	float averageUseCn0DbHz=0;

	
	
	boolean booleanBEIDOU=true;
	boolean booleanGALILEO=true;
	boolean booleanGLONASS=true;
	boolean booleanGPS=true;
	boolean booleanIRNSS=true;
	boolean booleanQZSS=true;
	boolean booleanSBAS=true;
	boolean booleanUNKNOWN=true;

	String str[];
	int mInt=0;
	int count1=0;
	ArrayList <String> strArray;
	int j=0;
	
	

	//разметка первой страницы (layout mylocation):
	static TextView textViewHeader;//заголовок с текстом "Данные местоположения:"
	static TextView textViewSetting;//textView слева от кнопки "Настройки"
	static Button buttonSetting;//кнопка "Настройки"
	static TextView textViewGPS;//заголовок "GPS"
	static TextView textViewNETWORK;//заголовок "NETWORK"
	//для GPS:
	static MyView myViewLatitudeGPS;//широта GPS
	static MyView myViewLongitudeGPS;//долгота GPS
	static MyView myViewAltitudeGPS;//высота GPS
	static MyView myViewAccuracyGPS;//точность измерения координат GPS
	static MyView myViewTimeGPS;////"время получения координат GPS"
	static MyView myViewSpeedGPS;//"скорость перемещения GPS"
	//для NETWORK:
	static MyView myViewLatitudeNetwork;//широта NETWORK
	static MyView myViewLongitudeNetwork;//долгота NETWORK
	static MyView myViewAltitudeNetwork;//высота NETWORK
	static MyView myViewAccuracyNetwork;//точность измерения координат NETWORK
	static MyView myViewTimeNetwork;//"время получения координат NETWORK"
	static MyView myViewSpeedNetwork;//"скорость перемещения NETWORK"

	static SignalProgress signalProgress;
	static BarGraph barGraph;
	ViewCountSat viewCountSat;
	//разметка первой страницы (layout mylocation)
	static String strSetting=new String();


	//разметка второй станицы
	LinearLayout satelliteHeaderLinearLayout;
	TextView satellitelistTextViewVisible;//textView в заголовке второй стр. Тот который превращается из текста "Working..." в другой текст
	TextView satellitelistTextViewUse;//textView в заголовке второй стр. Тот который превращается из текста "Working..." в другой текст
	TextView satelliteSort;//textView с меняющимся в процессе нажатий текстом "Сортировать по наличию эфемеридных данных"
	TextView satellitelistTextView;//куда постится длинная строка с информацией о спутниках
	ProgressBar progressBar;
	ScrollView scrollView;
	//разметка второй станицы

	//TextView textView;//textView второй стр, создаётся в коде программно, для того чтобы вписать "......"

	//разметка третьй станицы
	BarGraph barGraphSkyBasebandCn0DbHz;
	BarGraph barGraphSkyCn0DbHz;
	TextView skyTextView;//"показывать только GPS и тд."
    ViewCountSat viewCountSatSky;
	//разметка третьй станицы




//>>>>>>РАЗРЕШЕНИЯ RUNTIME>>>>>>>>>>
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public void permission(){//инициируем
		if(hasPermission()){ //sb.append("Выполняем код, потому что есть разрешения");
		metod2();//выполняем остальной код
		} else {//Если разрешения нет, то нам надо его запросить. 
			//Это выполняется методом requestPermissions. 
			ActivityCompat.requestPermissions(this, new String[] {//Manifest.permission.ACCESS_BACKGROUND_LOCATION,
												  //Manifest.permission.ACCESS_COARSE_LOCATION,
												  Manifest.permission.ACCESS_FINE_LOCATION,
												  Manifest.permission.ACCESS_BACKGROUND_LOCATION },
											  REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
	    }
	}


	//>>>проверка наличия разрешения<<<
	//будем вызывать метод перед выполнением любых действий
	//связанных с локацией
	public boolean hasPermission(){
		//Проверка текущего статуса разрешения выполняется методом checkSelfPermission
		//Он вернет константу PackageManager.PERMISSION_GRANTED (если разрешение есть) 
		//или PackageManager.PERMISSION_DENIED (если разрешения нет).
		//Если разрешение есть, значит мы ранее его уже запрашивали, 
		//и пользователь подтвердил его.
		if( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
		   ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED){
			return true;
		}else{
			return false;
		}}
	//>>>


	@Override//при переопределении метода лучше ничего лишнего в код не вписывать
	//особенно какое-нибудь обращение к активности, 
	//например textViewInfo.setText("text")
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION:
				if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED
					&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
						
						//пользователь предоставил нужные разрешения
						metod2();//выполняем остальной код
						
						} 
					
					
				//иначе пользователь отказал в разрешении или выбрал не те (нам нужно в любом режиме)
				else {

					//показываем пользователю диалог, 
					//что разрешение необходимо, и необходимо в любом режиме:
					showDialog(2);

				}
				return;
		}
	}


	//отправляемся в окно настроек программы
	public void openApplicationSettings() {

		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivityForResult(intent, PERMISSION_REQUEST_CODE);
		
    }


	

	@Override
	public Dialog onCreateDialog(int id) {
		
		
		AlertDialog dialog=null;
		AlertDialog.Builder adb;// = new AlertDialog.Builder(MainActivity.this);
		View myView;
		TextView myTextView;
		TextView textView;
		LinearLayout.LayoutParams lParams;
		Button button1;
		Button button2;
		
		switch (id){
			
			case 1:
				adb = new AlertDialog.Builder(MainActivity.this);
				myView= getLayoutInflater().inflate(R.layout.dialog1,null);
				adb.setView(myView);
				// создаем диалог
				dialog=adb.create();
				//dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
				dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rectangle_rounded_all);
				// заголовок (создаем TextView, и устанавливаем в качестве заголовка)
				myTextView=new TextView(MainActivity.this);
				lParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				myTextView.setLayoutParams(lParams);
				myTextView.setText("Политика конфиденциальности");
				myTextView.setTextSize(18);
				myTextView.setTypeface(myTextView.getTypeface(), Typeface.BOLD);
				myTextView.setGravity(Gravity.CENTER);
				myTextView.setPadding(10,10,10,10);
				myTextView.setTextColor(Color.parseColor("#BDBDBD"));//9E9E9E
				myTextView.setShadowLayer(2,2,2,Color.parseColor("#101010"));
				dialog.setCustomTitle(myTextView);

				//найдем textView
				textView=myView.findViewById(R.id.dialog1TextView1);
				textView.setText(Html.fromHtml(getString(R.string.privacy_policy)));
				//найдем кнопки и назначим им обработчик:
				button1= myView.findViewById(R.id.dialog1Button1);
				button2= myView.findViewById(R.id.dialog1Button2);
				button1.setOnClickListener(myButtonDialog);
				button2.setOnClickListener(myButtonDialog);

                //теперь назначим обработчик клавише "назад" (во время открытого диалогового окна, его нужно назначать тут)
				dialog. setOnKeyListener(new DialogInterface.OnKeyListener() {
						@Override
						public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK){
								editor.putBoolean(PRIVAC_POLIC, false);//кладём что не согласились с политикой конфиденциальности
								editor.apply();
								finish();//закрываем программу
								return true;
							}
							return false;
						}
					});
				//return dialog;
				break;
			
			
			case 2:
				adb = new AlertDialog.Builder(MainActivity.this);
				myView= getLayoutInflater().inflate(R.layout.dialog2,null);
				adb.setView(myView);
				// создаем диалог
				dialog=adb.create();
				//dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
				dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rectangle_rounded_all);
				// заголовок (создаем TextView, и устанавливаем в качестве заголовка)
				myTextView=new TextView(MainActivity.this);
				lParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				myTextView.setLayoutParams(lParams);
				myTextView.setText("Разрешение \"Местоположение\" необходимо!");
				myTextView.setTextSize(18);
				myTextView.setTypeface(myTextView.getTypeface(), Typeface.BOLD);
				myTextView.setGravity(Gravity.CENTER);
				myTextView.setPadding(10,10,10,10);
				myTextView.setTextColor(Color.parseColor("#BDBDBD"));//9E9E9E
				myTextView.setShadowLayer(2,2,2,Color.parseColor("#101010"));
				dialog.setCustomTitle(myTextView);

				String message="<font color=#448AFF>"+//заключен между цветовыми тегами
					"Для корректной работы программы необходимо разрешить приложению получать информация о местоположении "+//это текст
					"</font>"+//заключен между цветовыми тегами
					"<font color=#FF0000>"+//красный (был #cc0029)
					"в любом режиме" +
					"</font>";//цвет";

				dialog.setMessage(Html.fromHtml(message));

				//найдем кнопки и назначим им обработчик:
				button1= myView.findViewById(R.id.dialog2Button1);
				button2= myView.findViewById(R.id.dialog2Button2);
				button1.setOnClickListener(myButtonDialog);
				button2.setOnClickListener(myButtonDialog);

				//теперь назначим обработчик клавише "назад" (во время открытого диалогового окна, его нужно назначать тут)
				dialog. setOnKeyListener(new DialogInterface.OnKeyListener() {
						@Override
						public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK){
								finish();//закрываем программу
								return true;
							}
							return false;
						}
					});
				//return dialog;
			break;
			
		}
		
		
		//super.onCreateDialog(id);
		return dialog;
		 
		
    }
	
    //обработчик кнопок диалога:
	OnClickListener myButtonDialog = new OnClickListener(){

		@Override
		public void onClick(View p1)
		{
			switch (p1.getId()){

				
					//privacy_policy//privacy_policy//privacy_policy
				case R.id.dialog1Button1:
					editor.putBoolean(PRIVAC_POLIC, false);//кладём что не согласились с политикой конфиденциальности
					editor.apply();
				    vibracia();
					finish();//закрываем программу
					break;

				case R.id.dialog1Button2:
					editor.putBoolean(PRIVAC_POLIC, true);//кладём что согласились с политикой конфиденциальности
					editor.apply();
					dismissDialog(1);//скрываем диалог
					permission();
					vibracia();
					break;
					//privacy_policy//privacy_policy//privacy_policy
					
					
				case R.id.dialog2Button1:
					dismissDialog(2);
				    vibracia();
					finish();
					break;

				case R.id.dialog2Button2:
					dismissDialog(2);
					openApplicationSettings();
					vibracia();
					break;
			}
		}

	};
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	//>>>>>>РАЗРЕШЕНИЯ RUNTIME>>>>>>>>>>
	
	



	@Override//метод вызовется при возвращении из окна настроек
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		
		
		switch(requestCode){

				//если мы возвращались из манипуляций с разрешениями о местоположении
				//тогда был отправлен PERMISSION_REQUEST_CODE, который сюда пришел
			case PERMISSION_REQUEST_CODE:
				if(hasPermission()){
					
					
					//пользователь предоставил нужные разрешения
					metod2();//выполняем остальной код
					
					//Toaster.toast("hasPermission");
					
				}else{
					
					//пользователь не предоставил нужные разрешения
					showDialog(2);//снова показываем диалог
					//Toaster.toast("else");
					
				}
				break;

				//если мы возвращались из манипуляций с вкл/выкл GPS
				//тогда был отправлен SETTINGS_CODE, который сюда пришел
			case SETTINGS_CODE:
				//надо проверить доступого провайдера
				//и обновить представление нужных view на первой страницы пейджера
				//Проверим "Локацию" в настройках системы. Вкл/Выкл
				provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);





				//текст рядом с кнопкой "Настройки"
				if(provider.equals("")){//если в настройках системы выключено определение местоположения


					viewPager.setUserInputEnabled(false);//блокируем скроллирование страниц viewPager2

					//бегущий текст
					strSetting="Локация выключена в настройках системы"+"\n";
					textViewSetting.setText(strSetting);
					textViewSetting.setSelected(true);
					textViewSetting.setSingleLine(true);
					textViewSetting.setTextColor(Color.parseColor("#FF4908"));
					textViewSetting.setShadowLayer(3,3,3,Color.parseColor("#141414"));
					textViewSetting.setEllipsize(TruncateAt.MARQUEE);
					buttonSetting.setText("Включить");


				}else{//если в настройках системы наоборот, включено определение местоположения

					viewPager.setUserInputEnabled(true);//делаем доступным скроллирование страниц viewPager2

					//обычный текст 
					strSetting="Доступные провайдеры: " + provider+"\n";
					textViewSetting.setText(strSetting);
					textViewSetting.setTextColor(Color.parseColor("#FF5722"));
					textViewSetting.setShadowLayer(2,2,2,Color.parseColor("#141414"));
					textViewSetting.setSingleLine(false);
					textViewSetting.setEllipsize(null);
					buttonSetting.setText("Настройки");

					
					
					
					
					//запускаем сервис
					// используем явный вызов службы
					startForegroundService(
						new Intent(MainActivity.this, MyService.class));
					////В сервисе мы получаем сведения о последнем известном местоположении
					//при этом смотрим двух провайдеров. И инициализируем из сервиса переменные класса LocationData

					MainActivity. mLocationManager.registerGnssStatusCallback(gnssStatusCallback);
					//уведомление в строке состояния:
					mNotificationManager.notify(NOTIFY_ID, getNotification());
				}
				break;
		}
		
		
		if(viewPager!=null){
		//просим адаптер обновить данные:
		viewPagerAdapter.notifyDataSetChanged();
		}
		
		super.onActivityResult(requestCode, resultCode, data);


	}







    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		mContext=getApplicationContext();
		
		//было в metod2
		viewPager=findViewById(R.id.viewPager2);
		viewPagerAdapter=new ViewPagerAdapter();
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.registerOnPageChangeCallback(onPageChangeCallback);//регистрируем Listener
        //было в metod2
		
		if(privacePolicy()){
			permission();
		}else{
			showDialog(1);
		}
		
    }//закрылся onCreate()


	
	
	
	public boolean privacePolicy(){
		
		privacy_policy_Boolean=false;
		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		editor = mSettings.edit();

		//достаём переменную, показывающую пройдено ли privacy_policy
		if(mSettings.contains(PRIVAC_POLIC)) {
			privacy_policy_Boolean= mSettings.getBoolean(PRIVAC_POLIC, false);
		}

		//если privacy policy проходили до этого, то она инициализирована true
		return privacy_policy_Boolean;
	}
	
	
	
	
	
	
	
	
	//metod2//если успешно прошли privacy_policy
	//и уже есть разрешения на доступ к местоположению
	public void metod2(){
		
		/*
		viewPager=findViewById(R.id.viewPager2);
		viewPagerAdapter=new ViewPagerAdapter();
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.registerOnPageChangeCallback(onPageChangeCallback);//регистрируем Listener
        */
		
		//Проверим "Локацию" в настройках системы. Вкл/Выкл
		provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		
		//экземпляр класса с тональником (в нем метод, где есть звук "beep-beep" во время фиксации позиции)
		tone=new Tone(ToneGenerator.TONE_PROP_BEEP,300);

		executor = Executors.newSingleThreadExecutor();//используется внутри gnssStatusCallback (где "плохие условия приёма")

		//LocationManager
		//из официальной документанции:
		//Этот класс предоставляет доступ к системным службам определения местоположения. 
		//Эти службы позволяют приложениям получать периодические обновления о географическом
		//местоположении устройства или получать уведомления, когда устройство приближается 
		//к заданному географическому местоположению.
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gnssStatusCallback=new GnssStatusCallback();

		
			if((!provider.equals(""))){//если GPS включен
			//запускаем сервис
			// используем явный вызов службы
			startForegroundService(
				new Intent(MainActivity.this, MyService.class));
            ////В сервисе мы получаем сведения о последнем известном местоположении
			//при этом смотрим двух провайдеров. И инициализируем из сервиса переменные класса LocationData

			MainActivity. mLocationManager.registerGnssStatusCallback(gnssStatusCallback);
			viewPager.setUserInputEnabled(true);//делаем доступным скроллирование страниц viewPager2
			}//если GPS включен
			else{//иначе GPS выключен
				viewPager.setUserInputEnabled(false);//блокируем скроллирование страниц viewPager2
			}
			
			
		
		
		//каналы для уведомлений//
		//перед созданием каналов
		//получаем notificationManager:
		mNotificationManager = getSystemService(NotificationManager.class);
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
														PendingIntent.FLAG_UPDATE_CURRENT);
		//создаём Uri для звуков
		soundUriFixGPS = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.gps_fix);
		soundUriLostGPS = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.gps_lost);
		soundUriPoorGPS = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.gps_poor);

		soundError_spaceship = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.error_spaceship);

		//затем создаём каналы, по одному для каждого звука уаедомления:
		//https://help.batch.com/en/articles/5671551-how-can-i-use-a-custom-notification-sound-on-android
		createNotificationChannel1();//каналы, - при внесении изменений в ходе разработки требуется переустановка, 
		createNotificationChannel2();//чтобы изменения вступали в силу
		//каналы для уведомлений//
		worker=new MyRun();//класс связан с звуковым уведомлением

		//уведомление в строке состояния:
		//mNotificationManager.notify(NOTIFY_ID, getNotification());
		
	}//metod2//если успешно прошли privacy_policy






	@Override
    protected void onResume() {
        super.onResume();
    }

	@Override
    public void onPause() {
        super.onPause();
    }


	@Override
	public void finish()
	{
		
		
		//в определенных случаях мы дважды проходим finish() (это может вызвать сбой)
		if(mSettings.getBoolean(PRIVAC_POLIC, false)){//если успешно прошли privacy_policy, //то у нас уже запущено много всего
		if(hasPermission()){//но запущено в случае наличия разрешений на местоположение
		
		
//if(!provider.equals("")){//если GPS включен
		//уничтожаем сервис:
		stopService(
			new Intent(MainActivity.this, MyService.class));

		MainActivity. mLocationManager.unregisterGnssStatusCallback(gnssStatusCallback);
		
		
		//playNotification.stop();//уведомление "плохие условия приема"
		if(worker.t!=null){//
			//worker.t!=null если хоть раз выполнялся код в run()
			//хоть раз выполнится код в run(), в случае: executor.execute(worker);
			worker.t.interrupt();
		}

		executor.shutdownNow();// После этого исполнитель перестанет принимать какие-либо новые потоки и завершит все существующие в очереди 
//}////если GPS включен
		
			//Toaster.toast("finish()  " +myCount);
			
			
			super.finish();
		}//но запущено в случае наличия разрешений на местоположение
		}//если успешно прошли privacy_policy
		else{//иначе мы ещё не прошли privacy_policy
			//Toaster.toast("finish()  "+myCount);
			super.finish();//просто закрываем программу, не останавливаем никакие сервисы как выше в коде
		}
		
		super.finish();
		
	}
	
	
	
	
	
//обновлять элементы GUI из дочернего потока нельзя
//мы используем Handler
public static  Handler  handler = new Handler(){
public void handleMessage(Message msg){
super.handleMessage(msg);

switch(msg.what){

case 0:
facadeRenovation();//обновляем view в разметках (view.invalidate())
break;
}
}
};
	
	

	












	





    //каналы, используются в уведомлениях
    //канал1
	private void createNotificationChannel1(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//найти табл. Каналы появились в API 26, но старые устройства будут просто игнорировать данный параметр при вызове конструктора NotificationCompat.Builder.
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID_1, "CHANNEL_NAME_1", NotificationManager.IMPORTANCE_DEFAULT);


			AudioAttributes audioAttributes = new AudioAttributes.Builder()
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.setUsage(AudioAttributes.USAGE_NOTIFICATION)
				.build();


			serviceChannel.setSound(soundUriFixGPS, audioAttributes);
			//создаём канал:
            mNotificationManager.createNotificationChannel(serviceChannel);
		}
	}


    //канал2
	private void createNotificationChannel2(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//найти табл. Каналы появились в API 26, но старые устройства будут просто игнорировать данный параметр при вызове конструктора NotificationCompat.Builder.
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID_2, "CHANNEL_NAME_2", NotificationManager.IMPORTANCE_DEFAULT);


			AudioAttributes audioAttributes = new AudioAttributes.Builder()
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.setUsage(AudioAttributes.USAGE_NOTIFICATION)
				.build();


			serviceChannel.setSound(soundUriLostGPS, audioAttributes);
			//создаём канал:
            mNotificationManager.createNotificationChannel(serviceChannel);
		}
	}
	//каналы, используются в уведомлениях


	/*//метод для показа уведомлений (мы его не вызываем пока)
	public void notification(int countUsedFix){
		// Create PendingIntent
		//Чтобы выполнить какое-либо действие по нажатию на уведомление,
		//необходимо использовать PendingIntent. PendingIntent - это контейнер для Intent. 
		//Этот контейнер может быть использован для последующего запуска вложенного в него Intent.
		//https://startandroid.ru/ru/uroki/vse-uroki-spiskom/509-android-notifications-osnovy.html
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this,
																0, notificationIntent,
																PendingIntent.FLAG_CANCEL_CURRENT);


		// Create Notification
		//Используем билдер, в котором указываем иконку, 
		//заголовок и текст для уведомления. 
		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(this, countUsedFix==0?CHANNEL_ID_2:CHANNEL_ID_1)
			.setSmallIcon(R.drawable.location)
			.setContentTitle("Напоминание")
			.setContentText("Пора покормить кота")
			//.setContent(views);
			.setContentIntent(contentIntent);


		//Методом build получаем готовое уведомление.
		Notification notification =builder.build();


		//когда надо показать уведомление пользователю, вызывается метод notify() 
		//notificationManager'а
		mNotificationManager.notify(NOTIFY_ID, notification);
	}//закрылся public void notification(){*/


	//метод озвучивания "уведомлений"
	public static void playNotificationSound(Uri uriSound) {//public void playNotificationSound

		try {

			if(r!=null){
				r.stop();
				r=null;
			}


			r = RingtoneManager.getRingtone(mContext, uriSound);
			r.play(); 
		} catch (Exception e) {
			//Toaster.toast("playNotificationSound(): \n\n"+e.toString());
		}
	}//закрылся public void playNotificationSound



	//Кнопка "Настройки"
	//Открывает окно настроек, где находится настройка,
	//позволяющая использовать GPS для нахождения местоположения:
	public void OnButtonClick(View v){//назначен кнопке на первой стр
		openSettings();
	}
	//это окно настроек локации на устройстве

	public void openSettings(){
		vibracia();
		Intent gpsOptionsIntent = new Intent(  
            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
		startActivityForResult(gpsOptionsIntent, SETTINGS_CODE);
	}



	//вызывается из MyService при помощи mHandler.sendEmptyMessage(0)
	static protected void facadeRenovation() {//facadeRenovation() {

		if(page==0){//1*если первая страница пейджера сейчас на виду (если она не на виду, и мы будем делать setText - мы получим сбой)
			//обновляем представления
			//GPS:
			myViewLatitudeGPS.invalidate();
			myViewLongitudeGPS.invalidate();
			myViewAltitudeGPS.invalidate();
			myViewAccuracyGPS.invalidate();
			myViewTimeGPS.invalidate();
			myViewSpeedGPS.invalidate();
			//NETWORK:
			myViewLatitudeNetwork.invalidate();
			myViewLongitudeNetwork.invalidate();
			myViewAltitudeNetwork.invalidate();
			myViewAccuracyNetwork.invalidate();
			myViewTimeNetwork.invalidate();
			myViewSpeedNetwork.invalidate();

		}//1*если первая страница пейджера сейчас на виду

    }//facadeRenovation() {







	//Используется в satelliteSort.postDelayed(myAction,30);
	//здесь логика такая: изначально цвет текста textView красный,
	//а через 30 мс он становится "навсегда" #BDBDBD,
	//"навсегда", - это до момента следующего нажатия, для очередной сортировки.
	//текст изначально снова становится красным, и спустя 30 мс он опять
	//становится "навсегда" #696969, и так по кругу.
	//"чтобы не кодить таймер"
	Runnable myActionSky = new Runnable(){
		@Override
		public void run()
		{
			skyTextView.setTextColor(Color.parseColor("#696969"));
		}
	};

	
	
	
	
	
	
	
	public void onclSky(View v){
		
		
		
		

	
		strArray = new ArrayList();
		
		
		
		
		
		
		
		//mInt связан с количеством нажатий (нужен для блока кода внизу: if(j<mInt){j++;}else{j=0;})
		//существующие в данный момент нам нужны:
		for (MySatellite s : arraySat) {//цикл
			
			
			if(booleanBEIDOU&&(s.getConstellationType().equals("BEIDOU"))){
				mInt++;//инкрименируем
				booleanBEIDOU=false;//и сюда больше не попадаем, в случае нахождения ещё "BEIDOU"
				strArray.add("BEIDOU");
			}
			
			if(booleanGALILEO&&(s.getConstellationType().equals("GALILEO"))){
				mInt++;//инкрименируем
				booleanGALILEO=false;//и сюда больше не попадаем, в случае нахождения ещё "GALILEO"
				strArray.add("GALILEO");
			}
			
			if(booleanGLONASS&&(s.getConstellationType().equals("GLONASS"))){
				mInt++;//инкрименируем
				booleanGLONASS=false;//и сюда больше не попадаем, в случае нахождения ещё "GLONASS"
				strArray.add("GLONASS");
			}
			
			if(booleanGPS&&(s.getConstellationType().equals("GPS"))){
				mInt++;//инкрименируем
				booleanGPS=false;//и сюда больше не попадаем, в случае нахождения ещё "GPS"
				strArray.add("GPS");
			}
			
			if(booleanIRNSS&&(s.getConstellationType().equals("IRNSS"))){
				mInt++;//инкрименируем
				booleanIRNSS=false;//и сюда больше не попадаем, в случае нахождения ещё "IRNSS"
				strArray.add("IRNSS");
			}
			
			if(booleanQZSS&&(s.getConstellationType().equals("QZSS"))){
				mInt++;//инкрименируем
				booleanQZSS=false;//и сюда больше не попадаем, в случае нахождения ещё "QZSS"
				strArray.add("QZSS");
			}
			
			if(booleanSBAS&&(s.getConstellationType().equals("SBAS"))){
				mInt++;//инкрименируем
				booleanSBAS=false;//и сюда больше не попадаем, в случае нахождения ещё "SBAS"
				strArray.add("SBAS");
			}
			
			if(booleanUNKNOWN&&(s.getConstellationType().equals("UNKNOWN"))){
				mInt++;//инкрименируем
				booleanUNKNOWN=false;//и сюда больше не попадаем, в случае нахождения ещё "UNKNOWN"
				strArray.add("UNKNOWN");
			}
			
		}//цикл
		
		
		//закончился цикл, mInt инициализирована нужным числом
		//инкрименируем её ещё на 1 (все видимые устройством спутники)
		mInt++;
		strArray.add("ALL");//кладём несуществующий в myBooleanArray ключ 
		
		
		
		str=new String[mInt];//распределяем память массива
		
		//заполняем массив
		for(String s:strArray){
			str[count1]=s;
			count1++;
		}
		
		
		
	    
		//все в массив положим false
		boolean b=false;
		skyView. myBooleanArray.put("BEIDOU",b);
		skyView. myBooleanArray.put("GALILEO",b);
		skyView. myBooleanArray.put("GLONASS",b);
		skyView. myBooleanArray.put("GPS",b);
		skyView. myBooleanArray.put("IRNSS",b);
		skyView. myBooleanArray.put("QZSS",b);
		skyView. myBooleanArray.put("SBAS",b);
		skyView. myBooleanArray.put("UNKNOWN",b);
		//и единственную:
		if(skyView. myBooleanArray.containsKey(str[j])){//если содержит ключ
		skyView. myBooleanArray.put(str[j],true);//инициализируем true
		
			skyTextView.setTextColor(Color.parseColor("#808080"));
			skyTextView.setText("Показывать только спутники "+str[j]);
			skyTextView.postDelayed(myActionSky,70);
			
		
		}else{//если несуществющий ключ:
			b=true;
			skyView. myBooleanArray.put("BEIDOU",b);
			skyView. myBooleanArray.put("GALILEO",b);
			skyView. myBooleanArray.put("GLONASS",b);
			skyView. myBooleanArray.put("GPS",b);
			skyView. myBooleanArray.put("IRNSS",b);
			skyView. myBooleanArray.put("QZSS",b);
			skyView. myBooleanArray.put("SBAS",b);
			skyView. myBooleanArray.put("UNKNOWN",b);
			
			skyTextView.setTextColor(Color.parseColor("#808080"));
			skyTextView.setText("Показывать все видимые устройством спутники");
			skyTextView.postDelayed(myActionSky,70);
			
		}
		
		
			
		
				
		
		
			
			if(j<(mInt-1)){j++;}else{j=0;}
		
			//Toaster.toast( "mInt="+mInt+"\n"+
			               //"j="+j);
		
			
		
		
		mInt=0;
		count1=0;
		strArray=null;
		booleanBEIDOU=true;
		booleanGALILEO=true;
		booleanGLONASS=true;
		booleanGPS=true;
		booleanIRNSS=true;
		booleanQZSS=true;
		booleanSBAS=true;
		booleanUNKNOWN=true;
		
		
		
			
			
			
	


		skyView.invalidate();
		vibracia();
	}
	
	
	
	


	//при листании пейджера нужно находить элементы
	public void findView(){
		//первая стр
		buttonSetting=findViewById(R.id.buttonSetting);
		textViewHeader=findViewById(R.id.textViewHeader);
		textViewSetting=findViewById(R.id.textViewSetting);
		textViewGPS=findViewById(R.id.textViewGPS);
		textViewNETWORK=findViewById(R.id.textViewNETWORK);
		//GPS:
		myViewLatitudeGPS=findViewById(R.id.MyViewLatitudeGPS);
		myViewLongitudeGPS=findViewById(R.id.MyViewLongitudeGPS);
		myViewAltitudeGPS=findViewById(R.id.MyViewAltitudeGPS);
		myViewAccuracyGPS=findViewById(R.id.MyViewAccuracyGPS);
		myViewTimeGPS=findViewById(R.id.MyViewTimeGPS);
		myViewSpeedGPS=findViewById(R.id.MyViewSpeedGPS);
		//NETWORK:
		myViewLatitudeNetwork=findViewById(R.id.MyViewLatitudeNetwork);
		myViewLongitudeNetwork=findViewById(R.id.MyViewLongitudeNetwork);
		myViewAltitudeNetwork=findViewById(R.id.MyViewAltitudeNetwork);
		myViewAccuracyNetwork=findViewById(R.id.MyViewAccuracyNetwork);
		myViewTimeNetwork=findViewById(R.id.MyViewTimeNetwork);
		myViewSpeedNetwork=findViewById(R.id.MyViewSpeedNetwork);

		viewCountSat=findViewById(R.id.mylocationViewCountSat1);
		signalProgress=findViewById(R.id.signalProgress);
		barGraph=findViewById(R.id.barGraph);
		//первая стр


		//вторая стр
		satelliteHeaderLinearLayout=findViewById(R.id.satelliteHeaderLinearLayout1);
		satellitelistTextViewVisible=findViewById(R.id.satellitelistTextViewVisible);
		satellitelistTextViewUse=findViewById(R.id.satellitelistTextViewUse);
		satelliteSort=findViewById(R.id.satelliteSort);
		progressBar=findViewById(R.id.progressBar1);
		scrollView=findViewById(R.id.satellitelistScrollView1);
		satellitelistTextView=findViewById(R.id.satellitelistTextView1);
		//вторая стр


		//третья страница
		skyView=findViewById(R.id.skyView);
		barGraphSkyCn0DbHz=findViewById(R.id.barGraphCn0DbHz);
		barGraphSkyBasebandCn0DbHz=findViewById(R.id.barGraphBasebandCn0DbHz);
		viewCountSatSky=findViewById(R.id.mylocationViewCountSatSky);
		skyTextView=findViewById(R.id.skyTextView1);
		//третья страница
	}


	
	
	
	
	
	
	
	
	
	//Используется в satelliteSort.postDelayed(myAction,30);
	//здесь логика такая: изначально цвет текста textView красный,
	//а через 30 мс он становится "навсегда" #BDBDBD,
	//"навсегда", - это до момента следующего нажатия, для очередной сортировки.
	//текст изначально снова становится красным, и спустя 30 мс он опять
	//становится "навсегда" #696969, и так по кругу.
	//"чтобы не кодить таймер", а то мы там задумали уже класс с таймером подкодить
	Runnable myAction = new Runnable(){
		@Override
		public void run()
		{
			satelliteSort.setTextColor(Color.parseColor("#BDBDBD"));
		}


	};
	//метод ниже это oncl, назначен заголовочному лайауту (id=satelliteHeaderLinearLayout1) во второй стр
	public void satelliteHeaderLayoutOncl(View v){
		
		vibracia();


		if(sort<9){sort++;}else{sort=0;}


		switch(sort){//выполняем различные сортировки листа со списком спутников


				//изначально инициализирована public int sort=3;
			case 0:
				//toast(100, "Сортировать по группам");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать по группам");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 1:
				//toast(100, "Сортировать по азимуту");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать по азимуту");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 2:
				//toast(100, "Сортировать: угол возвышения");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать: угол возвышения");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 3:
				//toast(100, "Сортировать: использовались");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать: использовались");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 4:
				//toast(100, "Сортировать по Svid");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать по Svid");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 5:
				//toast(100, "Сортировать по несущей частоте");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать по несущей частоте");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 6:
				//toast(100, "Сортировать: \n"+
				//"плотность отношения несущей\n"+
				//"к шуму основной полосы");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать: ОНШП");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 7:
				//toast(100, "Сортировать: \n"+
				//"плотность отношения несущей\n"+
				//"к шуму на антенне");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать: ОНША");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 8:
				//toast(100, "Сортировать по наличию альманаха");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать по наличию альманаха");
				satelliteSort.postDelayed(myAction,100);
				break;

			case 9:
				//toast(100, "Сортировать по наличию эфемеридных данных");
				satelliteSort.setTextColor(Color.WHITE);
				satelliteSort.setText("Сортировать по наличию эфемеридных данных");
				satelliteSort.postDelayed(myAction,100);
				break;
		}


		mysort();//вызываем метод сортировки


		
	}
	
	
	
	
	
	
	
	

	//Listener viewPager2 
	ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {//Listener viewPager2 onPageChangeCallback
		
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		super.onPageScrolled(position, positionOffset, positionOffsetPixels);
	}

	@Override
	public void onPageSelected(int position)
	{
		// TODO: Implement this method
		super.onPageSelected(position);
		page=position;//делаем видимой переменную для других методов 
		findView();//находим элементы разметок
		
		
		//обновление view представлений страниц пейджера, приходится делать здесь, где Listener пейджера (это связано с findView())
		if(page==0){//если первая стр
		 //подпишем текст рядом с кнопкой "Настройки", в зависимости от того вкл или выкл локация
		 if(provider.equals("")){//если в настройках системы выключено определение местоположения

		 //бегущий текст
		 strSetting="Локация выключена в настройках системы"+"\n";
		 textViewSetting.setText(strSetting);
		 textViewSetting.setSelected(true);
		 textViewSetting.setSingleLine(true);
		 textViewSetting.setTextColor(Color.parseColor("#FF4908"));
		 textViewSetting.setShadowLayer(3,3,3,Color.parseColor("#141414"));
		 textViewSetting.setEllipsize(TruncateAt.MARQUEE);
		 buttonSetting.setText("Включить");


		 }else{//если в настройках системы наоборот, включено определение местоположения

		 //обычный текст 
		 strSetting="Доступные провайдеры: " + provider+"\n";
		 textViewSetting.setText(strSetting);
		 textViewSetting.setTextColor(Color.parseColor("#FF5722"));
		 textViewSetting.setShadowLayer(2,2,2,Color.parseColor("#141414"));
		 textViewSetting.setSingleLine(false);
		 textViewSetting.setEllipsize(null);
		 buttonSetting.setText("Настройки");

		 }//подпишем текст рядом с кнопкой "Настройки", в зависимости от того вкл или выкл локация
		}//если первая стр
		
		
		if(page==1){
			satelliteHeaderLinearLayout.setBackgroundDrawable(getDrawable(R.drawable.states_shapes_header));
		}
		
	}
	
	
	
	};//закрылся анонимный класс //Listener viewPager2 onPageChangeCallback
	

	
	
	
	

	




	public synchronized void mysort(){
		if(page==1){//если вторая стр


			//сортируем полученные экзепляры класса (на помощь нам приходит реализованный в классе MySatellite компаратор):
			//сортировку производим в зависимости от указанной в satelliteHeaderLinearLayout.setOnClickListener()
			//изначально инициализирована public int sort=3;
			switch (sort){//switch (sort){

				case 0:
					Collections.sort(arraySat, MySatellite.COMPARE_CONSTELLATION_TYPE);
					break;

				case 1:
					Collections.sort(arraySat, MySatellite.COMPARE_AZIMUTH_DEGREES);
					break;

				case 2:
					Collections.sort(arraySat, MySatellite.COMPARE_ELEVATION_DEGREES);
					break;

				case 3:
					Collections.sort(arraySat, MySatellite.COMPARE_USED_IN_FIX);
					break;

				case 4:
					Collections.sort(arraySat, MySatellite.COMPARE_SVID);
					break;

				case 5:
					Collections.sort(arraySat, MySatellite.COMPARE_CARRIER_FREQUENCY);
					break;

				case 6:
					Collections.sort(arraySat, MySatellite.COMPARE_BASEBAND_CN);
					break;

				case 7:
					//плотность отношения несущей к шуму на антенне
					Collections.sort(arraySat, MySatellite.COMPARE_CN0DBHZ);
					break;

				case 8:
					//альманах
					Collections.sort(arraySat, MySatellite.COMPARE_ALMANAC_DATA);
					break;

				case 9:
					//эфемеридные данные
					Collections.sort(arraySat, MySatellite.COMPARE_EPHEMERIS_DATA);
					break;

			}//switch (sort)
			//сортируем полученные экзепляры класса (на помощь нам приходит реализованный в классе MySatellite компаратор):




			//теперь, после сортировки arraylist, цикл for-each:
			for(MySatellite mySat:arraySat){//цикл111


				//соберем в StringBuilder текст:
				//при этом применим html-форматирование строки

				//если спутник использовался в расчёте фиксации положения - заключаем в цветовые теги часть строки:
				sBuilder.append(mySat.getUsedInFix()?"<font color=#448AFF>":"");//заключаем в цветовые теги
				sBuilder.append("Идентификационный номер спутника (Svid): "+mySat.getSvid()+"<br>");//Идентификационный номер спутника (Svid)
				sBuilder.append("Группа спутников: "+mySat.getConstellationType()+"<br>");//Группа
				sBuilder.append("Азимут спутника: "+mySat.getAzimuthDegrees()+"°"+"<br>");//Азимут спутника
				sBuilder.append("Угол возвышения: "+mySat.getElevationDegrees()+"°"+"<br>");//Высота спутника
				sBuilder.append("Несущая частота: "+(mySat.getCarrierFrequencyHz()/1000000)+" МГц"+"<br>");//Несущаю частота
				sBuilder.append("Плотность отношения несущей к шуму основной полосы частот спутника: "+mySat.getBasebandCn0DbHz()+" ДБ-Гц"+"<br>");//Плотность отношения несущей к шуму основной полосы частот спутника
				sBuilder.append("Плотность отношения несущей к шуму на антенне спутника: "+mySat.getCn0DbHz()+" ДБ-Гц"+"<br>");//Плотность отношения несущей к шуму на антенне спутника
				sBuilder.append("Альманах: "+mySat.hasAlmanacData()+"<br>");//hasAlmanacData
				sBuilder.append("Эфемеридные данные: "+mySat.hasEphemerisData()+"<br>");//hasEphemerisData
				sBuilder.append("Использовался ли спутник в расчете самой последней фиксации положения: "+ mySat.usedInFix()+"<br>");//Использовался ли спутник в расчете самой последней фиксации положения
				sBuilder.append("<br><br>");
				sBuilder.append(mySat.getUsedInFix()?"</font>":"");//заключаем в цветовые теги



			}//цикл111
			//в sBuilder у нас в результате получилась длинная строка, с раставленными в нужном порядке сведениями
			//и html-отформатированная

			satellitelistTextView.setText(Html.fromHtml(   sBuilder.toString())   );
			sBuilder.delete(0, Integer.MAX_VALUE);//очищаем содержимое для дальнейшего использования


		}//если вторая стр
	}






	public class MyRun implements Runnable
	{

		
		private Thread t;

		//это ещё основной поток
		
		@Override
		public void run()
		{

			//а здесь уже дочерний
			
			t=Thread.currentThread();

			while(!t.isInterrupted()){
				try
				{
					TimeUnit.SECONDS.sleep(3);

					playNotificationSound(soundUriPoorGPS);

					TimeUnit.SECONDS.sleep(10);
				}
				catch (InterruptedException e)
				{//Toaster.toast("Исключение: "+e);
					t. interrupt();}// повторно сбрасываем состояние



			}

		}




	}




	public Notification getNotification(){
		
		Notification notification;
		
		
		
		
		
		
		if(!provider.equals("")){
		
		// Create Notification
		//Используем билдер, в котором указываем иконку, 
		//заголовок и текст для уведомления. 
		NotificationCompat.Builder builder =
			new NotificationCompat.Builder(this,CHANNEL_ID)
			.setSmallIcon(R.drawable.truiton_short)
			.setContentText(MainActivity.countUsedFix!=0?"Точность: "  +(int)(double)LocationData.accuracyGPS+" метров    " +"«GPS  " +"("+MainActivity.countUsedFix+"\\"+MainActivity.count+")»"  :  "Ожидание связи с  «GPS  " +"("+MainActivity.countUsedFix+"\\"+MainActivity.count+")»")
			//.setUsesChronometer(true)
			.setContentIntent(resultPendingIntent)//вот интент в активити
			//.setContentTitle("Foreground Service")
			.setColor(Color.parseColor("#ff4f00"));

        //Методом build получаем готовое уведомление.
		notification = builder.build();

		}else{//провайде блокирован
			// Create Notification
			//Используем билдер, в котором указываем иконку, 
			//заголовок и текст для уведомления. 
			NotificationCompat.Builder builder =
				new NotificationCompat.Builder(this,CHANNEL_ID)
				.setSmallIcon(R.drawable.truiton_short)
				.setContentText("«GPS выключен»")
				//.setUsesChronometer(true)
				.setContentIntent(resultPendingIntent)//вот интент в активити
				//.setContentTitle("Foreground Service")
				.setColor(Color.parseColor("#ff4f00"));

			//Методом build получаем готовое уведомление.
			notification = builder.build();
			
		}
		
		
		
		return notification;
	}





	//Реализация GnssStatus.Callback
	//Вызывается как версия
	//locationManager.registerGnssStatusCallback(new GnssStatusCallback());
	class GnssStatusCallback extends GnssStatus.Callback
	{


		//GnssStatus было добавлено в уровне API 24
		@Override
		public void onSatelliteStatusChanged(final GnssStatus status) {


			//Quantidade de satelites
			count = status.getSatelliteCount();//Получает общее количество спутников в списке спутников.

			//создадим arrayList в который сложим экземпляры класса MySatellite, 
			//делаем это для удобства дальнейшей сортировки полученных данных:
			arraySat=null;
			arraySat=new ArrayList <MySatellite>();

			//извлекаем циклом сведения о спутниках:
			for(int i = 0; i<count; i++){///123цикл начался

				//Создаем экземпляры класса MySatellite, 
				MySatellite mySatellite=new MySatellite();
				//и инициализируем экземплярные переменные созданных классов
				//по мере интераций цикла:
				mySatellite.setSvid(status.getSvid(i));//Получает идентификационный номер спутника по указанному индексу.
				mySatellite.setConstellationType(status.getConstellationType(i));//Извлекает к группировки каких спутников, относится спутник по указанному индексу.
				mySatellite.setAzimuthDegrees(status.getAzimuthDegrees(i));//Получает азимут спутника по указанному индексу.
				mySatellite.setElevationDegrees(status.getElevationDegrees(i));//Получает высоту спутника по указанному индексу.
				mySatellite.setCarrierFrequencyHz(status.getCarrierFrequencyHz(i));//Получает несущую частоту отслеживаемого сигнала.//Например, это может быть центральная частота GPS для L1 = 1575,45 МГц или L2 = 1227,60 МГц, L5 = 1176,45 МГц, различные каналы GLO и т. д. Значение доступно только в том случае, если hasCarrierFrequencyHz(int)есть true.
				mySatellite.setBasebandCn0DbHz(status.getBasebandCn0DbHz(i));//}//Получает плотность отношения несущей к шуму основной полосы частот спутника с указанным индексом в дБ-Гц.
				mySatellite.setCn0DbHz(status.getCn0DbHz(i));//Извлекает плотность отношения несущей к шуму на антенне спутника с указанным индексом в дБ-Гц.
				mySatellite.setAlmanacData(status.hasAlmanacData(i));//Сообщает, есть ли у спутника с указанным индексом данные альманаха.
				mySatellite.setEphemerisData(status.hasEphemerisData(i));//Сообщает, есть ли у спутника с указанным индексом данные об эфемеридах.
				mySatellite.setUsedInFix(status.usedInFix(i));//Сообщает, использовался ли спутник с указанным индексом в расчете самой последней фиксации положения.

				//по мере прохождения цикла,
				//складываем в arrayList экземпляры класса MySatellite 
				//каждый экземпляр класса теперь содержит сведения об отдельном спутнике
				arraySat.add(mySatellite);


				//по ходу прохождения циклом,
				//выясним сколько всего спутников использовалось в расчете самой последней фиксации положения:
				if(mySatellite.getUsedInFix()){countUsedFix++;}



				//также по ходу прохождения циклом суммируем все значения
				//плотностей отношения несущей к шуму основной полосы частот спутников,
				//а затем поделим на их количество
				//и таким образом выясним среднее значение "условия приёма"
				//затем будем использовать при синтезе речи - плохие условия приёма/хорошие условия приёма
				summCn0DbHz=summCn0DbHz+mySatellite.getCn0DbHz();
				
				//но для правильной математической операции нужно выяснить
				//количество спутников без тех, у которых getBasebandCn0DbHz=0
				//иначе мы неправильно найдём среднее арифметическое
				if(mySatellite.getCn0DbHz()==NO_DATA){
					countNO_DATA++;//найдем сумму тех, у которых getBasebandCn0DbHz=0
				}//а потом найдем разность count-countNO_DATA, и поделим суммарное значение плотностей на эту разность. Получится правильное сред. арифм.


				//идёт цикл


			}///123цикл закончился


			//цикл закончился,
			//считаем среднее значение
			//плотностей отношения несущей к шуму основной полосы частот спутников,
			//(используется в звуковом уведомление "условия приёма")
			if((count-countNO_DATA)!=0){
			averageCn0DbHz=summCn0DbHz/(count-countNO_DATA);//делим суммарное значение плотностей на эту разность
			}
			
			






			//связано
			if((averageCn0DbHz<20)&&(!poor_gps_boolean)){
				poor_gps_boolean=true;//чтобы в блок кода снова не попасть при постоянных вызовах
				if(countUsedFix==0){executor.execute(worker);}//отправляем на выполнение блок кода в run (там с задержкой вызывается метод playNotificationSound(soundUriPoorGPS);)
			}



			if(averageCn0DbHz>20){
				poor_gps_boolean=false;//разблокируем возможность попадание в блок кода выше
				if(worker.t!=null){worker.t.interrupt();}//просим завершиться поток (при этом прервётся цикл while(!t.isInterrupted()), который нам вызывает playNotificationSound())
			}



			if(countUsedFix!=0){
				poor_gps_boolean=false;//разблокируем возможность попадание в блок кода выше
				if(worker.t!=null){worker.t.interrupt();}//просим завершиться поток (при этом прервётся цикл while(!t.isInterrupted()), который нам вызывает playNotificationSound())
			}
			//связано









			

			if(page==0){//если сейчас отображается первая страница
				viewCountSat.invalidate();//кол-спутников
				signalProgress.invalidate();//горизонтальная прогресс-шкала (Ср. ОНПШ dB-Hz)
				barGraph.invalidate();//гистограмма плотность отношения несущей к шуму на антенне 
			}//если сейчас отображается первая страница


			if(page==1){//если сейчас отображается вторая страница
				mysort();//сортируем список на второй странице
			}//если сейчас отображается вторая страница

			
			if(page==2){//если сейчас отображается третья страница
				skyView.invalidate();//обновляем небосвод
				viewCountSatSky.invalidate();//подпишем под небосводом
				barGraphSkyCn0DbHz.invalidate();//гистограмма плотность отношения несущей к шуму на антенне 
				barGraphSkyBasebandCn0DbHz.invalidate();//гистограмма плотность отношения несущей к шуму основной полосы
			}///если сейчас отображается третья страница
			




			//эта замысловатая конструкция с boolean переменными a и b,
			//нужна для того чтобы при переходе через 0 используемых спутников,
			//код срабатывал единожды в каждом из блоков if(a), if(b)
			if(countUsedFix==0){//if(countUsedFix==0){


				if(a){
					b=false;
				}else{
					a=true;
					playNotificationSound(soundUriLostGPS);//нет соединения со спутниками
				}


			}else{
				if(b){
					a=false;
				}else{
					b=true;
					playNotificationSound(soundUriFixGPS);//установлено соединение со спутниками
				}

			}
			//эта замысловатая конструкция с boolean переменными a и b,
			//нужна для того чтобы при переходе через 0 используемых спутников,
			//код срабатывал единожды в каждом из блоков if(a), if(b)







			




               if(page==1){//если вторая стр


				progressBar.setVisibility(View.INVISIBLE);
				//в заголовочный textView надо сделать setText, в зависимости от выбранной в данный момент сортировки:
				satelliteSort.setText(getSortText(sort));
				satellitelistTextViewVisible.setText(Html.fromHtml(getVisibleStr(count)));
				//satellitelistTextViewUse.setText(Html.fromHtml(getUseSatelliteStr(countUsedFix)));


				
				
				
				
			
				if(countUsedFix==0){//если ни один спутник не используется для определения
					//снимаем gravity - параметр
					satellitelistTextViewUse.setGravity(Gravity.NO_GRAVITY);
					//затем
					   //посчитаем ширину текста строки "Ожидаю расчёт фиксации положения.", и сделаем так, чтобы текст расположился по центру
					   String string="Ожидаю расчёт фиксации положения";
					   Paint textPaint = satellitelistTextViewUse.getPaint();
					   float widthText = textPaint.measureText(string);
					   int widthTextView=satellitelistTextViewUse.getWidth();
					   int result=(int)((widthTextView-widthText)/2);//величина отступа
					   satellitelistTextViewUse.setPadding(result,3,3,3);
					   

					i++;

					
					switch (i){


						case 1:
							satellitelistTextViewUse.setText("Ожидаю расчёт фиксации положения.");
							break;

						case 2:
							satellitelistTextViewUse.setText("Ожидаю расчёт фиксации положения..");
							break;

						case 3:
							satellitelistTextViewUse.setText("Ожидаю расчёт фиксации положения...");
							break;

						case 4:
							satellitelistTextViewUse.setText("Ожидаю расчёт фиксации положения....");
							i=0;
							break;
					}

					//если ни один спутник не используется для определения
				}else{//иначе, хотя бы один спутник используется для определения
					//возвращаем gravity-параметр
					satellitelistTextViewUse.setGravity(Gravity.TOP|Gravity.CENTER);
					//возращаем на место отступ:
					satellitelistTextViewUse.setPadding(3,3,3,3);
					satellitelistTextViewUse.setText(Html.fromHtml(getUseSatelliteStr(countUsedFix)));
				}


			}//если вторая стр


			
			
			
			
			
			mNotificationManager.notify(NOTIFY_ID, getNotification());
			
			

            //не забыть их обнулять, а то они насуммируются
			summCn0DbHz=0;
			countNO_DATA=0;
			countUsedFix=0;//обнуляем инициализированную в цикле выше переменную
		}//закрылся метод onSatelliteStatusChanged









		//заголовок второй стр
		//этот метод (getSortText), нужен в классе там где выполняется:
		//satelliteSort.setText(getSortText(sort));
		public String getSortText(int sort){

			String str="";
			//изначально инициализирована public int sort=3;
			switch(sort){//выполняем различные сортировки листа со списком спутников

				case 0:
					str= "Сортировать по группам";
					break;

				case 1:
					str="Сортировать по азимуту";
					break;

				case 2:
					str="Сортировать: угол возвышения";
					break;

				case 3:
					str="Сортировать: использовались";
					break;

				case 4:
					str="Сортировать по Svid";
					break;

				case 5:
					str="Сортировать по несущей частоте";
					break;

				case 6:
					str="Сортировать: ОНШП";
					break;

				case 7:
					str="Сортировать: ОНША";
					break;

				case 8:
					str="Сортировать по наличию альманаха";
					break;

				case 9:
					str="Сортировать по наличию эфемеридных данных";
					break;

			}
			return str;
		}//закрылся метод getSortText



        //заголовок второй стр
		//Метод возвращает строку.
		//в зависимости от кол-ва видимых спутников
		//подкрашивает цифру в строке #03A9F4 или красным
		public String getVisibleStr(int countVisibleSatellite){

			//в зависимости от кол-ва видимых спутников
			//подкрасить цифру цветом #03A9F4 или красным цветом:
			String visibleSat;
			switch (countVisibleSatellite){
				case 0://если 0 в поле зрения:
					visibleSat = 
						"<font color=#448AFF>"+//заключен между цветовыми тегами
						"Всего в поле зрения: "+//это текст
						"</font>"+//заключен между цветовыми тегами
						"<font color=#448AFF>"+//красный (был #cc0029)
						countVisibleSatellite +
						"</font>"//цвет
						;
					break;

				default://во всех остальных случаях:
					visibleSat = 
						"<font color=#448AFF>"+//заключен между цветовыми тегами
						"Всего в поле зрения: "+//это текст
						"</font>"+//заключен между цветовыми тегами
						"<font color=#03A9F4>"+
						countVisibleSatellite +
						"</font>"//цвет
						;
			}
			//в зависимости от кол-ва видимых спутников
			//подкрасить цифру #03A9F4 или красным
			return visibleSat;
		}//закрылся метод public String getVisibleStr



		//Метод возвращает строку.
		//в зависимости от кол-ва используемых спутников
		//подкрашивает цифру в строке #03A9F4 или красным
		public String getUseSatelliteStr(int countUsedFix){

			//в зависимости от кол-ва используемых спутников
			//подкрасить цифру #03A9F4 или красным:
			String useSat;
			switch (countUsedFix){
				case 0://если 0 используются для фиксации местоположения:

					useSat = 
						"<font color=#448AFF>"+//заключен между цветовыми тегами
						"Ожидаю расчёт фиксации положения... "+//это текст
						"</font>"//цвет
						;


					break;

				default://во всех остальных случаях:
					useSat = 
						"<font color=#448AFF>"+//заключен между цветовыми тегами
						"Использовалось в расчете последней фиксации положения: "+//это текст
						"</font>"+//заключен между цветовыми тегами
						"<font color=#03A9F4>"+
						countUsedFix +
						"</font>"//цвет
						;
			}
			//в зависимости от кол-ва используемых спутников
			//подкрасить цифру #03A9F4 или красным

			//в зависимости от кол-ва видимых спутников
			//подкрасить цифру #03A9F4 или красным
			return useSat;
		}//закрылся метод public String getUseSatelliteStr


	}//закрылся класс GnssStatusCallback






	public void vibracia(){
		if(true){
			long mills = 50L;
			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(mills);
			}}}






}
