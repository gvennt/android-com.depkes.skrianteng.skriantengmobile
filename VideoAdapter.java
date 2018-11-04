package com.depkes.skrianteng.skriantengmobile.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.depkes.skrianteng.skriantengmobile.R;
import com.depkes.skrianteng.skriantengmobile.activity.DeteksiSoalActivity;
import com.depkes.skrianteng.skriantengmobile.activity.MenuActivity;
import com.depkes.skrianteng.skriantengmobile.database.model.EntryDeteksi;
import com.depkes.skrianteng.skriantengmobile.database.model.EntryJawaban;
import com.depkes.skrianteng.skriantengmobile.database.model.EntryVideo;
import com.depkes.skrianteng.skriantengmobile.pojo.DeteksiItem;
import com.depkes.skrianteng.skriantengmobile.pojo.VideoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<String> listVideo;
    private Context context;
    String id;
    String index;
    public VideoAdapter(ArrayList<String> listVideo, Context context) {
        this.listVideo = listVideo;
        this.context = context;
    }

    public VideoAdapter(List<VideoItem> response, Context context) {
        this.context = context;
    }

    public VideoAdapter(DeteksiSoalActivity deteksiSoalActivity) {
        context = deteksiSoalActivity;
    }

    public List<String> getListVideo() {
        return listVideo;
    }

    public void setListVideo(List<String> listVideo) {
        this.listVideo = listVideo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVideo = LayoutInflater.from(parent.getContext()).inflate(R.layout.listvideo,parent,false);
        return  new ViewHolder(itemVideo);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_list.setText("Video " + position);
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

        final DeteksiSoalActivity deteksiSoalActivity =  new DeteksiSoalActivity();
//        final String idDeteksi =deteksiSoalActivity.getIntent().getExtras().getString("ID");

        final VideoItem videoItem1 = new VideoItem(position);
//        final String indeksSoal = deteksiSoalActivity.getIntent().getExtras().getString("INDEKS");
        String uri = videoItem1.getVideo();
        if(uri != null) {
            Uri videoUri = Uri.parse(deteksiSoalActivity.getIntent().getExtras().getString("URI"));
            holder.mVideoView.setVideoURI(videoUri);
        }

        id = videoItem1.getId_deteksi();
        index = String.valueOf(videoItem1.getNo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "video " + position, Toast.LENGTH_SHORT).show();
//                Toast.makeText(deteksiSoalActivity, "Uri : " + , Toast.LENGTH_SHORT).show();

//                deteksiSoalActivity.onActivityResult();
        //TODO : ma..maa
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("no : " + index);
                System.out.println("id : " + id);
                int indeks = Integer.parseInt(index);
                deleteData(id,indeks);
//                AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
//                builder.setTitle("Hapus Data");
//                builder.setMessage("Apakah anda yakin ingin menghapus data ?");
//                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listVideo.size();
    }

//    private Activity activity;
//    private String video;
//    private static LayoutInflater inflater = null;
//
//    public VideoAdapter(Activity activity, String video) {
//        this.activity = activity;
//        this.video = video;
//        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//    @Override
//    public int getCount() {
//        return video.length();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return i;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    public static class ViewHolder{
//        public TextView tv_list_video;
//
//
//    }
//    @Override
//    public View getView(final int i, View view, ViewGroup viewGroup) {
////        View v = view;
//        final ViewHolder holder;
//        if(view == null || view.getTag() == null        ){
//            view = inflater.inflate(R.layout.listvideo ,viewGroup,false);
//            holder = new ViewHolder();
//            holder.tv_list_video = view.findViewById(R.id.tv_video);
//            view.setTag(holder);
//        }
//        else holder = (ViewHolder) view.getTag();
//            holder.tv_list_video.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(activity.getApplicationContext(), ""+i, Toast.LENGTH_SHORT).show();
//                }
//            });
////        holder.tv_list_video.setText("Video");
//        holder.tv_list_video.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                deleteData(deteksiItem,position);
//                return true;
//            }
//        });
//        return view;
//    }
//    public void hapusData(final VideoItem deteksiItem, final int position){
//
//        final AlertDialog.Builder dialogHapus = new AlertDialog.Builder(activity.getApplicationContext());
//        dialogHapus.setTitle("Hapus Data");
//        dialogHapus.setMessage("Apakah anda yakin menghapus data ?");
//        dialogHapus.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                deleteData(deteksiItem.getId());
//                .remove(position);
//                notifyDataSetChanged();
//
//            }
//        });
//        dialogHapus.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });
//        dialogHapus.show();
//    }
//    private void deleteData(String idDeteksi){
//        //---Delete Jawaban---
//        String whereClauseJawaban = EntryVideo.COLUMN_ID_DETEKSI+"=?";
//        String[] paramsJawaban = {idDeteksi};
//        MenuActivity.dbSkrianteng.deleteData(EntryVideo.TABLE_NAME,whereClauseJawaban,paramsJawaban);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_list;
        private VideoView mVideoView;
        MediaController mMediaController;
        Context mContext;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_list = itemView.findViewById(R.id.tv_video);
            mVideoView = (VideoView)itemView.findViewById(R.id.vVideo);
            mMediaController = new MediaController(context);

        }
    }
    private void deleteData(String idDeteksi,int no){
        //---Delete video---
        String whereClauseJawaban = EntryVideo.COLUMN_ID_DETEKSI  + "=? AND "+ EntryVideo.COLUMN_NOMOR_SOAL + " =? ";
        String[] paramsJawaban = {idDeteksi, String.valueOf(no)};
        MenuActivity.dbSkrianteng.deleteData(EntryVideo.TABLE_NAME,whereClauseJawaban,paramsJawaban);

//        //---Delete Deteksi---
        String whereClauseDeteksi = EntryVideo.COLUMN_ID+"=?";
        String[] paramsDeteksi = {idDeteksi};
        MenuActivity.dbSkrianteng.deleteData(EntryVideo.TABLE_NAME,whereClauseDeteksi,paramsDeteksi);
    }
}
