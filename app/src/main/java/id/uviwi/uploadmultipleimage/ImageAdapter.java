package id.uviwi.uploadmultipleimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private ArrayList<String> list;
    private Context context;

    public ImageAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gambar, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        File f = new File(list.get(position));
        String filename = list.get(position).substring(list.get(position).lastIndexOf("/"));
        holder.tvFileName.setText(filename);
        Bitmap d = new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();
        Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(512, d);
        holder.iv.setImageBitmap(scaled);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        private TextView tvFileName;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tvFileName = itemView.findViewById(R.id.tv_text);
        }
    }
}