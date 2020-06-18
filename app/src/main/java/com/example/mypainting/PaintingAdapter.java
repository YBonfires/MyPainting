package com.example.mypainting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mypainting.gson.Painting;

import java.util.List;

public class PaintingAdapter extends RecyclerView.Adapter<PaintingAdapter.ViewHolder> {
    private List<Painting> mPaintingList;
  static class ViewHolder extends RecyclerView.ViewHolder{
      ImageView paintingImg;
      TextView paintingTopic;
      public ViewHolder(View view){
          super(view);
          paintingImg=view.findViewById(R.id.painting);
          paintingTopic=view.findViewById(R.id.painting_topic);

      }
  }
  public PaintingAdapter(List<Painting> paintingList){
      mPaintingList=paintingList;
  }
  public ViewHolder onCreateViewHolder(ViewGroup parent,int ViewType){
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.painting_item,parent,false);
      ViewHolder holder= new ViewHolder(view);
      return holder;
  }
  public void onBindViewHolder(ViewHolder holder,int position){
      Painting painting=mPaintingList.get(position);
     // holder.paintingTopic.setText(painting.getTopic());
      //holder.paintingImg.setImageResource(painting.getPaintingId());
  }
  public int getItemCount(){
      return mPaintingList.size();
  }
}
