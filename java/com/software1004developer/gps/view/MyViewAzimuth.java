package com.software1004developer.gps.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class MyViewAzimuth extends View
{

	float width, height;
	float f=0;
	float proggress;
	float length;
	Paint paint, paintUsedInFix;
	String text;
	private Rect mTextBoundRect = new Rect();
	boolean usedInFix;

	public MyViewAzimuth(Context context) {
        super(context);
        init();
    }

    public MyViewAzimuth(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewAzimuth(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


	public void init()
	{
		paint = new Paint();
        paint.setColor(Color.parseColor("#FF5722"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
		paint.setStrokeWidth(2.0f);
		paint.setTextSize(20);

		paintUsedInFix = new Paint();
        paintUsedInFix.setColor(Color.parseColor("#448AFF"));
        paintUsedInFix.setStyle(Paint.Style.STROKE);
        paintUsedInFix.setAntiAlias(true);
		paintUsedInFix.setStrokeWidth(2.0f);
		paintUsedInFix.setTextSize(20);


	}




	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		//canvas.drawColor(Color.BLACK);

		width = canvas. getWidth();
		height = canvas. getHeight();


		float startX=0;
		float startY=0;

		length=height/3;
		canvas.save();
		canvas.translate(width/2, height/3);


		//стрела азимута
		//верхняя часть стрелы
		float stopX=(float)Math.cos(f *  Math.PI/180)*length;
		float stopY=(float)Math.sin(f  *  Math.PI/180)*length;
		canvas.drawLine(startX,startY,stopX,stopY,usedInFix?paintUsedInFix:paint);

		//нижняя часть стрелы
		stopX=-(float)Math.cos(f *  Math.PI/180)*length;
		stopY=-(float)Math.sin(f  *  Math.PI/180)*length;
		canvas.drawLine(startX,startY,stopX,stopY,usedInFix?paintUsedInFix:paint);





		//наконечник стрелы
		float pts[]={
			(float)Math.cos(f *  Math.PI/180)*(length-5),
			(float)Math.sin(f  *  Math.PI/180)*(length-5),

			(float)Math.cos((f+50) *  Math.PI/180)*(height/40),
			(float)Math.sin((f+50)  *  Math.PI/180)*(height/40),

			(float)Math.cos(f *  Math.PI/180)*(length-5),
			(float)Math.sin(f  *  Math.PI/180)*(length-5),

			(float)Math.cos((f-50) *  Math.PI/180)*(height/40),
			(float)Math.sin((f-50)  *  Math.PI/180)*(height/40),

		};

		canvas.drawLines(pts,usedInFix?paintUsedInFix:paint);


		text=""+proggress+"°";
		float x=0;
		float y=0;
		//считаем середину по горизонтали:
		float textLenght = paint.measureText(text);//длина текста
		x=((width/2-(width/2))-(textLenght/2));//начальная точка для записи текста, чтобы текст оказался посередине (холст у нас уехал вправо на величину width/2 (мы делали translate), поэтому мы -width/5)
		//считаем середину для нижней 1/3 части по вертикали:
		float a1=(height/3)/2;//середина 1/3 общей высоты View
		float a2=height-(height/3)  + (-a1);
		paint.getTextBounds(text, 0, text.length(), mTextBoundRect);
        float textHeight = mTextBoundRect.height();//высота текста
		y=((a2 + textHeight)-height/20);//начальная точка для записи текста по оси y, чтобы текст оказался посередине
		//paint.setStrokeWidth(2.0f);
		canvas.drawText(text,x,y,usedInFix?paintUsedInFix:paint);
		//paint.setStrokeWidth(3.0f);




		canvas.restore();

	}





	public void setAzimuth(float azimuth, boolean usedInFix){
		this.proggress=azimuth;
		f=-90+azimuth;
		this.usedInFix=usedInFix;
		invalidate();
	}


}
