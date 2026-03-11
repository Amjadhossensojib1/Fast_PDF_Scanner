package com.amjad.fastpdfscanner.model;

public class Pdflist {
    private String  pdffileImage;
    private String pdffileName;
    private String pdffileSize;

    public Pdflist(String pdffileImage, String pdffileName, String pdffileSize) {
        this.pdffileImage = pdffileImage;
        this.pdffileName = pdffileName;
        this.pdffileSize = pdffileSize;
    }

    public String getPdffileImage() {
        return pdffileImage;
    }

    public void setPdffileImage(String pdffileImage) {
        this.pdffileImage = pdffileImage;
    }

    public String getPdffileName() {
        return pdffileName;
    }

    public void setPdffileName(String pdffileName) {
        this.pdffileName = pdffileName;
    }

    public String getPdffileSize() {
        return pdffileSize;
    }

    public void setPdffileSize(String pdffileSize) {
        this.pdffileSize = pdffileSize;
    }
}
