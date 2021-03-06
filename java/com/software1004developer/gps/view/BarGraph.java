package com.software1004developer.gps.view;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import com.software1004developer.*;
import com.software1004developer.gps.*;


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


		String s1="?????????????????? ?????????????????? ?????????????? ?? ???????? ???????????????? ???????????? ???????????? ?????????????????? ????-????";
		String s2="?????????????????? ?????????????????? ?????????????? ?? ???????? ???? ?????????????? ?????????????????? ????-????";
		//???????????? ???????????? ????????????:
		float textWidthBasebandCn0DbHz = paintText.measureText(s1);
		float textWidthCn0DbHz = paintText.measureText(s2);
		//?? ?????????????????????? ???? ???????????????????????????? ???????????????? ???????????????????????????? ???????????????????? ???????????? ????????????:
		textDBHz=basebandCn0DbHz?s1:s2;//???????????? ?? ??????????????
		//?? ???????????????????? ?????????? ???????? ????????????
		textWidthDbHz=basebandCn0DbHz?textWidthBasebandCn0DbHz:textWidthCn0DbHz;

	}



	//???????? ?????????????? GPS ???????????? ??????????????????????
	public void drawBarGraph(Canvas canvas){
		width=canvas.getWidth();
		height=canvas.getHeight();


		if(MainActivity.arraySat!=null){//if(MainActivity.arraySat!=null){



			int count=MainActivity.arraySat.size();
			int size=(int) width/50;//???????????? ???????????? ???????????????? ??????????????????
			//?????????????? ?????? 1/45 ?????????? ???????????? view
			//?????????? ???????????? ???????????????? ?????????????????? ???????????????? ??????????????.
			int padding=5;//???????????? (????????????????????????????)
			int lenght=size*(count+1);//?????????? ???????? ?????????????????? ?????????????????? ????????????
			int x=(int)(width-lenght)/2;////?????????????????? ?????????? ?? View (?????????? ???? ?????? ???????????????? ???????????????? ??????????????????) (?????????? ?????????????????? ???????????????????? ?? ????????????????)


			//?????????????????? ?????????? stopLefX, stopRightX ?????? ?????????? ?????????????????? :
			if(lenght>width){//???????? ?????????? ???????? ?????????????????? ?????????????????? ???????????? ???????????? ???????????? ????????????
				enable=true;
				startX=x;//??????????????????
				stopX=startX+lenght;//????????????????
				stopLefX=startX; //0+50;
				stopRightX=stopX-width;   //width-50;
			}else{//?????????? ?????? ?????? ???????????? ???????????? ???????????? ????????????
				enable=false;
				startX= x + (lenght-(int)width)/2;//??????????????????
				stopX=startX+lenght;//????????????????
				stopLefX=startX; //0+50;
				stopRightX=stopX;//-size;   //width-50;
			}






			if((xLength>=stopLefX)&(xLength<=stopRightX)){
				//?????? ????????????, ???? ?????????????????? ?? ???????????? ????????????????, ?? ????????????:

				for (MySatellite s : MainActivity. arraySat) {//????????11

					//?? ?????????????????????? ???? ???????????????????????????? ?? ?????????????????? ???????????????????????????? 
					//???????? getBasebandCn0DbHz() ???????? getCn0DbHz()
					dbHz=basebandCn0DbHz?s.getBasebandCn0DbHz():s.getCn0DbHz();


					left=(int) xLength+(x+ size*i +padding);
					top=((int)(height/4))  -  ((int)dbHz) ;
					right=(int) xLength+(x+ size*(i+1));
					bottom=(int)height/4; //???????? height/4; 

					//???????????? ???????????????????? ?????????????? ?????????? ?? ???????????? ???????????? ??????????
					myRect.set(left,top,right,bottom);
					//???????????? ??????????????????????????
					canvas.drawRect(myRect, s.getUsedInFix()?paintFix:paintNoFix);


					//???????????????????????? ??????????????:
					//String text="????????????????"+i;
					String text=s.getConstellationType()+""+s.getSvid()+":  "+dbHz;
					// ???????????????????? ???????????? ????????????
					paintFix.getTextBounds(text, 0, text.length(), mTextBoundRect);
					//textWidth = textBounds.width();
					// ???????????????????? measureText ?????? ?????????????????? ????????????
					textWidth = paintFix.measureText(text);
					//textHeight = mTextBoundRect.height();
					// ???????????????????? ???????????? ????????????

					//?? ????????????, ?????????? ???????? ?????? ?????????????????? ???????????? ????????????
					//?????????????? ?????? ?????????? ?????? ?????????????? ?????????????????? ?? ???????????????? ???????????????? ???????????? view, ???? ???? ????????????????, ?? ?? ???????????????? +(size/2)
					xText=0+height-(textWidth+height/4) +(-size/2);//(??????????/????????)?????????????????? ??????????, ???????????? ???????????????????? ??????????????
					yText= left -width +(size-padding);//(??????????/????????????)?????????????????? ??????????, ???????????? ???????????????????? ??????????????
					canvas.save();
					canvas.scale(-1,-1);
					canvas.translate(-width,-height);
					canvas.rotate(90);
					canvas.drawText(text, xText,yText, s.getUsedInFix()?paintFix:paintNoFix);
					canvas.restore();
					//???????????????????????? ?????????????? 

					i++;

				}//????????11
				i=0;



				//>>>>
				//?????????? ???????????? ???? ??????????
				//?????????????????? ?????????? ???????????? ?????? ???????????????????????????? ???????????????? ?????????????? ??????????????
				//??.??. ???????????????????????? ?????????????? ???? ?????????? ???????? ?????????????? ??????
				//???????????????? ??????????????: "Glonass999:  27.9"
				longText="GLONASS999:  27.9";
				//?????????? ???????? ??????????????:
				lenghtLongText = paintFix.measureText(longText);
				//???????????????? ???????? ???? ?????????? ?????????????? ?????????????? ???? ???????????????? size
				//?? ?????????????? ?????????????? "?????????????????? ?????????????????? ?????????????? ?? ???????? ???? ?????????????? ??????????????????"
				yStr=height/4+lenghtLongText+size/2 + size; //height -(lenghtLongText+height/4)+((-size/2) + (-size/2));
				xStr=(width- textWidthDbHz)/2;//?????????????????? ?????????? ?????? ????????????, ?????????? ?????????? ???????????????????????? ???? ???????????? view
				canvas.drawText(textDBHz,xStr,yStr,paintText);

				//>>>>



			}else{//??????????, ???? ???????? ???? ????????????


				if(xLength<=stopLefX){//???????? ???????? ?????????? ??????????????????????
					//?????????? ?????????????????? canvas ???????????????? ???????????? ?? ??????????: (?? ?????????? stopLefX, ?? ???? ?? ?????????? e<=stopLefX (?????? ??????????????????????, ???????? ???? ???? ???????? ?????????? ?????????? ????????), ?????????? ?????????? ?????? ???? ???????????? ?? ???? ?? ?????? ??????????)
					//(xLength ???????????????? ?? stopLefX ?? ????????????. ?????? ???????????? ???????????????? ???? ????????, ?????????????? ???????????? ?? ?????????? stopLefX)



					for (MySatellite s : MainActivity. arraySat) {

						//?? ?????????????????????? ???? ???????????????????????????? ?? ?????????????????? ???????????????????????????? 
						//???????? getBasebandCn0DbHz() ???????? getCn0DbHz()
						dbHz=basebandCn0DbHz?s.getBasebandCn0DbHz():s.getCn0DbHz();


						left=(int) stopLefX+(x+ size*i +padding);
						top=((int)(height/4))  -  ((int)dbHz) ;
						right=(int) stopLefX+(x+ size*(i+1));
						bottom=(int)height/4; //???????? height/4; 

						//???????????? ???????????????????? ?????????????? ?????????? ?? ???????????? ???????????? ??????????
						myRect.set(left,top,right,bottom);
						//???????????? ??????????????????????????
						canvas.drawRect(myRect, s.getUsedInFix()?paintFix:paintNoFix);

						//???????????????????????? ??????????????:
						//String text="????????????????"+i;
						String text=s.getConstellationType()+""+s.getSvid()+":  "+dbHz;
						// ???????????????????? ???????????? ????????????
						paintFix.getTextBounds(text, 0, text.length(), mTextBoundRect);
						//textWidth = textBounds.width();
						// ???????????????????? measureText ?????? ?????????????????? ????????????
						textWidth = paintFix.measureText(text);
						//textHeight = mTextBoundRect.height();
						// ???????????????????? ???????????? ????????????

						//?? ????????????, ?????????? ???????? ?????? ?????????????????? ???????????? ????????????
						//?????????????? ?????? ?????????? ?????? ?????????????? ?????????????????? ?? ???????????????? ???????????????? ???????????? view, ???? ???? ????????????????, ?? ?? ???????????????? +(size/2)
						xText=0+height-(textWidth+height/4) +(-size/2);//(??????????/????????)?????????????????? ??????????, ???????????? ???????????????????? ??????????????
						yText= left -width +(size-padding);//(??????????/????????????)?????????????????? ??????????, ???????????? ???????????????????? ??????????????
						canvas.save();
						canvas.scale(-1,-1);
						canvas.translate(-width,-height);
						canvas.rotate(90);
						canvas.drawText(text, xText,yText, s.getUsedInFix()?paintFix:paintNoFix);
						canvas.restore();
						//???????????????????????? ?????????????? 

						i++;
					}

					i=0;
				}


				if(xLength>=stopRightX){//???????? ???????? ???????????? ??????????????????????
					//?????????? ?????????????????? canvas ???????????????? ?? ??????????: (?? ?????????? stopRightX, ?? ???? ?? ?????????? e>=stopRightX (?????? ??????????????????????, ???????? ???? ???? ???????? ?????????? ?????????? ????????), ?????????? ?????????? ?????? ???? ???????????? ?? ???? ?? ?????? ??????????)
					//(xLength ???????????????? ?? stopRightX ?? ????????????. ?????? ???????????? ???????????????? ???? ????????, ?????????????? ???????????? ?? ?????????? stopRightX)

					for (MySatellite s : MainActivity. arraySat) {

						//?? ?????????????????????? ???? ???????????????????????????? ?? ?????????????????? ???????????????????????????? 
						//???????? getBasebandCn0DbHz() ???????? getCn0DbHz()
						dbHz=basebandCn0DbHz?s.getBasebandCn0DbHz():s.getCn0DbHz();


						left=(int) stopRightX+(x+ size*i +padding);
						top=((int)(height/4))  -  ((int)dbHz) ;
						right=(int) stopRightX+(x+ size*(i+1));
						bottom=(int)height/4; //???????? height/4; 

						//???????????? ???????????????????? ?????????????? ?????????? ?? ???????????? ???????????? ??????????
						myRect.set(left,top,right,bottom);
						//???????????? ??????????????????????????
						canvas.drawRect(myRect, s.getUsedInFix()?paintFix:paintNoFix);

						//???????????????????????? ??????????????:
						//String text="????????????????"+i;
						String text=s.getConstellationType()+""+s.getSvid()+":  "+dbHz;
						// ???????????????????? ???????????? ????????????
						paintFix.getTextBounds(text, 0, text.length(), mTextBoundRect);
						//textWidth = textBounds.width();
						// ???????????????????? measureText ?????? ?????????????????? ????????????
						textWidth = paintFix.measureText(text);
						//textHeight = mTextBoundRect.height();
						// ???????????????????? ???????????? ????????????

						//?? ????????????, ?????????? ???????? ?????? ?????????????????? ???????????? ????????????
						//?????????????? ?????? ?????????? ?????? ?????????????? ?????????????????? ?? ???????????????? ???????????????? ???????????? view, ???? ???? ????????????????, ?? ?? ???????????????? +(size/2)
						xText=0+height-(textWidth+height/4) +(-size/2);//(??????????/????????)?????????????????? ??????????, ???????????? ???????????????????? ??????????????
						yText= left -width +(size-padding);//(??????????/????????????)?????????????????? ??????????, ???????????? ???????????????????? ??????????????
						canvas.save();
						canvas.scale(-1,-1);
						canvas.translate(-width,-height);
						canvas.rotate(90);
						canvas.drawText(text, xText,yText, s.getUsedInFix()?paintFix:paintNoFix);
						canvas.restore();
						//???????????????????????? ?????????????? 

						i++;
					}

					i=0;

				}


			}//??????????, ???? ???????? ???? ????????????




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

        //?????? ?????? ???????????????? ???????????????? ???????????? ????????
		if(enable){//?????????? ???????? ?????????????????????? ???????????? ???????????? view


			switch (event.getAction()){

				case MotionEvent.ACTION_DOWN:
					xDown=event.getX();
					yDown=event.getY();
					break;

				case MotionEvent.ACTION_MOVE:



					if((xLength>=stopLefX)&(xLength<=stopRightX)){//???????? ???? ?????????????????? ?? ???????????? ????????????????

						//?????????????? ???? ?????????? ???????????????? ?? ???????????????????? ?? ?????? ???????????? ???????????????????? ????????????
						xLength=xMemoryLength+ (event.getX()-xDown);
						yLength=yMemoryLength+ (event.getY()-yDown);

					}else{//?????????? ???? ???????? ???? ??????????????

						//???????? ???? ?????????????????? ?????? ?????????????????????????? ???? ??????????????????
						if(xLength<=stopLefX){xLength=stopLefX;}//???????? ???? ???????? ?????????? ??????????????
						//???????? ???? ?????????????????? ?????? ?????????????????????????? ???? ??????????????????
						if(xLength>=stopRightX){xLength=stopRightX;}//???????? ???????? ????????????

					}//?????????? ???? ???????? ???? ??????????????




					break;

				case MotionEvent.ACTION_UP:
					xMemoryLength=xLength;
					yMemoryLength=yLength;
					break;
			}


			invalidate();

		}//?????? ?????? ???????????????? ???????????????? ???????????? ????????
		//if(enable){//?????????? ???????? ?????????????????? ???????????? ???????????? view


		return true;
	}



}
