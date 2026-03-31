package com.amjad.fastpdfscanner.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.amjad.fastpdfscanner.R;
import com.amjad.fastpdfscanner.model.Pdflist;

import java.io.File;
import java.util.ArrayList;

public class PdflistAdapter extends RecyclerView.Adapter<PdflistAdapter.ViewHolder> {

    private final ArrayList<Pdflist> pdfList;
    private final Context context;
    private final OnItemClick listener;

    public interface OnItemClick {
        void onMoreClick(int position);
    }

    public PdflistAdapter(Context context, ArrayList<Pdflist> pdfList, OnItemClick listener) {
        this.context = context;
        this.pdfList = pdfList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_pdflist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pdflist pdf = pdfList.get(position);

        holder.tvPdfName.setText(pdf.getTitle());
        holder.tvPdfSize.setText(pdf.getSize());
        holder.pdfImage.setImageResource(R.drawable.logo);

        holder.btnShare.setOnClickListener(v -> {
            File file = new File(pdf.getPath());
            if (file.exists()) {
                Uri uri = FileProvider.getUriForFile(
                        context, context.getPackageName() + ".provider", file);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                context.startActivity(Intent.createChooser(intent, "PDF শেয়ার করুন"));
            } else {
                Toast.makeText(context, "ফাইল পাওয়া যাচ্ছে না!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnMore.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMoreClick(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            File file = new File(pdf.getPath());
            if (file.exists()) {
                Uri uri = FileProvider.getUriForFile(
                        context, context.getPackageName() + ".provider", file);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);

                context.startActivity(intent);
            } else {
                Toast.makeText(context, "ফাইল পাওয়া যাচ্ছে না!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPdfName, tvPdfSize;
        ImageView pdfImage, btnShare, btnMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfImage  = itemView.findViewById(R.id.pdfImage);
            tvPdfName = itemView.findViewById(R.id.tvPdfName);
            tvPdfSize = itemView.findViewById(R.id.tvPdfSize);
            btnShare  = itemView.findViewById(R.id.btnShare);
            btnMore   = itemView.findViewById(R.id.btnMore);
        }
    }
}