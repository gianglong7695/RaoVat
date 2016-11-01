package poly.fall16.pro2051.group8.raovat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.objects.CategoryObject;

/**
 * Created by giang on 10/13/2016.
 */

public class CategoryAdapter extends BaseAdapter{
    ArrayList<CategoryObject> alCategory;
    Context mContext;
    LayoutInflater mInflater;

    public CategoryAdapter(Context mContext, ArrayList<CategoryObject> alCategory) {
        this.mContext = mContext;
        this.alCategory = alCategory;
    }

    @Override
    public int getCount() {
        return alCategory.size();
    }

    @Override
    public Object getItem(int i) {
        return alCategory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(mInflater == null){
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = mInflater.inflate(R.layout.row_category, null);

        }

        ImageView logo = (ImageView) view.findViewById(R.id.ivLogo);
        TextView name = (TextView) view.findViewById(R.id.tvName);

        CategoryObject object = alCategory.get(i);
        logo.setImageResource(object.getLogo());
        name.setText(object.getName());


        return view;
    }
}
