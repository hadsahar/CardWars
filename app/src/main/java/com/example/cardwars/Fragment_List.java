package com.example.cardwars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Fragment_List extends Fragment {

    private ArrayList<String> ImageURLs = new ArrayList<>();
    private ArrayList<Player_Winner> winner = new ArrayList<>();
    private CallBack_top callBack_top;

    public void setCallBack_top(CallBack_top callBack_top) {
        this.callBack_top = callBack_top;
    }

    public Fragment_List() {
    }

    public Fragment_List(ArrayList<Player_Winner> winner) {
        this.winner = winner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscores_list,container,false);
        initImageURLs(view);
        return view;
    }

    // Set the rank images at the imageURLs arrayList.
    // These images will be shown at the high score screen by order.
    private void initImageURLs(View view){
        ImageURLs.add(getString(R.string.url_image1));
        ImageURLs.add(getString(R.string.url_image2));
        ImageURLs.add(getString(R.string.url_image3));
        ImageURLs.add(getString(R.string.url_image4));
        ImageURLs.add(getString(R.string.url_image5));
        ImageURLs.add(getString(R.string.url_image6));
        ImageURLs.add(getString(R.string.url_image7));
        ImageURLs.add(getString(R.string.url_image8));
        ImageURLs.add(getString(R.string.url_image9));
        ImageURLs.add(getString(R.string.url_image10));
        initRecycleView(view);
    }

    // initialize the recycler view.
    private void initRecycleView(View view){
        RecyclerView recycleView = view.findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(ImageURLs,winner,this.getContext());
        recycleView.setAdapter(adapter);
        adapter.setCallBack_top(this.callBack_top);
        recycleView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }


}
