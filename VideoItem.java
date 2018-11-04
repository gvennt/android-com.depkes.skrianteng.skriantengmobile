package com.depkes.skrianteng.skriantengmobile.pojo;

import java.util.Date;
import java.util.List;

public class VideoItem {
    private String video;
    private VideoItem videoItem;
    private String id_deteksi;
    private Date tgl_pemeriksaan;
    private int no;

    public VideoItem(String id_deteksi, Date tgl_pemeriksaan, String video, int no) {
        this.id_deteksi = id_deteksi;
        this.tgl_pemeriksaan = tgl_pemeriksaan;
        this.video = video;
        this.no = no;
    }

    public VideoItem(String id_deteksi, int no, Date tgl_pemeriksaan) {
        this.id_deteksi = id_deteksi;
        this.tgl_pemeriksaan = tgl_pemeriksaan;
    }

    public VideoItem(int no, Date tgl_pemeriksaan) {
        this.no = no;
        this.tgl_pemeriksaan = tgl_pemeriksaan;
    }

    public VideoItem(int position) {

    }

    public void setTgl_pemeriksaan(Date tgl_pemeriksaan) {
        this.tgl_pemeriksaan = tgl_pemeriksaan;
    }


    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getId_deteksi() {
        return id_deteksi;
    }

    public void setId_deteksi(String id_deteksi) {
        this.id_deteksi = id_deteksi;
    }


    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Date getTgl_pemeriksaan() {
        return tgl_pemeriksaan;
    }
}
