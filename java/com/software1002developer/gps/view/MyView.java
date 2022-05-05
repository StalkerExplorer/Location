package com.software1002developer.gps.view;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.icu.text.*;
import android.util.*;
import android.view.*;
import com.software1002developer.*;
import com.software1002developer.gps.*;


public class MyView extends View
{

	int width,height;
	String text="no data";
	String textTime1,textTime2;
	String param;
	Paint paint,paintUsedInFix, paintNoUsedInFix;
	Rect mTextBoundRect;
	float textWidth,textHeight,textHeightTime, textWidthTime1,textWidthTime2,textWidthTime3,textWidthTime4;
	float x,y,xTime1,xTime2,yTime1,yTime2;
	Double myDouble;
	DecimalFormat decimalFormat;
	SimpleDateFormat df1;
	SimpleDateFormat df2;

	String attr[];

	public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyView);
		param = a.getString(R.styleable.MyView_param);
		a.recycle();

        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyView);
		param = a.getString(R.styleable.MyView_param);
		a.recycle();

        init();
    }


	public void init()
	{
		paint = new Paint();
		paint.setColor(Color.parseColor("#448AFF"));
		paint.setShadowLayer(2,2,2,Color.parseColor("#101010"));
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);
		paint.setTextSize(28);

		paintUsedInFix = new Paint();
		paintUsedInFix.setColor(Color.parseColor("#448AFF"));
		paintUsedInFix.setShadowLayer(2,2,2,Color.parseColor("#101010"));
		paintUsedInFix.setStyle(Paint.Style.FILL);
		paintUsedInFix.setAntiAlias(true);
		paintUsedInFix.setSubpixelText(true);
		paintUsedInFix.setStrokeWidth(3);
		paintUsedInFix.setTextSize(28);

		paintNoUsedInFix = new Paint();
		paintNoUsedInFix.setColor(Color.parseColor("#448AFF"));//#cc0029#696969
		paintNoUsedInFix.setShadowLayer(2,2,2,Color.parseColor("#101010"));
		paintNoUsedInFix.setStyle(Paint.Style.FILL);
		paintNoUsedInFix.setAntiAlias(true);
		paintNoUsedInFix.setSubpixelText(true);
		paintNoUsedInFix.setStrokeWidth(3);
		paintNoUsedInFix.setTextSize(28);


		mTextBoundRect = new Rect();

		//это массив для сравнения в цикле (в onDraw()) установленного атрибута у этого View в разметке
		//чтобы из класса LocationData получить нужный параметр и нарисовать в onDraw()
		attr=new String[12];
		attr[0]="latitudeNETWORK";
		attr[1]="longitudeNETWORK";
		attr[2]="altitudeNETWORK";
		attr[3]="accuracyNETWORK";
		attr[4]="timeNETWORK";
		attr[5]="speedNETWORK";
		attr[6]="latitudeGPS";
		attr[7]="longitudeGPS";
		attr[8]="altitudeGPS";
		attr[9]="accuracyGPS";
		attr[10]="timeGPS";
		attr[11]="speedGPS";

		decimalFormat= new DecimalFormat("#.########");
		df1 = new SimpleDateFormat("yyyy-MM-dd");
		df2 = new SimpleDateFormat("HH:mm:ss");

		// Подсчитаем размер текста
		//paint.getTextBounds("2022-04-21", 0, text.length(), mTextBoundRect);
		//высота текста строки:
		//textHeightTime = mTextBoundRect.height();
		//ширина текста строки с временем:
		textWidthTime1 = paint.measureText("2022-04-21");
		textWidthTime2 = paint.measureText("23.32.08");
		textWidthTime3 = paint.measureText("Нет");
		textWidthTime4 = paint.measureText("данных");
	}



	public void drawView(Canvas canvas){
		width=canvas.getWidth();
		height=canvas.getHeight();



		for(String str:attr){


			if(param.equals(str)){
				myDouble=LocationData.getParam(param);


				//но если в параметрах View в разметке указано время
				//то нужно будет записывать текст в две строки:
				//2022-04-21
				//23.32.08
				if(param.equals("timeGPS")|param.equals("timeNETWORK")){

					if(myDouble!=null){//если в сервисе locationGPS и locationNetwork перестали быть null
						textTime1=df1.format((double)myDouble);
						textTime2=df2.format((double)myDouble);
					}else{textTime1="Нет";textTime2="данных";}


				}else{


					if(myDouble!=null){//если в сервисе locationGPS и locationNetwork перестали быть null
						text=decimalFormat.format( (double)myDouble );
					}else{text="Нет данных";}


				}





				break;}


		}

		//text=LocationData.getParam(param);

		//if(text!=null){//1*



		// Подсчитаем размер текста
		paint.getTextBounds(text, 0, text.length(), mTextBoundRect);
		//высота текста строки:
		textHeight = mTextBoundRect.height();
		//ширина текста строки:
		textWidth = paint.measureText(text);

		x=(width-textWidth)/2;//начальная точка для текста, чтобы текст расположился по центру view
		y=(height/2) + (textHeight/2);//для центра по вертикали




		if(param.equals("timeGPS")|param.equals("timeNETWORK")){
			//но если в параметрах View в разметке указано время
			//то нужно будет записывать текст в две строки:
			//2022-04-21
			//23.32.08
			//

		    //canvas.drawText(textTime1,x,y,paint);


			yTime1=(height/2) - (textHeight/2);//для центра по вертикали 1 строки
		    yTime2=(height/2) + (textHeight) ;//для центра по вертикали 2 строки




			//будут разные начальные точки для текста
			//2022-04-21
			// 23.32.08
			//и текста 
			//Нет
			//данных
			if(myDouble!=null){//значит у нас есть данные
				xTime1=(width-textWidthTime1)/2;//начальная точка для текста, чтобы текст расположился по центру view
				xTime2=(width-textWidthTime2)/2;//начальная точка для текста, чтобы текст расположился по центру view
			}else{
				xTime1=(width-textWidthTime3)/2;//начальная точка для текста, чтобы текст расположился по центру view
				xTime2=(width-textWidthTime4)/2;//начальная точка для текста, чтобы текст расположился по центру view
			}



			canvas.drawText(textTime1,xTime1,yTime1,getPaint(myDouble));
			canvas.drawText(textTime2,xTime2,yTime2,getPaint(myDouble));


		}else{

			canvas.drawText(text,x,y,getPaint(myDouble));


		}


	}





	public void drawNoData(Canvas canvas){

		width=canvas.getWidth();
		height=canvas.getHeight();

		text="Нет данных";
		// Подсчитаем размер текста
		paint.getTextBounds(text, 0, text.length(), mTextBoundRect);
		//высота текста строки:
		textHeight = mTextBoundRect.height();
		//ширина текста строки:
		textWidth = paint.measureText(text);


		//если в параметрах View в разметке указано время
		//то нужно будет записывать текст в две строки:
		//2022-04-21
		//23.32.08
		if(param.equals("timeGPS")|param.equals("timeNETWORK")){

			textTime1="Нет";
			textTime2="данных";

			yTime1=(height/2) - (textHeight/2);//для центра по вертикали 1 строки
		    yTime2=(height/2) + (textHeight) ;//для центра по вертикали 2 строки

			xTime1=(width-textWidthTime3)/2;//начальная точка для текста, чтобы текст расположился по центру view
			xTime2=(width-textWidthTime4)/2;//начальная точка для текста, чтобы текст расположился по центру view

			canvas.drawText(textTime1,xTime1,yTime1,paint);
			canvas.drawText(textTime2,xTime2,yTime2,paint);

		}else{

			text="Нет данных";
			x=(width-textWidth)/2;//начальная точка для текста, чтобы текст расположился по центру view
			y=(height/2) + (textHeight/2);//для центра по вертикали
			canvas.drawText(text,x,y,paint);

		}

	}


	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//canvas.drawColor(Color.parseColor("#212121"));
		if(MainActivity.provider.equals("")){
			//canvas.drawColor(Color.parseColor("#212121"));
			drawNoData(canvas);
		}else{
			drawView(canvas);
		}



	}



	//}//1*



	public Paint getPaint(Double myDoub){

		if(myDoub==null){
			paint=paintNoUsedInFix;
		}



		if(myDoub!=null){
			paint=paintUsedInFix;
		}

		return paint;
	}



}


