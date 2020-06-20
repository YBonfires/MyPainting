package com.example.mypainting;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mypainting.gson.Painting;

import java.util.BitSet;
import java.util.List;

public class PaintingAdapter extends RecyclerView.Adapter<PaintingAdapter.ViewHolder> {
    private List<Bitmap> mPaintingList;
  static class ViewHolder extends RecyclerView.ViewHolder{
      ImageView paintingImg;
      public ViewHolder(View view){
          super(view);
          paintingImg=view.findViewById(R.id.painting);
      }
  }
  public PaintingAdapter(List<Bitmap> paintingList){
      mPaintingList=paintingList;
  }
  public ViewHolder onCreateViewHolder(ViewGroup parent,int ViewType){
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.painting_item,parent,false);
      ViewHolder holder= new ViewHolder(view);
      return holder;
  }
  public void onBindViewHolder(ViewHolder holder,int position){
      Bitmap bitmap=mPaintingList.get(position);
     // holder.paintingTopic.setText(painting.getTopic());
      holder.paintingImg.setImageBitmap(bitmap);
  }
  public int getItemCount(){
      return mPaintingList.size();
  }
}
