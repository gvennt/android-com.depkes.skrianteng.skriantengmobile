package com.depkes.skrianteng.skriantengmobile.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.depkes.skrianteng.skriantengmobile.R;
import com.depkes.skrianteng.skriantengmobile.adapter.HistoryAdapter;
import com.depkes.skrianteng.skriantengmobile.adapter.ItemArrayAdapter;
import com.depkes.skrianteng.skriantengmobile.adapter.VideoAdapter;
import com.depkes.skrianteng.skriantengmobile.data.DataUmur;
import com.depkes.skrianteng.skriantengmobile.database.model.EntryDeteksi;
import com.depkes.skrianteng.skriantengmobile.database.model.EntryJawaban;
import com.depkes.skrianteng.skriantengmobile.database.model.EntryVideo;
import com.depkes.skrianteng.skriantengmobile.pojo.DeteksiItem;
import com.depkes.skrianteng.skriantengmobile.pojo.JawabanItem;
import com.depkes.skrianteng.skriantengmobile.pojo.VideoItem;
import com.depkes.skrianteng.skriantengmobile.sqlite.DatabaseSqliteCallback;
import com.depkes.skrianteng.skriantengmobile.utility.FilePath;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DeteksiSoalActivity extends AppCompatActivity {
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    private TextView tvSoal, tvHasil, tvDesk, tvMeragukanSesuai;
    private RadioGroup radioGroup;
    private Button btnLanjut, btnKembali, btnRekam;
    private EditText etPertanyaan, etPertanyaan2, etPertanyaan3;
    private CardView cvRadio, cvVideo, cvPertanyaan;
    private VideoView vVideo;
    private ImageView red, green, blue, yellow, anjing, kuda, manusia, burung, kucing, circle;
    private String[] pertanyaanSatu, pertanyaanDua, pertanyaanTiga;
    private String[] arrSoal;
    private int indexSoal = 0;
    private String[] jawabanId;
//    private String[] jawabanVideo;
    private int[] jawaban;
    private VideoItem videoItem;
    private DeteksiItem deteksiItem;
    int kode;
    private String umur;
    private boolean isImageFit;
    private ArrayList<DeteksiItem> list = new ArrayList<>();
    private List<String> jawVideo;
    //    private ListView list;
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deteksi_soal);

        //        tvHasil = findViewById(R.id.tv_hasilDeteksi);
        tvDesk = findViewById(R.id.tvMeragukanSesuaiDesk);
        tvMeragukanSesuai = findViewById(R.id.tvMeragukanSesuai);
        tvSoal = findViewById(R.id.tv_soal);
        btnLanjut = findViewById(R.id.btnLanjut);
        btnKembali = findViewById(R.id.btnKembali);
        btnRekam = findViewById(R.id.btnRekam);
        etPertanyaan = findViewById(R.id.etPertanyaan1);
        etPertanyaan2 = findViewById(R.id.etPertanyaan2);
        etPertanyaan3 = findViewById(R.id.etPertanyaan3);

        radioGroup = findViewById(R.id.rg_radioGroup);
        cvRadio = findViewById(R.id.cardview_radio);
        cvVideo = findViewById(R.id.cvVideo);
        vVideo = findViewById(R.id.vVideo);
        red = findViewById(R.id.red);
        yellow = findViewById(R.id.yellow);
        green = findViewById(R.id.green);
        blue = findViewById(R.id.blue);
        circle = findViewById(R.id.circle);
        burung = findViewById(R.id.burung);
        kucing = findViewById(R.id.kucing);
        anjing = findViewById(R.id.anjing);
        kuda = findViewById(R.id.kuda);
        manusia = findViewById(R.id.manusia);
        cvPertanyaan = findViewById(R.id.cardView_Pertanyaan);
//        tvUmurBayi = findViewById(R.id.umurBayi);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vVideo);
        vVideo.setMediaController(mediaController);
//        list = findViewById(R.id.list_video);

        //---Get Intent Data---
        kode = getIntent().getExtras().getInt("KODE");
//        getIntent().putExtra("INDEKS",indeks);
        final String idDeteksi = getIntent().getExtras().getString("ID");
        String namaAnak = getIntent().getExtras().getString("NAMAANAK");
        String namaAyah = getIntent().getExtras().getString("NAMAAYAH");
        String namaIbu = getIntent().getExtras().getString("NAMAIBU");
        String tglLahir = getIntent().getExtras().getString("LAHIR");
        String tglDeteksi = getIntent().getExtras().getString("DETEKSI");
        umur = getIntent().getExtras().getString("UMUR");

        //---RecyclerView
        recyclerView = findViewById(R.id.list_video);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        //---Convert String to Date---
        Date dateLahir = Calendar.getInstance().getTime();
        Date dateDeteksi = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            dateLahir = sdf.parse(tglLahir);
            dateDeteksi = sdf.parse(tglDeteksi);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //---Get Video---

//        if (videoItem == null) {
//            videoItem = new VideoItem(idDeteksi, dateDeteksi);
        videoItem = new VideoItem(idDeteksi, indexSoal, dateDeteksi);
        Toast.makeText(this, "indexSoal : " + indexSoal, Toast.LENGTH_SHORT).show();
//        } else {
//            readVideo(this.deteksiItem.getId(), indexSoal);
//        }


        //---Get Soal---
        int idSoal = getResources().getIdentifier
                ("deteksi_" + String.valueOf(kode), "array", getPackageName());
        arrSoal = getResources().getStringArray(idSoal);
        jawaban = new int[arrSoal.length];
//        jawabanVideo = new String[arrSoal.length];
        jawabanId = new String[arrSoal.length];
        tvSoal.setText(arrSoal[indexSoal]);

        //---Toast Message---
//        for (int i = 0; i < jawabanId.length; i++) {
//            Toast.makeText(this, "" + jawabanId[i], Toast.LENGTH_SHORT).show();
//
//        }
        for (int i = 0; i < arrSoal.length; i++) {
            Toast.makeText(this, "soal : " + arrSoal[i], Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "ID Deteksi : " + idDeteksi, Toast.LENGTH_SHORT).show();

        //---Make Object---
        if (idDeteksi == null) {
            deteksiItem = new DeteksiItem(namaAnak, namaAyah, namaIbu, dateLahir, dateDeteksi);

        } else {
            deteksiItem = new DeteksiItem(idDeteksi, namaAnak, namaAyah, namaIbu, dateLahir, dateDeteksi);
            //---setId + RV
            //            list.addAll(DataUmur.getListData());
//            showItem();
            readDataJawaban(idDeteksi);
            setJawaban();
        }
        //---Set Title---
        setTitle(umur);
//        for (int i = 0; i < arrSoal.length; i++) {
//            Toast.makeText(DeteksiSoalActivity.this, "" + arrSoal[i], Toast.LENGTH_SHORT).show();
//        }

        //---Video Item---
        if(videoItem.getId_deteksi() != null){
            videoItem = new VideoItem(idDeteksi,indexSoal,dateDeteksi);
        }
        else{
            videoItem = new VideoItem(indexSoal,dateDeteksi);
        }

        //---Toast ID---
        Toast.makeText(this, "ID Deteksi : " + idDeteksi, Toast.LENGTH_SHORT).show();

//        videoItem.setId_deteksi(idDeteksi);
//        Toast.makeText(this, "" + idDeteksi, Toast.LENGTH_SHORT).show();
//        videoItem.setNo(indexSoal);
//        videoItem.setTgl_pemeriksaan(dateDeteksi);
//        videoItem.setId_deteksi(idDeteksi);
//        Toast.makeText(this, "" + idDeteksi, Toast.LENGTH_SHORT).show();
//        videoItem.setNo(indexSoal);
//        videoItem.setTgl_pemeriksaan(dateDeteksi);

        //---Event Button---
        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btnLanjut.getText().toString().equalsIgnoreCase("lanjut")) {

                    if (radioGroup.getCheckedRadioButtonId() == R.id.rb_ya) {
//                        Toast.makeText(DeteksiSoalActivity.this, "before" + indexSoal, Toast.LENGTH_SHORT).show();
                        jawaban[indexSoal++] = 1;
                        Toast.makeText(DeteksiSoalActivity.this, "after" + indexSoal, Toast.LENGTH_SHORT).show();
                    } else {
                        jawaban[indexSoal++] = 0;
                    }

                    if (indexSoal < arrSoal.length) {
                        btnKembali.setVisibility(View.VISIBLE);
                        tvSoal.setText(arrSoal[indexSoal]);
                        tampilEditTxt(kode, indexSoal);
                        tampilGambar(kode, indexSoal);
//                        updateVideo(idDeteksi);
                        setJawaban();
                    } else {
                        tampilHasil();
                    }
                } else {

                    if (deteksiItem.getId() == null)
                        writeAllData();
                    else

                        updateAllJawaban();

                    if (cvPertanyaan.getVisibility() == View.VISIBLE) {
                        for (int i = 0; i < 3; i++) {
                            pertanyaanSatu[i] = etPertanyaan.getText().toString();
                            pertanyaanDua[i] = etPertanyaan2.getText().toString();
                            pertanyaanTiga[i] = etPertanyaan3.getText().toString();
                        }
                    }

                    Intent intent = new Intent(DeteksiSoalActivity.this, MenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexSoal--;
                if (indexSoal == 0)
                    btnKembali.setVisibility(View.INVISIBLE);
                tampilEditTxt(kode, indexSoal);
                tampilGambar(kode, indexSoal);
                setJawaban();
            }
        });

        btnRekam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });
    }

    private void showItem() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoAdapter videoAdapter = new VideoAdapter(DeteksiSoalActivity.this);
        videoAdapter.setListVideo(jawVideo);
        recyclerView.setAdapter(videoAdapter);
    }

//    private void imageFullScreen(ImageView imageView){
//        imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//        imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//        if(isImageFit){
//                    isImageFit=false;
//                    imageView.setLayoutParams(new LayoutParams().WRAP_CONTENT,
//                            LayoutParams.WRAP_CONTENT);
//                    imageView.setAdjustViewBounds(true);
//                }
//                else{
//                    isImageFit = true;
//                    imageView.setLayoutParams(new LayoutParams().MATCH_PARENT,
//                            LayoutParams.MATCH_PARENT);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
//            }
//
//    private void setFullscreenImage(String imageResources) {
//        setContentView(R.layout.fullscreen_image);
//        View view = getLayoutInflater().inflate(R.layout.fullscreen_image, null);
//        ImageView imageView1 = findViewById(R.id.img_full);
////        ImageView resource = imageView.getId();
//        int id = getResources().getIdentifier(imageResources, "drawable", getPackageName());
//        imageView1.setImageResource(id);
//    }
    //    protected void onPostExecute(RecyclerView<String> list) {
//
////        if(list != null)
////        {
//                for(int i=0;i<jawVideo.size();i++) {
//                    videoAdapter = new VideoAdapter(DeteksiSoalActivity.this, jawVideo.get(i));
//                    this.list.setAdapter(videoAdapter);
//                    Toast.makeText(this, "list stete" + jawVideo.get(i), Toast.LENGTH_SHORT).show();
//                }
////        }

    private void readVideo(final String idDeteksi, int indexSoal) {
        String query = EntryVideo.getQueryVideo();
        String[] params = {idDeteksi, String.valueOf(indexSoal)};

        DatabaseSqliteCallback<VideoItem> callback = new DatabaseSqliteCallback<VideoItem>() {
            @Override
            public void onResponse(List<VideoItem> response) {

                for (int i = 0; i < response.size(); i++) {
                    VideoItem videoItem = response.get(i);
                    jawabanId[i] = videoItem.getId_deteksi();

                }

                VideoAdapter adapter = new VideoAdapter(response, DeteksiSoalActivity.this);
                jawVideo.addAll(videoAdapter.getListVideo());
                recyclerView.setAdapter(adapter);

                showItem();
            }


            @Override
            public void onFailure(Exception e) {

            }
        };
        MenuActivity.dbSkrianteng.getData(query, params, callback);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            vVideo.setVideoURI(videoUri);
//            jawabanVideo[indexSoal] = FilePath.getPath(this, videoUri);
            jawVideo.add(FilePath.getPath(this, videoUri));

            insertVideo();
            System.out.println("vido = " + videoUri);

            readVideo(videoItem.getId_deteksi(), indexSoal);
//            onPostExecute(jawVideo);
            Toast.makeText(this, "viddeeoo not null", Toast.LENGTH_SHORT).show();
//            for (int i = 0; i < jawVideo.size(); i++) {
//                Toast.makeText(this, "video = " + jawVideo.get(i), Toast.LENGTH_SHORT).show();
//            }
            showItem();
            intent.putExtra("URI",videoUri);
        }


    }

    private void setJawaban() {
        //---Pilihan---
        if (jawaban[indexSoal] == 1)
            radioGroup.check(R.id.rb_ya);
        else
            radioGroup.check(R.id.rb_tidak);

        //---Video---
//        if (jawabanVideo[indexSoal] != null) {
        if (jawVideo != null) {
//            Uri uri = Uri.fromFile(new File(jawabanVideo[indexSoal]));
            Uri uri = Uri.fromFile(new File(String.valueOf(jawVideo)));
            vVideo.setVideoURI(uri);
        } else {
            vVideo.setVideoURI(null);
            vVideo.setVisibility(View.GONE);
            vVideo.setVisibility(View.VISIBLE);
        }


    }

    private void tampilEditTxt(int kode, int index) {
        if ((kode == 12 && index == 2) ||
                (kode == 13 && indexSoal == 2) ||
                (kode == 14 && indexSoal == 3) ||
                (kode == 15 && indexSoal == 2)) {
            Log.d("DSA", "success " + kode + "," + indexSoal);
            cvPertanyaan.setVisibility(View.VISIBLE);
        } else {
            cvPertanyaan.setVisibility(View.GONE);
        }
    }

    private void tampilGambar(int kode, int index) {

        if (kode == 8 && index == 3 || kode == 9 && index == 1) {
            burung.setVisibility(View.VISIBLE);
            anjing.setVisibility(View.VISIBLE);
            kucing.setVisibility(View.VISIBLE);
            kuda.setVisibility(View.VISIBLE);
            manusia.setVisibility(View.VISIBLE);
        } else if (!(kode == 8 && index == 3 || kode == 9 && index == 1)) {
            burung.setVisibility(View.GONE);
            anjing.setVisibility(View.GONE);
            kucing.setVisibility(View.GONE);
            kuda.setVisibility(View.GONE);
            manusia.setVisibility(View.GONE);
        }

        if (kode == 12 && index == 1 || kode == 13 && index == 1 || kode == 14 && index == 1 || kode == 15 && index == 0) {
            blue.setVisibility(View.VISIBLE);
            yellow.setVisibility(View.VISIBLE);
            green.setVisibility(View.VISIBLE);
            red.setVisibility(View.VISIBLE);
        } else if (!(kode == 12 && index == 1 || kode == 13 && index == 1 || kode == 14 && index == 1 || kode == 15 && index == 0)) {
            blue.setVisibility(View.GONE);
            yellow.setVisibility(View.GONE);
            green.setVisibility(View.GONE);
            red.setVisibility(View.GONE);
        }
    }


    private void tampilHasil() {
        btnLanjut.setText("SELESAI");
        btnKembali.setVisibility(View.INVISIBLE);
        cvVideo.setVisibility(View.GONE);
        cvRadio.setVisibility(View.GONE);
        cvPertanyaan.setVisibility(View.GONE);

        //---Check Apakah Ada Jawaban Tidak---
        boolean adaJawabanTidak = false;
        for (int i = 0; i < jawaban.length; i++) {
            if (jawaban[i] == 0) {
                adaJawabanTidak = true;
                break;
            }
        }

        //---Tampil Hasil---
        if (!adaJawabanTidak) {
            tvMeragukanSesuai.setVisibility(View.VISIBLE);
            tvDesk.setVisibility(View.VISIBLE);
            tvMeragukanSesuai.setText("SESUAI (S)");
            cvRadio.setVisibility(View.INVISIBLE);
            radioGroup.setVisibility(View.INVISIBLE);
            tvSoal.setVisibility(View.GONE);
            tvDesk.setText("Selamat, perkembangan bicara dan bahasa anak Anda telah  sesuai." +
                    " Teruskan  pola asuh dan ikutkan dalam kegiatan posyandu.");
        } else {
//            tvHasil.setVisibility(View.VISIBLE);
            tvMeragukanSesuai.setVisibility(View.VISIBLE);
            tvDesk.setVisibility(View.VISIBLE);
            tvSoal.setVisibility(View.GONE);
            tvMeragukanSesuai.setText("MERAGUKAN (M)");
            tvDesk.setText("Segera lakukan stimulasi pada anak Anda. Pilih di tabel menu bagian ayo stimulasi dan sesuaikan dengan usia anak Anda. Lalu lakukan  deteksi ulang 2 minggu lagi," +
                    " jika masih meragukan bawa anak ke puskesmas terdekat untuk dilakukan tindak lanjut segera keterlambatan bicara pada anak anda");

        }
    }

    private void writeAllData() {
        //---Insert Record Deteksi---
        MenuActivity.dbSkrianteng.insertData(EntryDeteksi.TABLE_NAME, deteksiItem);

        //---Get Last Inserted ID---
        String query = EntryDeteksi.getQueryLastDeteksi();
        DatabaseSqliteCallback<DeteksiItem> callback = new DatabaseSqliteCallback<DeteksiItem>() {
            @Override
            public void onResponse(List<DeteksiItem> response) {
                DeteksiItem lastDeteksi = response.get(0);

                //---Insert Every Record Jawaban---
                for (int i = 0; i < jawaban.length; i++) {
                    JawabanItem jawabanItem = new JawabanItem(lastDeteksi.getId(), i, jawaban[i], etPertanyaan.getText().toString(),
                            etPertanyaan2.getText().toString(),
                            etPertanyaan3.getText().toString()
//                            jawabanVideo[i]
                    );


                    MenuActivity.dbSkrianteng.insertData(EntryJawaban.TABLE_NAME, jawabanItem);
//                    System.out.println("TEST W" + jawabanVideo[i]);
//                    System.out.println("TEST W" + jawVideo[i]);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        MenuActivity.dbSkrianteng.getData(query, null, callback);
    }

    private void updateAllJawaban() {
        String whereClause = EntryJawaban.COLUMN_ID + "=?";
        for (int i = 0; i < jawabanId.length; i++) {
            JawabanItem jawabanItem = new JawabanItem(deteksiItem.getId(), i, jawaban[i], etPertanyaan.getText().toString(),
                    etPertanyaan2.getText().toString(),
                    etPertanyaan3.getText().toString()
//                    jawabanVideo[i]
            );
            String[] params = {jawabanId[i]};
            MenuActivity.dbSkrianteng.updateData(EntryJawaban.TABLE_NAME, jawabanItem, whereClause, params);
        }
    }

//    private void updateVideo(String idDeteksi) {
//        String whereClause = EntryVideo.COLUMN_ID + "=?";
//
//        for (int i = 0; i < jawabanVideo.length; i++) {
//            JawabanItem jawabanItem = new JawabanItem(deteksiItem.getId(), i, jawabanVideo[i]);
//            String[] params = {idDeteksi, jawabanId[i]};
//            MenuActivity.dbSkrianteng.updateData(EntryVideo.TABLE_NAME, jawabanItem, whereClause, params);
//        }
//    }

    private void insertVideo() {
        //--- Insert video
//            MenuActivity.dbSkrianteng.insertData(EntryVideo.TABLE_NAME,videoItem);

        //---Get Last Inserted ID---
        String query = EntryVideo.getQueryAllVideo();

        DatabaseSqliteCallback<VideoItem> callback = new DatabaseSqliteCallback<VideoItem>() {
            @Override
            public void onResponse(List<VideoItem> response) {
                VideoItem lastDeteksi = response.get(0);

                //---Insert Every Record Jawaban---
                for (int i = 0; i < response.size(); i++) {
                    VideoItem jawabanItem = new VideoItem(lastDeteksi.getId_deteksi(),
                            lastDeteksi.getTgl_pemeriksaan(),
                            jawVideo.get(i),
                            i);
                    MenuActivity.dbSkrianteng.insertData(EntryVideo.TABLE_NAME, jawabanItem);
//                    System.out.println("TEST W" + jawVideo.get(i));
                    Toast.makeText(DeteksiSoalActivity.this, "ID VIDEO : " + jawabanItem.getId_deteksi().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        MenuActivity.dbSkrianteng.getData(query, null, callback);
    }

//    private void readDataVideo( String id, int indexSoal) {
//        String query = EntryVideo.getQueryVideo();
//        String[] params = {idDeteksi, String.valueOf(this.indexSoal)};
//        DatabaseSqliteCallback<VideoItem> callback = new DatabaseSqliteCallback<VideoItem>() {
//            @Override
//            public void onResponse(List<VideoItem> response) {
//                for (int i = 0; i < response.size(); i++) {
//                    VideoItem jawabanItem = response.get(i);
//                    jawabanId[i] = jawabanItem.getId_deteksi();
//
//                    System.out.println("TEST R" + jawVideo.get(i));
//                    Toast.makeText(DeteksiSoalActivity.this,
//                            "video : " + jawVideo.get(i), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//
//            }
//        };
//        MenuActivity.dbSkrianteng.getData(query, params, callback);
//    }

    private void readDataJawaban(String idDeteksi) {
        String query = EntryJawaban.getQueryJawaban();
        String[] params = {idDeteksi};
        DatabaseSqliteCallback<JawabanItem> callback = new DatabaseSqliteCallback<JawabanItem>() {
            @Override
            public void onResponse(List<JawabanItem> response) {
                for (int i = 0; i < response.size(); i++) {
                    JawabanItem jawabanItem = response.get(i);
                    jawabanId[i] = jawabanItem.getId();
                    jawaban[i] = jawabanItem.getJawaban();
//                    jawabanVideo[i] = jawabanItem.getVideo();
                    etPertanyaan.setText(jawabanItem.getJawaban_editText_satu());
                    etPertanyaan2.setText(jawabanItem.getJawaban_editText_dua());
                    etPertanyaan3.setText(jawabanItem.getJawaban_editText_tiga());
                    setTitle(umur);

                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        MenuActivity.dbSkrianteng.getData(query, params, callback);
    }

}
