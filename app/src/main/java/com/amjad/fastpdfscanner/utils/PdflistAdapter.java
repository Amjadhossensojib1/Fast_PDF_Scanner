package com.amjad.fastpdfscanner.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amjad.fastpdfscanner.R;
import com.amjad.fastpdfscanner.model.Pdflist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PdflistAdapter extends RecyclerView.Adapter<PdflistAdapter.PdflistViewHolder>{
    private List<Pdflist> pdfList;
    PdflistClickListener listener;

    public PdflistAdapter(List<Pdflist> pdfList) {
        this.pdfList = pdfList;
    }

    @NonNull
    @Override
    public PdflistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_pdflist, parent,false);
        return new PdflistAdapter.PdflistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdflistViewHolder holder, int position) {
        Pdflist pdflist = pdfList.get(position);
        holder.bind(pdflist);

    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public class PdflistViewHolder extends RecyclerView.ViewHolder {
        private ImageView pdfImage;
        private TextView pdfName,pdfSize;
        public PdflistViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfImage = itemView.findViewById(R.id.pdfImage);
            pdfName = itemView.findViewById(R.id.tvPdfName);
            pdfSize = itemView.findViewById(R.id.tvPdfSize);
        }

        public void bind(Pdflist pdflist) {
            Picasso.get().load(pdflist.getPdffileImage()).into(pdfImage);
            pdfName.setText(pdflist.getPdffileName());
            pdfSize.setText(pdflist.getPdffileSize());

        }
    }
}
