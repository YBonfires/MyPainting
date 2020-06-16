package com.example.mypainting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mypainting.gson.*;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Album extends AppCompatActivity {
    private List<Painting> paintingList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initPaintings();
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        PaintingAdapter adapter=new PaintingAdapter(paintingList);
        recyclerView.setAdapter(adapter);
    }
    private void initPaintings(){
        Painting zhu=new Painting("猪",R.drawable.zbj);
        paintingList.add(zhu);
        Painting hou=new Painting("猴",R.drawable.swk);
        paintingList.add(hou);
        Painting ren=new Painting("人",R.drawable.ts);
        paintingList.add(ren);
        Painting xiong=new Painting("熊",R.drawable.xiong);
        paintingList.add(xiong);

    }
}