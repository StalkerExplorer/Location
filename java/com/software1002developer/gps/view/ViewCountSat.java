package com.software1002developer.gps.view;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import com.software1002developer.*;
import com.software1002developer.gps.*;

public class ViewCountSat extends View
{

	Paint paint, paintUsedInFix, paintNoUsedInFix;
	float x,y;
	float textSize;
	int width,height;
	int count=0;
	int countUsedFix=0;
	String text,textCount;
	float textWidth,textHeight;
	Rect mTextBoundRect;


	public ViewCountSat(Context context) {
        super(context);
        init();
    }

    public ViewCountSat(Context context, AttributeSet attrs) {
        super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ViewCountSat);
		textSize = a.getDimension(R.styleable.ViewCountSat_textSize, 15);
		a.recycle();

        init();
    }

    public ViewCountSat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ViewCountSat);
		textSize = a.getDimension(R.styleable.ViewCountSat_textSize, 15);
		a.recycle();

        init();
    }


	public void init(){

		paintUsedInFix = new Paint();
		paintUsedInFix.setColor(Color.parseColor("#2d84ff"));
		paintUsedInFix.setShadowLayer(2,2,2,Color.parseColor("#101010"));
		paintUsedInFix.setStyle(Paint.Style.FILL);
		paintUsedInFix.setAntiAlias(true);
		paintUsedInFix.setSubpixelText(true);
		paintUsedInFix.setStrokeWidth(3);
		paintUsedInFix.setTextSize(textSize);

		paintNoUsedInFix = new Paint();
		paintNoUsedInFix.setColor(Color.parseColor("#FE5723"));
		paintNoUsedInFix.setShadowLayer(2,2,2,Color.parseColor("#101010"));
		paintNoUsedInFix.setStyle(Paint.Style.FILL);
		paintNoUsedInFix.setAntiAlias(true);
		paintNoUsedInFix.setSubpixelText(true);
		paintNoUsedInFix.setStrokeWidth(3);
		paintNoUsedInFix.setTextSize(textSize);


		mTextBoundRect = new Rect();
	}



	public void drawViewCountSat(Canvas canvas){
		width=canvas.getWidth();
		height=canvas.getHeight();

		//выполним метод metod1()
		//после нам станут доступны count и countUsed
		metod1();
        paint=getPaint(countUsedFix);
	    textCount=countUsedFix+"/"+count;
		text="Спутники "+textCount;


		// Подсчитаем размер текста
        paint.getTextBounds(text, 0, text.length(), mTextBoundRect);
		//высота текста строки:
        textHeight = mTextBoundRect.height();
		//ширина текста строки:
		textWidth = paint.measureText(text);

		x=(width-textWidth)/2;//начальная точка для текста, чтобы текст расположился по центру view
		y=(height/2) + (textHeight/2);//для центра по вертикали


		canvas.drawText(text,x,y,paint);

		//прошел очередной вызов onDraw(), весь код отработал,
        //обнуляем переменные:
		count=0;
		countUsedFix=0;
	}



	public void metod1(){
		if(MainActivity. arraySat!=null){//1*

			for (MySatellite s : MainActivity. arraySat) {//2*
				count++;//считаем итерации (считаем кол-во спутников)

				//по ходу прохождения циклом,
				//выясним сколько всего спутников использовалось в расчете самой последней фиксации положения:
				if(s.getUsedInFix()){countUsedFix++;}
			}//2*


		}//1*
	}


	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//canvas.drawColor(Color.parseColor("#212121"));

		if(MainActivity.provider.equals("")){
			//canvas.drawColor(Color.parseColor("#212121"));
		}else{
			drawViewCountSat(canvas);
		}

	}


	public Paint getPaint(int countUsedFix){

		if(countUsedFix==0){
			return paintNoUsedInFix;
		}else{
			return paintUsedInFix;
		}
	}



}
