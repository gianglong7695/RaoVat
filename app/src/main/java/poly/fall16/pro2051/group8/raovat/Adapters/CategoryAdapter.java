package poly.fall16.pro2051.group8.raovat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.Objects.CategoryObject;
import poly.fall16.pro2051.group8.raovat.R;

/**
 * Created by giang on 9/20/2016.
 */

public class CategoryAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    ArrayList<CategoryObject> list;
    Context context;

    public CategoryAdapter(Context context, ArrayList<CategoryObject> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(mInflater == null){
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = mInflater.inflate(R.layout.custom_row_category_item, null);

        }
        ImageView background = (ImageView) view.findViewById(R.id.ivBG);
        TextView title = (TextView) view.findViewById(R.id.tvTitle);

        CategoryObject object = list.get(i);
        background.setImageResource(object.getBackground());
        title.setText(object.getTitle());


        return view;
    }
}
