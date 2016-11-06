package poly.fall16.pro2051.group8.raovat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.activities.PushingPostActivity;
import poly.fall16.pro2051.group8.raovat.objects.ImageObject;

import static poly.fall16.pro2051.group8.raovat.activities.PushingPostActivity.alBaseImage;

/**
 * Created by giang on 10/15/2016.
 */

public class ImageSelectAdapter extends RecyclerView.Adapter<ImageSelectAdapter.MyViewHolder>{
    ArrayList<ImageObject> alImage;
    Context mContext;

    public ImageSelectAdapter(ArrayList<ImageObject> alImage, Context mContext) {
        this.alImage = alImage;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_select,parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImageObject object = alImage.get(position);
        Bitmap bitmap = null;
        try {
            bitmap = decodeUri(mContext, object.uri, 100 );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        holder.ivImg.setImageBitmap(bitmap);


        holder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alImage.remove(position);
                alBaseImage.remove(position);
                notifyDataSetChanged();
                if(alImage.size() == 0){
                    PushingPostActivity.largeSelect.setVisibility(View.VISIBLE);
                    PushingPostActivity.addPictureView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alImage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImg, ivCancel;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            ivCancel = (ImageView) itemView.findViewById(R.id.ivCancel);
        }
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2.5 < requiredSize || height_tmp / 2.5 < requiredSize)
                break;
            width_tmp /= 2.5;
            height_tmp /= 2.5;
            scale *= 2.5;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
}
