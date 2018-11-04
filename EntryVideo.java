package com.depkes.skrianteng.skriantengmobile.database.model;

public class EntryVideo {

    public static final String TABLE_NAME = "tblVideo";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("+
            " id_video INTEGER PRIMARY KEY AUTOINCREMENT," +
            "id_deteksi INTEGER," +
            "nomor_soal INTEGER(1), " +
            "tgl_pemeriksaan DATE, " +
            "video TEXT );";

    public static final String COLUMN_ID = "id_video";
    public static final String COLUMN_ID_DETEKSI = "id_deteksi";
    public static final String COLUMN_NOMOR_SOAL = "nomor_soal";
    public static final String COLUMN_TGL_PEMERIKSAAN = "tgl_pemeriksaan";
    public static final String COLUMN_VIDEO = "video";

    public static String getQueryAllVideo (){
        return "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID  +" DESC";
    }

    public static String getQueryVideo(){
        return "SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_ID_DETEKSI+" =? AND " + COLUMN_NOMOR_SOAL + " =?";
    }
}
