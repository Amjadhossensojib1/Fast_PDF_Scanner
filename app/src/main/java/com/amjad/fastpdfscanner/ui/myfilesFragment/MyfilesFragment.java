package com.amjad.fastpdfscanner.ui.myfilesFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amjad.fastpdfscanner.R;
import com.amjad.fastpdfscanner.model.Pdflist;
import com.amjad.fastpdfscanner.utils.PdflistAdapter;

import java.util.ArrayList;

public class MyfilesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Pdflist> pdfList;
    private PdflistAdapter adapter;

    public MyfilesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfiles, container, false);

        recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pdfList = new ArrayList<>();
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVa1sfZZyTUYq9g9WZNG2x5HFLof6IOchMRA&s","pdf 1","20 KB"));
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVa1sfZZyTUYq9g9WZNG2x5HFLof6IOchMRA&s","pdf 2","57 KB"));
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTClx9Uqd8_tzvs2uupiHBDY49cUC536Ptb-Q&s","pdf 3","80 KB"));
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTClx9Uqd8_tzvs2uupiHBDY49cUC536Ptb-Q&s","pdf 4","57 KB"));
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVa1sfZZyTUYq9g9WZNG2x5HFLof6IOchMRA&s","pdf 5","50 KB"));
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVa1sfZZyTUYq9g9WZNG2x5HFLof6IOchMRA&s","pdf 5","50 KB"));
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVa1sfZZyTUYq9g9WZNG2x5HFLof6IOchMRA&s","pdf 5","50 KB"));
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVa1sfZZyTUYq9g9WZNG2x5HFLof6IOchMRA&s","pdf 5","50 KB"));
        pdfList.add(new Pdflist("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVa1sfZZyTUYq9g9WZNG2x5HFLof6IOchMRA&s","pdf 5","50 KB"));


        adapter = new PdflistAdapter(pdfList);
        recyclerView.setAdapter(adapter);




        return view;
    }
}