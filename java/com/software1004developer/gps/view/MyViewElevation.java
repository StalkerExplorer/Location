package com.software1004developer.gps.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class MyViewElevation extends View
{

	float width, height;
	float f=0;
	float length;
	Paint paint, paintUsedInFix;
	String text;
	private Rect mTextBoundRect = new Rect();
	boolean usedInFix;

	public MyViewElevation(Context context) {
        super(context);
        init();
    }

    public MyViewElevation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewElevation(Context context, AttributeSet attrs, int defStyleAttr) {
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
		paint.setTextSize(25);

		paintUsedInFix = new Paint();
        paintUsedInFix.setColor(Color.parseColor("#448AFF"));
        paintUsedInFix.setStyle(Paint.Style.STROKE);
        paintUsedInFix.setAntiAlias(true);
		paintUsedInFix.setStrokeWidth(2.0f);
		paintUsedInFix.setTextSize(25);
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

		length=(width/5)*3;
		canvas.save();
		canvas.translate(width/5, (width/5)*3);


		canvas.drawLine(startX,startY,length,0,usedInFix?paintUsedInFix:paint);


		float stopX=(float)Math.cos(f *  Math.PI/180)*length;
		float stopY=(float)Math.sin(f  *  Math.PI/180)*length;
		canvas.drawLine(startX,startY,stopX,stopY,usedInFix?paintUsedInFix:paint);

		text=""+(Math.abs(f))+"°";
		float x=0;
		float y=0;
		//считаем середину по горизонтали:
		//paint.setTextSize(25);//перед вычислением длины текста
		float textLenght = paint.measureText(text);//длина их текста
		x=((width/2-(width/5))-(textLenght/2));//начальная точка для записи текста, чтобы текст оказался посередине (холст у нас уехал вправо на величину width/5 (мы делали translate), поэтому мы -width/5)
		//считаем середину для нижней 3/5 части по вертикали:
		float a1=((height/5)*3)/2;//середина 3/5 общей высоты View
		float a2=height-((width/5)*3)  + (-a1);
		paint.getTextBounds(text, 0, text.length(), mTextBoundRect);
        float textHeight = mTextBoundRect.height();//высота текста
		y=(a2 + textHeight);//начальная точка для записи текста по оси y, чтобы текст оказался посередине
		//paint.setStrokeWidth(2.0f);
		canvas.drawText(text,x,y,usedInFix?paintUsedInFix:paint);
		//paint.setStrokeWidth(3.0f);

		// рисуем разорванное кольцо
		final RectF oval = new RectF();
		float center_x, center_y;
		center_x = 0;
		center_y = 0;
		float radius = (length/5)*2;
		oval.set(center_x - radius,
				 center_y - radius,
				 center_x + radius,
				 center_y + radius);
		canvas.drawArc(oval, 0, f, false, usedInFix?paintUsedInFix:paint);


		canvas.restore();
	}





	public void setAngleUsed(float elevation, boolean usedInFix){
		f=-elevation;
		this.usedInFix=usedInFix;
		invalidate();
	}


	
	
	
}
