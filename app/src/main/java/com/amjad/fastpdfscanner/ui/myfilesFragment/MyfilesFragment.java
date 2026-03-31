package com.amjad.fastpdfscanner.ui.myfilesFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;

import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.amjad.fastpdfscanner.R;
import com.amjad.fastpdfscanner.model.Pdflist;
import com.amjad.fastpdfscanner.utils.PdflistAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.*;
import java.util.*;

public class MyfilesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Pdflist> pdfList;
    private PdflistAdapter adapter;

    private Pdflist recentlyDeleted;
    private int recentlyDeletedPosition;

    private final ActivityResultLauncher<Intent> importLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) importPdfToMyFiles(uri);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfiles, container, false);

        recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MaterialButton btnImport = view.findViewById(R.id.btn_Import_files);
        btnImport.setOnClickListener(v -> openFilePicker());

        loadPdfList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPdfList();
    }

    private void loadPdfList() {
        pdfList = new ArrayList<>();

        File dir = new File(requireActivity().getExternalFilesDir(null), "MyFiles");
        if (dir.exists()) {
            File[] files = dir.listFiles(file -> file.getName().endsWith(".pdf"));
            if (files != null) {
                for (File file : files) {
                    pdfList.add(new Pdflist("", file.getName(),
                            formatSize(file.length()), file.getAbsolutePath()));
                }
            }
        }

        adapter = new PdflistAdapter(getContext(), pdfList, position -> showOptions(position));
        recyclerView.setAdapter(adapter);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        importLauncher.launch(intent);
    }

    private void importPdfToMyFiles(Uri uri) {
        try {
            File dir = new File(requireActivity().getExternalFilesDir(null), "MyFiles");
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, "PDF_" + System.currentTimeMillis() + ".pdf");

            InputStream is = requireContext().getContentResolver().openInputStream(uri);
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) fos.write(buffer, 0, len);

            is.close();
            fos.close();

            loadPdfList();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Import failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOptions(int position) {
        String[] options = {"Delete", "Delete All"};

        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) confirmDelete(position);
                    else confirmDeleteAll();
                }).show();
    }

    private void confirmDelete(int position) {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Delete this file?")
                .setPositiveButton("Yes", (d, i) -> deleteItem(position))
                .setNegativeButton("No", null)
                .show();
    }

    private void confirmDeleteAll() {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setMessage("Delete ALL files?")
                .setPositiveButton("Yes", (d, i) -> deleteAll())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteItem(int position) {
        recentlyDeleted = pdfList.get(position);
        recentlyDeletedPosition = position;

        File file = new File(recentlyDeleted.getPath());
        if (file.exists()) file.delete();

        pdfList.remove(position);
        adapter.notifyItemRemoved(position);

        Snackbar.make(recyclerView, "Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> undoDelete())
                .show();
    }

    private void undoDelete() {
        pdfList.add(recentlyDeletedPosition, recentlyDeleted);
        adapter.notifyItemInserted(recentlyDeletedPosition);
    }

    private void deleteAll() {
        for (Pdflist pdf : pdfList) {
            File file = new File(pdf.getPath());
            if (file.exists()) file.delete();
        }

        pdfList.clear();
        adapter.notifyDataSetChanged();

        Toast.makeText(getContext(), "All Deleted", Toast.LENGTH_SHORT).show();
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        else return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}