package poly.fall16.pro2051.group8.raovat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.objects.ProductDetailObject;

public class ShowImageActivity extends AppCompatActivity {
    SliderLayout sliderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        setViews();


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getWindow().setLayout(width, height/2);

        setData(PostDetailActivity.productDetailObject);
    }


    public void setViews(){
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
    }

    public void  setData(ProductDetailObject object){
        for (int i = 0; i < object.img_url.size(); i++){
            sliderLayout.addSlider(new TextSliderView(getApplicationContext()).image(object.img_url.get(i)));
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        if(object.img_url.size() == 1){
            sliderLayout.stopAutoCycle();
        }
    }
}
