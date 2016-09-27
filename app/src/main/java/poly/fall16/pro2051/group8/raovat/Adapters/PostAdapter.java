package poly.fall16.pro2051.group8.raovat.adapters;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.objects.PostObject;
import poly.fall16.pro2051.group8.raovat.R;

/**
 * Created by giang on 9/23/2016.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    ArrayList<PostObject> alPost;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    public PostAdapter(ArrayList<PostObject> arr){
        this.alPost = arr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post_items,parent, false);
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

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PostObject object = alPost.get(position);
        holder.title.setText(object.getTitle());
        holder.price.setText(object.getPrice());
        holder.time.setText(object.getTime());

        imageLoader.displayImage(object.getImage(), holder.image, options, new ImageLoadingListener() {
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
        return alPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, price, time;
        ImageView image;
        CircularProgressView progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            time = (TextView) itemView.findViewById(R.id.time);
            image = (ImageView) itemView.findViewById(R.id.image);
            progressBar = (CircularProgressView) itemView.findViewById(R.id.progressBar);
        }
    }
}
