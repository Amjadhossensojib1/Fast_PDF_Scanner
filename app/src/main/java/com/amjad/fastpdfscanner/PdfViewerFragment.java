package com.amjad.fastpdfscanner;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PdfViewerFragment extends Fragment {

    private static final String ARG_URI  = "pdf_uri";
    private static final String ARG_NAME = "pdf_name";

    public static PdfViewerFragment newInstance(String uriString, String fileName) {
        PdfViewerFragment f = new PdfViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URI,  uriString);
        args.putString(ARG_NAME, fileName);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pdf_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvName = view.findViewById(R.id.tv_pdf_filename);
        WebView  webView = view.findViewById(R.id.webview_pdf);

        if (getArguments() == null) return;

        String uriString = getArguments().getString(ARG_URI, "");
        String fileName  = getArguments().getString(ARG_NAME, "Document.pdf");

        tvName.setText(fileName);

        Uri uri = Uri.parse(uriString);
        String realPath = getRealPath(uri);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("file://" + realPath);
    }

    private String getRealPath(Uri uri) {

        String path = uri.getPath();
        if (path != null && path.startsWith("/external_files")) {
            String relative = path.replace("/external_files", "");
            return requireContext().getExternalFilesDir(null).getAbsolutePath() + relative;
        }
        return path != null ? path : "";
    }
}