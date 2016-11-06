package poly.fall16.pro2051.group8.raovat.adapters;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.objects.ProductObject;

import static poly.fall16.pro2051.group8.raovat.utils.MyString.handingPrice;

/**
 * Created by Giang Long on 11/2/2016.
 */

public class ProposeAdapter extends RecyclerView.Adapter<ProposeAdapter.MyViewHolder>{
    ArrayList<ProductObject> alProduct;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public ProposeAdapter(ArrayList<ProductObject> alProduct) {
        this.alProduct = alProduct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_propose_item,parent, false);
        // ImageLoader
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(parent.getContext()));

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.photo_default)
                .showImageOnFail(R.drawable.ic_image_fail_to_loading)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductObject object = alProduct.get(position);
        holder.tvtitle.setText(object.name);
        holder.tvCurrentPrice.setText(handingPrice(object.price));
        holder.tvUserName.setText(object.username);


        imageLoader.displayImage(object.image_url, holder.ivImg, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return alProduct.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImg, ivUserAvatar;
        TextView tvtitle, tvCurrentPrice, tvUserName;
        RelativeLayout discount;
        ProgressBar progressBar;
        public MyViewHolder(View itemView) {
            super(itemView);

            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            ivUserAvatar = (ImageView) itemView.findViewById(R.id.ivUserAvatar);
            tvtitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvCurrentPrice = (TextView) itemView.findViewById(R.id.tvCurrentPrice);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            discount = (RelativeLayout) itemView.findViewById(R.id.discount);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

        }
    }
}
