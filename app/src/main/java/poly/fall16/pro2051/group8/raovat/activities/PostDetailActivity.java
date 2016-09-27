package poly.fall16.pro2051.group8.raovat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import poly.fall16.pro2051.group8.raovat.R;

public class PostDetailActivity extends AppCompatActivity {
    SliderLayout sliderShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        setViews();

        sliderShow.addSlider(new TextSliderView(this).image("http://taihinhanhdep.xyz/wp-content/uploads/2015/11/anh-dep-cho-dien-thoai-2.jpg"));
        sliderShow.addSlider(new TextSliderView(this).image("http://taihinhanhdep.xyz/wp-content/uploads/2015/11/anh-dep-cho-dien-thoai-7.jpg"));
        sliderShow.addSlider(new TextSliderView(this).image("http://anhdepvetinhyeu.com/wp-content/uploads/mua-thu-tinh-yeu.jpg"));

        sliderShow.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setDuration(4000);

        //sliderShow.setCustomIndicator((PagerIndicator) v.findViewById(R.id.custom_indicator));



    }

    public void setViews(){
        sliderShow = (SliderLayout) findViewById(R.id.slider);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
