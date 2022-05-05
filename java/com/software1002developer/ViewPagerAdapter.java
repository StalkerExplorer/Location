package com.software1002developer.gps;

import android.content.*;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.*;
import androidx.viewpager2.widget.*;
import java.util.*;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> implements SharedConstants
{


	public ViewPagerAdapter(){}

	
	//Вызывается, когда RecyclerView требуется новый RecyclerView.ViewHolder заданного типа для представления элемента.
	@Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(layout[viewType], parent, false);
		return new ViewHolder(view);
    }
	
	
	
	//Возвращает тип представления элемента в позиции для повторного использования представления.
	@Override
    public int getItemViewType(int position) {
		return position;
    }
	
	

	//Вызывается RecyclerView для отображения данных в указанной позиции.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //ничего пока не биндим
    }


	//Возвращает общее количество элементов в наборе данных, удерживаемом адаптером.
    @Override
    public int getItemCount() {
        return layout.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
     
		ViewHolder(View itemView)
		{
            super(itemView); 
		}
    }

}


