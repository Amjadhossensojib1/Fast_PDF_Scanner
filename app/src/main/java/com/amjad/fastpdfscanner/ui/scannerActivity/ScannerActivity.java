package com.amjad.fastpdfscanner.ui.scannerActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.amjad.fastpdfscanner.PdfViewerFragment;
import com.amjad.fastpdfscanner.R;
import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    private TextView tvDone, tvShare, tvPageCount;
    private ImageView ivMerge, ivEdit, ivText, ivSignature, ivReorder;
    private ImageView ivScannedPreview;
    private ImageView btnCapture;
    private PreviewView cameraPreview;
    private MaterialButton btnSaveInFiles;

    // CameraX
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private ProcessCameraProvider cameraProvider;

    private final ArrayList<Bitmap> scannedPages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        cameraExecutor = Executors.newSingleThreadExecutor();

        initViews();
        setupClickListeners();
        updateUI();
        requestCameraPermission();
    }

    private void initViews() {
        tvDone           = findViewById(R.id.tv_done);
        tvShare          = findViewById(R.id.tv_share);
        tvPageCount      = findViewById(R.id.tv_page_count);
        cameraPreview    = findViewById(R.id.camera_preview);
        ivScannedPreview = findViewById(R.id.iv_scanned_preview);
        btnCapture       = findViewById(R.id.btn_capture);
        ivMerge          = findViewById(R.id.iv_merge);
        ivEdit           = findViewById(R.id.iv_edit);
        ivText           = findViewById(R.id.iv_text);
        ivSignature      = findViewById(R.id.iv_signeture);
        ivReorder        = findViewById(R.id.iv_reorder);
        btnSaveInFiles   = findViewById(R.id.btn_save_in_files);
    }
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(this, "Allow camera!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(this);

        future.addListener(() -> {
            try {
                cameraProvider = future.get();
                bindCameraUseCases();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build();

        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

        cameraPreview.setVisibility(android.view.View.VISIBLE);
        ivScannedPreview.setVisibility(android.view.View.GONE);
    }

    private void capturePhoto() {
        if (imageCapture == null) return;

        imageCapture.takePicture(
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        Bitmap bitmap = imageProxyToBitmap(image);
                        image.close();

                        if (bitmap != null) {
                            scannedPages.add(bitmap);

                            runOnUiThread(() -> {
                                ivScannedPreview.setImageBitmap(bitmap);
                                ivScannedPreview.setVisibility(android.view.View.VISIBLE);
                                cameraPreview.setVisibility(android.view.View.GONE);
                                updateUI();
                                Toast.makeText(ScannerActivity.this,
                                        "✓ Page " + scannedPages.size() + "Added",
                                        Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        runOnUiThread(() ->
                                Toast.makeText(ScannerActivity.this,
                                        "There was a problem taking the picture!", Toast.LENGTH_SHORT).show());
                    }
                }
        );
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        try {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupClickListeners() {

        btnCapture.setOnClickListener(v -> capturePhoto());

        ivMerge.setOnClickListener(v -> {
            if (cameraProvider != null) {
                bindCameraUseCases(); // ক্যামেরা আবার চালু
            } else {
                startCamera();
            }
        });

        // Done
        tvDone.setOnClickListener(v -> {
            if (!scannedPages.isEmpty())
                Toast.makeText(this, scannedPages.size() + " The page is ready", Toast.LENGTH_SHORT).show();
        });

        tvShare.setOnClickListener(v -> {
            if (!scannedPages.isEmpty()) {
                File pdf = createPdf("shared_" + System.currentTimeMillis());
                if (pdf != null) sharePdf(pdf);
            }
        });

        btnSaveInFiles.setOnClickListener(v -> {
            if (scannedPages.isEmpty()) {
                Toast.makeText(this, "No pages scanned!", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = "Scan_" + new SimpleDateFormat(
                    "yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File pdf = createPdf(name);
            if (pdf != null) showPdfInFragment(pdf);
        });

        ivEdit.setOnClickListener(v ->
                Toast.makeText(this, "Edit Page", Toast.LENGTH_SHORT).show());
        ivText.setOnClickListener(v ->
                Toast.makeText(this, "Add Text", Toast.LENGTH_SHORT).show());
        ivSignature.setOnClickListener(v ->
                Toast.makeText(this, "Signature", Toast.LENGTH_SHORT).show());
        ivReorder.setOnClickListener(v ->
                Toast.makeText(this, "Reorder Pages", Toast.LENGTH_SHORT).show());
    }

    // ─── UI Update ────────────────────────────────────────────────────────────
    private void updateUI() {
        boolean hasPages = !scannedPages.isEmpty();
        int activeColor  = ContextCompat.getColor(this, R.color.button_color);

        tvDone.setEnabled(hasPages);
        tvDone.setTextColor(hasPages ? activeColor : Color.GRAY);

        tvShare.setEnabled(hasPages);
        tvShare.setTextColor(hasPages ? activeColor : Color.GRAY);

        btnSaveInFiles.setEnabled(hasPages);
        btnSaveInFiles.setAlpha(hasPages ? 1.0f : 0.5f);

        tvPageCount.setText(scannedPages.size() + " Page");
    }

    private File createPdf(String fileName) {
        PdfDocument doc = new PdfDocument();
        try {
            for (int i = 0; i < scannedPages.size(); i++) {
                Bitmap bmp = scannedPages.get(i);
                PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(
                        bmp.getWidth(), bmp.getHeight(), i + 1).create();
                PdfDocument.Page page = doc.startPage(info);
                page.getCanvas().drawBitmap(bmp, 0, 0, null);
                doc.finishPage(page);
            }
            File dir = new File(getExternalFilesDir(null), "MyFiles");
            if (!dir.exists()) dir.mkdirs();
            File pdfFile = new File(dir, fileName + ".pdf");
            FileOutputStream fos = new FileOutputStream(pdfFile);
            doc.writeTo(fos);
            fos.flush();
            fos.close();
            return pdfFile;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Problem creating PDF!", Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            doc.close();
        }
    }

    private void showPdfInFragment(File pdfFile) {
        Uri uri = FileProvider.getUriForFile(
                this, getPackageName() + ".provider", pdfFile);
        PdfViewerFragment fragment = PdfViewerFragment.newInstance(
                uri.toString(), pdfFile.getName());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        Toast.makeText(this, "Saved: " + pdfFile.getName(), Toast.LENGTH_LONG).show();
    }

    private void sharePdf(File pdfFile) {
        Uri uri = FileProvider.getUriForFile(
                this, getPackageName() + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share the PDF"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}