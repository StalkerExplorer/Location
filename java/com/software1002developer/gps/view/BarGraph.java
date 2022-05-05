package com.software1002developer.gps.view;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import com.software1002developer.*;
import com.software1002developer.gps.*;


public class BarGraph extends View
{

	Paint paint;
	Paint paintNoFix;
	Paint paintFix;
	Paint paintText;
	float xDown,yDown;
	float xLength,yLength;
	float xMemoryLength,yMemoryLength,textWidthDbHz; //textWidthBasebandCn0DbHz,textWidthCn0DbHz;
	float stopLefX,stopRightX;
	float width,height;
	float dbHz;
	int left,top,right,bottom;
	int startX,stopX;
	float xText,yText;
	float xStr,yStr;
	Rect myRect;
	boolean enable;
	boolean basebandCn0DbHz;
	Rect mTextBoundRect;
	float textWidth,textHeight;
	int i=0;
	String longText;
	String textDBHz;
	float lenghtLongText;

	public BarGraph(Context context) {
        super(context);
        init();

    }

    public BarGraph(Context context, AttributeSet attrs) {
        super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.BarGraph);
		basebandCn0DbHz = a.getBoolean(R.styleable.BarGraph_BasebandCn0DbHz, false);
		a.recycle();

        init();
    }

    public BarGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.BarGraph);
		basebandCn0DbHz = a.getBoolean(R.styleable.BarGraph_BasebandCn0DbHz, false);
		a.recycle();

        init();
    }



	public void init(){
		paint = new Paint();
		paint.setColor(Color.parseColor("#BDBDBD"));
		//paint.setStyle(Paint.Style.FILL);
		paint.setStyle(Paint.Style.STROKE);
		//paint.setShadowLayer(2,2,2,Color.parseColor("#BDBDBD"));
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);

		paintNoFix = new Paint();
		paintNoFix.setColor(Color.parseColor("#696969"));
		paintNoFix.setStyle(Paint.Style.FILL_AND_STROKE);
        //paintNoFix.setShadowLayer(2,2,-2,Color.parseColor("#BDBDBD"));
		paintNoFix.setAntiAlias(true);
		paintNoFix.setSubpixelText(true);
		paintNoFix.setTextSize(15);


		paintFix = new Paint();
		paintFix.setColor(Color.parseColor("#448AFF"));
		paintFix.setStyle(Paint.Style.FILL_AND_STROKE);
		//paintFix.setShadowLayer(2,2,-2,Color.parseColor("#BDBDBD"));
		paintFix.setAntiAlias(true);
		paintFix.setSubpixelText(true);
		paintFix.setTextSize(15);


		paintText = new Paint();
		paintText.setColor(Color.parseColor("#696969"));
		paintText.setStyle(Paint.Style.FILL_AND_STROKE);
        paintText.setShadowLayer(1,1,-1,Color.parseColor("#000000"));
		paintText.setAntiAlias(true);
		paintText.setSubpixelText(true);
		paintText.setTextSize(17);


		myRect = new Rect();
		mTextBoundRect = new Rect();


		String s1="Плотность отношения несущей к шуму основной полосы частот спутников ДБ-Гц";
		String s2="Плотность отношения несущей к шуму на антенне спутников ДБ-Гц";
		//ширина текста строки:
		float textWidthBasebandCn0DbHz = paintText.measureText(s1);
		float textWidthCn0DbHz = paintText.measureText(s2);
		//в зависимости от установленного атрибута инициализируем переменную ширины строки:
		textDBHz=basebandCn0DbHz?s1:s2;//строку с текстом
		//и переменную длины этой строки
		textWidthDbHz=basebandCn0DbHz?textWidthBasebandCn0DbHz:textWidthCn0DbHz;

	}



	//если включен GPS рисуем гистограмму
	public void drawBarGraph(Canvas canvas){
		width=canvas.getWidth();
		height=canvas.getHeight();


		if(MainActivity.arraySat!=null){//if(MainActivity.arraySat!=null){



			int count=MainActivity.arraySat.size();
			int size=(int) width/50;//ширину одного элемента диаграммы
			//возьмем как 1/45 часть ширины view
			//такая ширина элемента впринципе выглядит неплохо.
			int padding=5;//отступ (прямоугольники)
			int lenght=size*(count+1);//длина всех элементов диаграммы вместе
			int x=(int)(width-lenght)/2;////начальная точка у View (чтобы от неё начинать рисовать диаграмму) (чтобы диаграмма получилась в середине)


			//установим здесь stopLefX, stopRightX для нашей диаграммы :
			if(lenght>width){//если длина всех элементов диаграммы вместе больше ширины экрана
				enable=true;
				startX=x;//начальная
				stopX=startX+lenght;//конечная
				stopLefX=startX; //0+50;
				stopRightX=stopX-width;   //width-50;
			}else{//иначе они все вместе меньше ширины экрана
				enable=false;
				startX= x + (lenght-(int)width)/2;//начальная
				stopX=startX+lenght;//конечная
				stopLefX=startX; //0+50;
				stopRightX=stopX;//-size;   //width-50;
			}






			if((xLength>=stopLefX)&(xLength<=stopRightX)){
				//всё хорошо, мы находимся в нужных границах, и рисуем:

				for (MySatellite s : MainActivity. arraySat) {//цикл11

					//в зависимости от установленного в атрибутах инициализируем 
					//либо getBasebandCn0DbHz() либо getCn0DbHz()
					dbHz=basebandCn0DbHz?s.getBasebandCn0DbHz():s.getCn0DbHz();


					left=(int) xLength+(x+ size*i +padding);
					top=((int)(height/4))  -  ((int)dbHz) ;
					right=(int) xLength+(x+ size*(i+1));
					bottom=(int)height/4; //было height/4; 

					//задаём координаты верхней левой и нижней правой точек
					myRect.set(left,top,right,bottom);
					//рисуем прямоугольник
					canvas.drawRect(myRect, s.getUsedInFix()?paintFix:paintNoFix);


					//вертикальные надписи:
					//String text="Проверка"+i;
					String text=s.getConstellationType()+""+s.getSvid()+":  "+dbHz;
					// Подсчитаем размер текста
					paintFix.getTextBounds(text, 0, text.length(), mTextBoundRect);
					//textWidth = textBounds.width();
					// Используем measureText для измерения ширины
					textWidth = paintFix.measureText(text);
					//textHeight = mTextBoundRect.height();
					// Подсчитаем размер текста

					//и теперь, после того как посчитали размер текста
					//сделаем так чтобы все надписи упирались в вверхнюю четверть нашего view, но не вплотную, а с отступом +(size/2)
					xText=0+height-(textWidth+height/4) +(-size/2);//(вверх/вниз)начальная точка, откуда начинаются надписи
					yText= left -width +(size-padding);//(влево/вправо)начальная точка, откуда начинаются надписи
					canvas.save();
					canvas.scale(-1,-1);
					canvas.translate(-width,-height);
					canvas.rotate(90);
					canvas.drawText(text, xText,yText, s.getUsedInFix()?paintFix:paintNoFix);
					canvas.restore();
					//вертикальные надписи 

					i++;

				}//цикл11
				i=0;



				//>>>>
				//после выхода из цикла
				//посчитаем длину текста для предпологаемой наиболее длинной надписи
				//т.е. вертикальная надпись не может быть длиннее чем
				//например надпись: "Glonass999:  27.9"
				longText="GLONASS999:  27.9";
				//длина этой надписи:
				lenghtLongText = paintFix.measureText(longText);
				//отступим вниз от самой длинной надписи на величину size
				//и сделаем надпись "Плотность отношения несущей к шуму на антенне спутников"
				yStr=height/4+lenghtLongText+size/2 + size; //height -(lenghtLongText+height/4)+((-size/2) + (-size/2));
				xStr=(width- textWidthDbHz)/2;//начальная точка для текста, чтобы текст расположился по центру view
				canvas.drawText(textDBHz,xStr,yStr,paintText);

				//>>>>



			}else{//иначе, мы ушли из границ


				if(xLength<=stopLefX){//если ушли левее допустимого
					//нужно принудить canvas рисовать только в месте: (в месте stopLefX, а не в месте e<=stopLefX (что происходило, если бы не было этого блока кода), иначе видно что мы рисуем в не в том месте)
					//(xLength приехало в stopLefX и дальше. Нам дальше рисовать не надо, поэтому рисуем в точке stopLefX)



					for (MySatellite s : MainActivity. arraySat) {

						//в зависимости от установленного в атрибутах инициализируем 
						//либо getBasebandCn0DbHz() либо getCn0DbHz()
						dbHz=basebandCn0DbHz?s.getBasebandCn0DbHz():s.getCn0DbHz();


						left=(int) stopLefX+(x+ size*i +padding);
						top=((int)(height/4))  -  ((int)dbHz) ;
						right=(int) stopLefX+(x+ size*(i+1));
						bottom=(int)height/4; //было height/4; 

						//задаём координаты верхней левой и нижней правой точек
						myRect.set(left,top,right,bottom);
						//рисуем прямоугольник
						canvas.drawRect(myRect, s.getUsedInFix()?paintFix:paintNoFix);

						//вертикальные надписи:
						//String text="Проверка"+i;
						String text=s.getConstellationType()+""+s.getSvid()+":  "+dbHz;
						// Подсчитаем размер текста
						paintFix.getTextBounds(text, 0, text.length(), mTextBoundRect);
						//textWidth = textBounds.width();
						// Используем measureText для измерения ширины
						textWidth = paintFix.measureText(text);
						//textHeight = mTextBoundRect.height();
						// Подсчитаем размер текста

						//и теперь, после того как посчитали размер текста
						//сделаем так чтобы все надписи упирались в вверхнюю четверть нашего view, но не вплотную, а с отступом +(size/2)
						xText=0+height-(textWidth+height/4) +(-size/2);//(вверх/вниз)начальная точка, откуда начинаются надписи
						yText= left -width +(size-padding);//(влево/вправо)начальная точка, откуда начинаются надписи
						canvas.save();
						canvas.scale(-1,-1);
						canvas.translate(-width,-height);
						canvas.rotate(90);
						canvas.drawText(text, xText,yText, s.getUsedInFix()?paintFix:paintNoFix);
						canvas.restore();
						//вертикальные надписи 

						i++;
					}

					i=0;
				}


				if(xLength>=stopRightX){//если ушли правее допустимого
					//нужно принудить canvas рисовать в месте: (в месте stopRightX, а не в месте e>=stopRightX (что происходило, если бы не было этого блока кода), иначе видно что мы рисуем в не в том месте)
					//(xLength приехало в stopRightX и дальше. Нам дальше рисовать не надо, поэтому рисуем в точке stopRightX)

					for (MySatellite s : MainActivity. arraySat) {

						//в зависимости от установленного в атрибутах инициализируем 
						//либо getBasebandCn0DbHz() либо getCn0DbHz()
						dbHz=basebandCn0DbHz?s.getBasebandCn0DbHz():s.getCn0DbHz();


						left=(int) stopRightX+(x+ size*i +padding);
						top=((int)(height/4))  -  ((int)dbHz) ;
						right=(int) stopRightX+(x+ size*(i+1));
						bottom=(int)height/4; //было height/4; 

						//задаём координаты верхней левой и нижней правой точек
						myRect.set(left,top,right,bottom);
						//рисуем прямоугольник
						canvas.drawRect(myRect, s.getUsedInFix()?paintFix:paintNoFix);

						//вертикальные надписи:
						//String text="Проверка"+i;
						String text=s.getConstellationType()+""+s.getSvid()+":  "+dbHz;
						// Подсчитаем размер текста
						paintFix.getTextBounds(text, 0, text.length(), mTextBoundRect);
						//textWidth = textBounds.width();
						// Используем measureText для измерения ширины
						textWidth = paintFix.measureText(text);
						//textHeight = mTextBoundRect.height();
						// Подсчитаем размер текста

						//и теперь, после того как посчитали размер текста
						//сделаем так чтобы все надписи упирались в вверхнюю четверть нашего view, но не вплотную, а с отступом +(size/2)
						xText=0+height-(textWidth+height/4) +(-size/2);//(вверх/вниз)начальная точка, откуда начинаются надписи
						yText= left -width +(size-padding);//(влево/вправо)начальная точка, откуда начинаются надписи
						canvas.save();
						canvas.scale(-1,-1);
						canvas.translate(-width,-height);
						canvas.rotate(90);
						canvas.drawText(text, xText,yText, s.getUsedInFix()?paintFix:paintNoFix);
						canvas.restore();
						//вертикальные надписи 

						i++;
					}

					i=0;

				}


			}//иначе, мы ушли из границ




		}//if(MainActivity.arraySat!=null){

	}




	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//canvas.drawColor(Color.parseColor("#212121"));

		if(MainActivity.provider.equals("")){
			//canvas.drawColor(Color.parseColor("#212121"));
		}else{
			drawBarGraph(canvas);
		}

	}






	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

        //всё это начинает работать только если
		if(enable){//длина всей гистограммы больше ширины view


			switch (event.getAction()){

				case MotionEvent.ACTION_DOWN:
					xDown=event.getX();
					yDown=event.getY();
					break;

				case MotionEvent.ACTION_MOVE:



					if((xLength>=stopLefX)&(xLength<=stopRightX)){//если мы находимся в нужных границах

						//считаем на какую величину в результате у нас должен сдвинуться объект
						xLength=xMemoryLength+ (event.getX()-xDown);
						yLength=yMemoryLength+ (event.getY()-yDown);

					}else{//иначе мы ушли за границы

						//если не проделать эту инициализацию мы застрянем
						if(xLength<=stopLefX){xLength=stopLefX;}//если мы ушли левее границы
						//если не проделать эту инициализацию мы застрянем
						if(xLength>=stopRightX){xLength=stopRightX;}//если ушли правее

					}//иначе мы ушли за границы




					break;

				case MotionEvent.ACTION_UP:
					xMemoryLength=xLength;
					yMemoryLength=yLength;
					break;
			}


			invalidate();

		}//всё это начинает работать только если
		//if(enable){//длина всей диаграммы больше ширины view


		return true;
	}



}
