package com.software1004developer.gps.view;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.icu.text.*;
import android.util.*;
import android.view.*;
import com.software1004developer.*;
import com.software1004developer.gps.*;


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

		//?????? ???????????? ?????? ?????????????????? ?? ?????????? (?? onDraw()) ???????????????????????????? ???????????????? ?? ?????????? View ?? ????????????????
		//?????????? ???? ???????????? LocationData ???????????????? ???????????? ???????????????? ?? ???????????????????? ?? onDraw()
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

		// ???????????????????? ???????????? ????????????
		//paint.getTextBounds("2022-04-21", 0, text.length(), mTextBoundRect);
		//???????????? ???????????? ????????????:
		//textHeightTime = mTextBoundRect.height();
		//???????????? ???????????? ???????????? ?? ????????????????:
		textWidthTime1 = paint.measureText("2022-04-21");
		textWidthTime2 = paint.measureText("23.32.08");
		textWidthTime3 = paint.measureText("??????");
		textWidthTime4 = paint.measureText("????????????");
	}



	public void drawView(Canvas canvas){
		width=canvas.getWidth();
		height=canvas.getHeight();



		for(String str:attr){


			if(param.equals(str)){
				myDouble=LocationData.getParam(param);


				//???? ???????? ?? ???????????????????? View ?? ???????????????? ?????????????? ??????????
				//???? ?????????? ?????????? ???????????????????? ?????????? ?? ?????? ????????????:
				//2022-04-21
				//23.32.08
				if(param.equals("timeGPS")|param.equals("timeNETWORK")){

					if(myDouble!=null){//???????? ?? ?????????????? locationGPS ?? locationNetwork ?????????????????? ???????? null
						textTime1=df1.format((double)myDouble);
						textTime2=df2.format((double)myDouble);
					}else{textTime1="??????";textTime2="????????????";}


				}else{


					if(myDouble!=null){//???????? ?? ?????????????? locationGPS ?? locationNetwork ?????????????????? ???????? null
						text=decimalFormat.format( (double)myDouble );
					}else{text="?????? ????????????";}


				}





				break;}


		}

		//text=LocationData.getParam(param);

		//if(text!=null){//1*



		// ???????????????????? ???????????? ????????????
		paint.getTextBounds(text, 0, text.length(), mTextBoundRect);
		//???????????? ???????????? ????????????:
		textHeight = mTextBoundRect.height();
		//???????????? ???????????? ????????????:
		textWidth = paint.measureText(text);

		x=(width-textWidth)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view
		y=(height/2) + (textHeight/2);//?????? ???????????? ???? ??????????????????




		if(param.equals("timeGPS")|param.equals("timeNETWORK")){
			//???? ???????? ?? ???????????????????? View ?? ???????????????? ?????????????? ??????????
			//???? ?????????? ?????????? ???????????????????? ?????????? ?? ?????? ????????????:
			//2022-04-21
			//23.32.08
			//

		    //canvas.drawText(textTime1,x,y,paint);


			yTime1=(height/2) - (textHeight/2);//?????? ???????????? ???? ?????????????????? 1 ????????????
		    yTime2=(height/2) + (textHeight) ;//?????? ???????????? ???? ?????????????????? 2 ????????????




			//?????????? ???????????? ?????????????????? ?????????? ?????? ????????????
			//2022-04-21
			// 23.32.08
			//?? ???????????? 
			//??????
			//????????????
			if(myDouble!=null){//???????????? ?? ?????? ???????? ????????????
				xTime1=(width-textWidthTime1)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view
				xTime2=(width-textWidthTime2)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view
			}else{
				xTime1=(width-textWidthTime3)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view
				xTime2=(width-textWidthTime4)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view
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

		text="?????? ????????????";
		// ???????????????????? ???????????? ????????????
		paint.getTextBounds(text, 0, text.length(), mTextBoundRect);
		//???????????? ???????????? ????????????:
		textHeight = mTextBoundRect.height();
		//???????????? ???????????? ????????????:
		textWidth = paint.measureText(text);


		//???????? ?? ???????????????????? View ?? ???????????????? ?????????????? ??????????
		//???? ?????????? ?????????? ???????????????????? ?????????? ?? ?????? ????????????:
		//2022-04-21
		//23.32.08
		if(param.equals("timeGPS")|param.equals("timeNETWORK")){

			textTime1="??????";
			textTime2="????????????";

			yTime1=(height/2) - (textHeight/2);//?????? ???????????? ???? ?????????????????? 1 ????????????
		    yTime2=(height/2) + (textHeight) ;//?????? ???????????? ???? ?????????????????? 2 ????????????

			xTime1=(width-textWidthTime3)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view
			xTime2=(width-textWidthTime4)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view

			canvas.drawText(textTime1,xTime1,yTime1,paint);
			canvas.drawText(textTime2,xTime2,yTime2,paint);

		}else{

			text="?????? ????????????";
			x=(width-textWidth)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view
			y=(height/2) + (textHeight/2);//?????? ???????????? ???? ??????????????????
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


