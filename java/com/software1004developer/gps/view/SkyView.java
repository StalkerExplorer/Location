package com.software1004developer.gps.view;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import com.software1004developer.*;
import com.software1004developer.gps.*;
import java.util.*;

public class SkyView extends View implements SharedConstants
{
	
	public Map  myBooleanArray;

	private static int mHeight;
    private static int mWidth;
	private double mOrientation = 0.0;
	private static int SAT_RADIUS;
	//private Paint horizontStrokePaint, horizontFillPaint,mGridStrokePaint,mHorizonStrokePaint;

	private Paint horizontStrokePaint, horizontFillPaint, horizontPaint,mGridStrokePaint,mHorizonStrokePaint,
	mNorthPaint, mNorthFillPaint,fillPaint,strokePaint,mPrnIdPaint,
	usedInFixPaint,usedInFixTextPaint;


	

	public SkyView(Context context) {
        super(context);
        init(context);
    }

    public SkyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SkyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


	public void init(Context context)
	{
		
		myBooleanArray=new HashMap();
		boolean b=true;
		myBooleanArray.put("BEIDOU",b);
		myBooleanArray.put("GALILEO",b);
		myBooleanArray.put("GLONASS",b);
		myBooleanArray.put("GPS",b);
		myBooleanArray.put("IRNSS",b);
		myBooleanArray.put("QZSS",b);
		myBooleanArray.put("SBAS",b);
		myBooleanArray.put("UNKNOWN",b);
		
		
		
		SAT_RADIUS = dpToPixels(context, 5);

		horizontStrokePaint = new Paint();
        horizontStrokePaint.setColor(Color.parseColor("#696969"));
        horizontStrokePaint.setStyle(Paint.Style.STROKE);
        horizontStrokePaint.setAntiAlias(true);

		horizontFillPaint = new Paint();
        horizontFillPaint.setColor(Color.parseColor("#696969"));
        horizontFillPaint.setStyle(Paint.Style.FILL);
        horizontFillPaint.setAntiAlias(true);

		horizontPaint = new Paint();
        horizontPaint.setColor(Color.parseColor("#252525"));
        horizontPaint.setStyle(Paint.Style.FILL);
        horizontPaint.setAntiAlias(true);

		mGridStrokePaint = new Paint();
        mGridStrokePaint.setColor(Color.parseColor("#757575"));
        mGridStrokePaint.setStyle(Paint.Style.STROKE);
        mGridStrokePaint.setAntiAlias(true);

		mHorizonStrokePaint = new Paint();
        mHorizonStrokePaint.setColor(Color.BLUE);
        mHorizonStrokePaint.setStyle(Paint.Style.STROKE);
        mHorizonStrokePaint.setStrokeWidth(2.0f);
        mHorizonStrokePaint.setAntiAlias(true);

		mNorthPaint = new Paint();
        mNorthPaint.setColor(Color.BLACK);
        mNorthPaint.setStyle(Paint.Style.STROKE);
        mNorthPaint.setStrokeWidth(4.0f);
        mNorthPaint.setAntiAlias(true);

        mNorthFillPaint = new Paint();
        mNorthFillPaint.setColor(Color.GRAY);
        mNorthFillPaint.setStyle(Paint.Style.FILL);
        mNorthFillPaint.setStrokeWidth(4.0f);
        mNorthFillPaint.setAntiAlias(true);

		fillPaint = new Paint();
        fillPaint.setColor(Color.parseColor("#424242"));
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);

		strokePaint = new Paint();
        strokePaint.setColor(Color.parseColor("#000000"));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);

		mPrnIdPaint = new Paint();
        mPrnIdPaint.setColor(Color.parseColor("#303F9F"));
        mPrnIdPaint.setStyle(Paint.Style.STROKE);
        mPrnIdPaint.setAntiAlias(true);


		usedInFixPaint = new Paint();
        usedInFixPaint.setColor(Color.parseColor("#FE5723"));//2d84ff
		usedInFixPaint.setShadowLayer(2,2,2,Color.parseColor("#101010"));
        usedInFixPaint.setStyle(Paint.Style.FILL);
        usedInFixPaint.setAntiAlias(true);

		usedInFixTextPaint = new Paint();
        usedInFixTextPaint.setColor(Color.parseColor("#2d84ff"));//
        usedInFixTextPaint.setStyle(Paint.Style.FILL);
        usedInFixTextPaint.setAntiAlias(true);

	}


	public int dpToPixels(Context context, float dp){
        // Get the screen's density scale
        int scale = (int)(context.getResources().getDisplayMetrics().density);
        // Convert the dps to pixels, based on density scale
        return Math.round( (dp * scale + 0.5f));
    }



	private float elevationToRadius(int s, float elev) {
        return ((s / 2) - SAT_RADIUS) * (1.0f - (elev / 90.0f));
    }


	private void drawLine(Canvas c, float x1, float y1, float x2, float y2) {
        // rotate the line based on orientation
        double angle = Math.toRadians(-mOrientation);
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        float centerX = (x1 + x2) / 2.0f;
        float centerY = (y1 + y2) / 2.0f;
        x1 -= centerX;
        y1 = centerY - y1;
        x2 -= centerX;
        y2 = centerY - y2;

        float X1 = cos * x1 + sin * y1 + centerX;
        float Y1 = -(-sin * x1 + cos * y1) + centerY;
        float X2 = cos * x2 + sin * y2 + centerX;
        float Y2 = -(-sin * x2 + cos * y2) + centerY;

        c.drawLine(X1, Y1, X2, Y2, mGridStrokePaint);
    }



	private void drawHorizon(Canvas c, int s) {
        float radius = s / 2;

		//рисует окружность в заданной точке с координатами radius, radius (наш небосвод (большая окружность) )
        c.drawCircle(radius, radius, radius, horizontStrokePaint);

		//перекрестие на небосводе
        drawLine(c, 0, radius, 2 * radius, radius);
        drawLine(c, radius, 0, radius, 2 * radius);

		//круги (высота по радиусу) (видимо это надо понимать так, что я как-бы смотрю наверх, на купол неба)
		//тогда это получается "углы возвышения":
		//c.drawCircle(radius, radius, elevationToRadius(s, 90.0f), mGridStrokePaint);//тогда надо полагать, эта точка прямо над головой
		c.drawCircle(radius, radius, elevationToRadius(s, 60.0f), mGridStrokePaint);
		c.drawCircle(radius, radius, elevationToRadius(s, 30.0f), mGridStrokePaint);
		c.drawCircle(radius, radius, elevationToRadius(s, 0.0f), mGridStrokePaint);
		c.drawCircle(radius, radius, radius, mHorizonStrokePaint);


    }


	private void drawNorthIndicator(Canvas c, int s) {
        float radius = s / 2;
        double angle = Math.toRadians(-mOrientation);
        final float ARROW_HEIGHT_SCALE = 0.05f;
        final float ARROW_WIDTH_SCALE = 0.1f;

        float x1, y1;  // Tip of arrow
        x1 = radius;
        y1 = elevationToRadius(s, 90.0f);

        float x2, y2;
        x2 = x1 + radius * ARROW_HEIGHT_SCALE;
        y2 = y1 + radius * ARROW_WIDTH_SCALE;

        float x3, y3;
        x3 = x1 - radius * ARROW_HEIGHT_SCALE;
        y3 = y1 + radius * ARROW_WIDTH_SCALE;

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x1, y1);
        path.close();

        // Rotate arrow around center point
        Matrix matrix = new Matrix();
        matrix.postRotate((float) -mOrientation, radius, radius);
        path.transform(matrix);

        c.drawPath(path, mNorthPaint);
        c.drawPath(path, mNorthFillPaint);
    }



	private void drawCirkle(Canvas c, float x, float y, Paint fillPaint, Paint strokePaint) {

		c.drawCircle(x, y, SAT_RADIUS, fillPaint);
		c.drawCircle(x, y, SAT_RADIUS, strokePaint);

    }


	private void drawSatellite(Canvas c, int minScreenDimen, float elev, float azim, float cn0, int prn,
							   String gnssType, boolean usedInFix) {
        double radius, angle;
        float x, y;
        // Place PRN text slightly below drawn satellite
        final double PRN_X_SCALE = 1.8;
        final double PRN_Y_SCALE = 1.5;




        radius = elevationToRadius(minScreenDimen, elev);
        azim -= mOrientation;
        angle = (float) Math.toRadians(azim);

        x = (float) ((minScreenDimen / 2) + (radius * Math.sin(angle)));
        y = (float) ((minScreenDimen / 2) - (radius * Math.cos(angle)));


		//рисуем спутник:
		drawCirkle(c,x,y,  usedInFix?usedInFixPaint:fillPaint,  strokePaint);

		//подписываем спутник:
        c.drawText(String.valueOf(prn), x - (int) (SAT_RADIUS * PRN_X_SCALE),
				   y + (int) (SAT_RADIUS * PRN_Y_SCALE), usedInFix?usedInFixPaint:mPrnIdPaint );
    }


	
	


	@Override
	protected void onDraw(Canvas canvas)
	{
		
/////
		
		
		mHeight = getHeight();
		mWidth = getWidth();
		int minScreenDimen;
        minScreenDimen = Math.min(mWidth, mHeight);//минимальное число из двух


		drawHorizon(canvas, minScreenDimen);
		drawNorthIndicator(canvas, minScreenDimen);

		if(MainActivity. arraySat!=null){

			for (MySatellite s : MainActivity. arraySat) {
				if (s.getElevationDegrees() != NO_DATA && s.getAzimuthDegrees() != NO_DATA) {

					
					
					//перебираем все значения
					//for (boolean value : myBooleanArray.values()) {
						//System.out.println("Value: " + value);
					//}

					
					
					
					if((boolean)(myBooleanArray.get(s.getConstellationType()))){//1*


					//constellation[i]==s.getConstellationType();


						drawSatellite(canvas, minScreenDimen,
									  s.getElevationDegrees(),
									  s.getAzimuthDegrees(),
									  s.getCn0DbHz(),
									  s.getSvid(),
									  s.getConstellationType(),
									  s.getUsedInFix());





					}//1*



				}
			}

		}
////
	}



	
	/*
//возвращает boolean - рисовать drawSatellite() или нет (смотреть onDraw(), там в блоке if)
//сами переменные инициализируются в обработчике нажатий skyView - onclSky(), в MainActivity 
	public boolean constellationEnabled(String constellationType){

		boolean b=false;

		switch (constellationType){

			case "BEIDOU":
				b= constellation[BEIDOU];
				break;

			case "GALILEO":
				b=constellation[GALILEO];
				break;

			case "GLONASS":
				b=constellation[GLONASS];
				break;

			case  "GPS":
				b=constellation[GPS];
				break;

			case "IRNSS":
				b=constellation[IRNSS];
				break;

			case "QZSS":
				b=constellation[QZSS];
				break;

			case "SBAS":
				b=constellation[SBAS];
				break;

			case "CONSTELLATION_UNKNOWN":
				b=constellation[UNKNOWN];
				break;


		}

		return b;
	}
    */
	
	
	
}
