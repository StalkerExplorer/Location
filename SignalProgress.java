package com.software1004developer.gps.view;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import com.software1004developer.*;
import com.software1004developer.gps.*;
import java.text.*;



public class SignalProgress extends View implements SharedConstants
{

	float width, height, eighth_width;
	float textWidth1;
	float textWidth2;
	float textWidth3;
	float textWidth4;
	float xText1,xText2,xText3, yText;
	int left,top,right,bottom;
	int leftBackground,topBackground,rightBackground,bottomBackground;
	Rect myRect;
	Rect myRectBackground;
	Paint paint, paintBackground, paintUsedInFix, paintNoUsedInFix, paintPoorGPS;
	String textSrOnph="";
	String textdBHz="";
	String dbHz="";
	String textResult="";
	float averageCn0DbHz=0;;//плотность отношения несущей к шуму на антенне спутника (среднее)
	float summCn0DbHz=0;;
	int count=0;
	int countNO_DATA=0;
	int countUsedFix=0;
	DecimalFormat decimalFormat;


	public SignalProgress(Context context) {
        super(context);
        init();
    }

    public SignalProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


	public void init()
	{
		myRect = new Rect();
		myRectBackground = new Rect();

		paintUsedInFix = new Paint();
		paintUsedInFix.setColor(Color.parseColor("#2d84ff"));
		paintUsedInFix.setStyle(Paint.Style.FILL);
		paintUsedInFix.setAntiAlias(true);
		paintUsedInFix.setSubpixelText(true);
		paintUsedInFix.setStrokeWidth(3);
		paintUsedInFix.setTextSize(15);

		paintNoUsedInFix = new Paint();
		paintNoUsedInFix.setColor(Color.parseColor("#696969"));
		paintNoUsedInFix.setStyle(Paint.Style.FILL);
		paintNoUsedInFix.setAntiAlias(true);
		paintNoUsedInFix.setSubpixelText(true);
		paintNoUsedInFix.setStrokeWidth(3);
		paintNoUsedInFix.setTextSize(15);

		paintPoorGPS = new Paint();
		paintPoorGPS.setColor(Color.parseColor("#FF0000"));
		paintPoorGPS.setStyle(Paint.Style.FILL);
		paintPoorGPS.setAntiAlias(true);
		paintPoorGPS.setSubpixelText(true);
		paintPoorGPS.setStrokeWidth(3);
		paintPoorGPS.setTextSize(15);

		paint = new Paint();
		paint.setColor(Color.parseColor("#448AFF"));
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);
		paint.setStrokeWidth(3);
		paint.setTextSize(25);

		paintBackground = new Paint();
		paintBackground.setColor(Color.parseColor("#363636"));
		paintBackground.setStyle(Paint.Style.FILL);
		paintBackground.setAntiAlias(true);
		paintBackground.setSubpixelText(true);
		paintBackground.setStrokeWidth(3);
		paintBackground.setTextSize(15);

		/*paintText = new Paint();
		 paintText.setColor(Color.parseColor("#448AFF"));
		 paintText.setStyle(Paint.Style.FILL);
		 paintText.setAntiAlias(true);
		 paintText.setSubpixelText(true);
		 paintText.setStrokeWidth(3);
		 paintText.setTextSize(25);*/


		//потребуется ширина текста последней записи (это запись "45")
		// Подсчитаем размер текста 
		String text1="45";
		// Используем measureText для измерения ширины
		paint.setTextSize(15);//перед подсчётом ширины нужно установить размер текста 15
		textWidth1 = paint.measureText(text1);
		paint.setTextSize(25);//после - вернуть назад на 25

		//потребуется ширина текста строки Ср. ОНПШ: 29.0 dB-Hz"
		String text2="Ср. ОНПШ: 29.0 dB-Hz";
		textWidth2 = paint.measureText(text2);

		String text3="Ср. ОНПШ: 29.0 ";
		textWidth3 = paint.measureText(text3);

		String text4="Ср. ОНПШ:";
		textWidth4 = paint.measureText(text4);

		decimalFormat= new DecimalFormat("#.#");

	}





	//если вкл GPS рисуем шкалу
	public void drawSignalProgress(Canvas canvas){
		width = canvas. getWidth();
		height = canvas. getHeight();
		eighth_width=width/8;


		setDbHz();//произведем сразу рассчет значения переменной right
		//при помощи которой и устанавливается величина прогресса

		//затем, после выполнения setDbHz() у нас появились сведения о countUsedFix и averageCn0DbHz
		//мы можем получить нужные кисти, вызывая метод getPaint()
		paint=getPaint(averageCn0DbHz,countUsedFix);
		paint.setTextSize(15);


		int size=(int)width/50;//толщина прямоугольника (возьмем 1/50 часть ширины view)

		left=0;
		top=(int)height/2 + (-size/2);
		//right=(int)width;
		bottom=(int)height/2 + (size/2);

		//задаём координаты верхней левой и нижней правой точек
		myRect.set(left,top,right,bottom);


		//также проделаем и с background-прямоугольником
		leftBackground=0;
		topBackground=(int)height/2 + (-size/2);
		rightBackground=(int)width;
		bottomBackground=(int)height/2 + (size/2);
		myRectBackground.set(leftBackground,topBackground,rightBackground,bottomBackground);

		//рисуем background прямоугольник
		canvas.drawRect(myRectBackground, paintBackground);

		//затем поверх,
		//рисуем основной прямоугольник (этот прямоугольник меняется как показатель прогресса, при помощи переменной right)
		canvas.drawRect(myRect, paint);


		//нарисуем линии
		//отступим вниз от нарисованного прямоугольника, на расстояние его толщины (size),
		//и нарисуем череду вертикальных линий, расположенных друг от друга на расстоянии 1/8 ширины view
		//линии чередуем - длинные/короткие
		//длинные вертикальные линии сделаем высотой равной трем толщинам прямоугольника
		//короткие линии сделаем высотой равной одной толщине прямоугольника

		int startY=bottom + (size);

		canvas.drawLine( (+3)+eighth_width*0 ,startY , (+3)+eighth_width*0 ,startY + size*3, paint);//+3 чтобы самая первая линия была правее на 3 px, а то её плохо видно
		canvas.drawLine( eighth_width*1 ,startY , eighth_width*1 ,startY + size*1, paint);
		canvas.drawLine( eighth_width*2 ,startY , eighth_width*2 ,startY + size*3, paint);
		canvas.drawLine( eighth_width*3 ,startY , eighth_width*3 ,startY + size*1, paint);
		canvas.drawLine( eighth_width*4 ,startY , eighth_width*4 ,startY + size*3, paint);
		canvas.drawLine( eighth_width*5 ,startY , eighth_width*5 ,startY + size*1, paint);
		canvas.drawLine( eighth_width*6 ,startY , eighth_width*6 ,startY + size*3, paint);
		canvas.drawLine( eighth_width*7 ,startY , eighth_width*7 ,startY + size*1, paint);
		canvas.drawLine( (-3)+eighth_width*8 ,startY , (-3)+eighth_width*8 ,startY + size*3, paint);//-3 чтобы самая последняя линия была левее на 3 px, а то её плохо видно


		//теперь подпишем линии
		//сделаем отступ у текста, величиной size/3 вправо от каждой линии, и size*1.7f вверх от конца длинных линий
		float paddingLeft=size/3;
		float paddingBottom=(startY + size*3) - size*1.7f;//отступаем вверх на величину size*1.7, от конца длинных линий

		canvas.drawText("5" , (+3)+paddingLeft+(0*eighth_width) ,paddingBottom,paint);//так как первая линия нарисована на 3px правее, нужно отступить с записью текста на столько-же правее
		canvas.drawText("10" ,paddingLeft+(1*eighth_width) ,paddingBottom,paint);
		canvas.drawText("15" ,paddingLeft+(2*eighth_width) ,paddingBottom,paint);
		canvas.drawText("20" ,paddingLeft+(3*eighth_width) ,paddingBottom,paint);
		canvas.drawText("25" ,paddingLeft+(4*eighth_width) ,paddingBottom,paint);
		canvas.drawText("30" ,paddingLeft+(5*eighth_width) ,paddingBottom,paint);
		canvas.drawText("35" ,paddingLeft+(6*eighth_width) ,paddingBottom,paint);
		canvas.drawText("40" ,paddingLeft+(7*eighth_width) ,paddingBottom,paint);
		canvas.drawText("45" ,(-3-3)+(-textWidth1)-paddingLeft+(8*eighth_width) ,paddingBottom,paint);//-ширина текста -3px левее (так как линия нарисована на 3px левее) и теперь, далее, можно отступить влево paddingLeft и ещё 3px




		//сделаем сверху прямоугольника, отступив вверх на расстоянии его толщины (size),
		//по центру надпись: Ср. ОНПШ: "числовые значения" dB-Hz

		textSrOnph="Ср. ОНПШ:";
		textdBHz="dB-Hz";


		//вычислим начальную точку для записи предпологаемой строки  Ср. ОНПШ: 29.0 dB-Hz (чтобы эта строка расположилась по центру view):
		xText1=(width-textWidth2)/2;

		//вычислим начальную точку для строки dB-Hz:
		xText3=xText1+textWidth3;

		yText=top+(-size);//отступаем вверх на расстоянии его толщины



		//так как числа меняются, и в результате меняется ширина текста их записи
		//а нам нужно чтобы записи чисел всегда распологались в центре отрезка
		//нам нужно вычислить начало для их записи на отрезке:
		//Xначальная = xText1+textWidth4
		//Xконечная = xText3
		//посчитаем ширину текста в записи чисел,
		//вычтем из длины отрезка Xначальная...Xконечная,
		//и поделим пополам.
		//Полученную величину прибавим к координате Xначальная
		//это и будет координата для записи текста меняющихся числовых значений

		float Xstart=xText1+textWidth4;//начальная
		float Xend=xText3;//конечная
		float lenght=Xend-Xstart;//длина отрезка
		dbHz=decimalFormat.format(averageCn0DbHz);//строка записи меняющихся числовых значений
		paint.setTextSize(25);//перед вычислением длины текста, вернем размер на 25 (он дальше понадобится 25, а вверху кода снова установится 15)
		float textLenght = paint.measureText(dbHz);//длина их текстовой записи
		float n=(lenght-textLenght)/2;//
		//вычислим начальную точку для строки самих числовых значений dB-Hz:
		float xText2=Xstart+n;//координата

		//впишем строку "Ср. ОНПШ: ":
		canvas.drawText(textSrOnph,xText1,yText,paint);

		//впишем строку самих числовых значений
		canvas.drawText(dbHz,xText2,yText,paint);

		//впишем строку "dB-Hz"
		canvas.drawText(textdBHz,xText3,yText,paint);


        //прошел очередной вызов onDraw(), весь код отработал,
        //обнуляем переменные:
		count=0;
		countNO_DATA=0;
		summCn0DbHz=0;
		countUsedFix=0;

	}



	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//canvas.drawColor(Color.parseColor("#212121"));

		if(MainActivity.provider.equals("")){
			//canvas.drawColor(Color.parseColor("#212121"));
		}else{
			drawSignalProgress(canvas);
		}

	}


	public int metod1(float progress){

		float a= width/40;

		return (int) (progress*a+(-5*a));
	}


	//проведем вычисления right=(int)averageCn0DbHz; в методе onDraw()
	public void setDbHz(){

		if(MainActivity. arraySat!=null){//1*

			for (MySatellite s : MainActivity. arraySat) {//2*
				count++;//считаем итерации (считаем кол-во спутников)

				//по ходу прохождения циклом,
				//выясним сколько всего спутников использовалось в расчете самой последней фиксации положения:
				if(s.getUsedInFix()){countUsedFix++;}


				//суммируем все плотностя отношения несущей к шуму на антенне спутников
				summCn0DbHz=summCn0DbHz+s.getCn0DbHz();
				//ещё нам для правильной математической операции нужно выяснить
				//количество спутников без тех, у которых getBasebandCn0DbHz=0
				//иначе мы неправильно найдём среднее арифметическое
				if(s.getCn0DbHz()==NO_DATA){
					countNO_DATA++;//найдем сумму тех, у которых getBasebandCn0DbHz=0
				}//а потом найдем разность count-countNO_DATA, и поделим. Получится правильное сред. арифм.


			}//2*

			//цикл закончился, теперь можно выяснить
			//среднее значение
			//плотностей отношения несущей к шуму на антенне спутников,
			if((count-countNO_DATA)!=0){//искл деление на ноль
				averageCn0DbHz=summCn0DbHz/(count-countNO_DATA);
			}

			//и наконец инициализируем переменную прямоугольника
			//right=(int)averageCn0DbHz;

			right=metod1(averageCn0DbHz);//пересчет для координаты прямоугольника



		}//1*

	}



	public Paint getPaint(float averageCn0DbHz, int countUsedFix){


		if((averageCn0DbHz>=20)&(countUsedFix!=0)){
			paint=paintUsedInFix;
		}

		if((averageCn0DbHz>=20)&(countUsedFix==0)){
			paint=paintNoUsedInFix;
		}

		if((averageCn0DbHz<20)&(countUsedFix==0)){
			paint=paintPoorGPS;
		}

		if((averageCn0DbHz<20)&(countUsedFix!=0)){
			paint=paintUsedInFix;
		}

		return paint;

	}



}
