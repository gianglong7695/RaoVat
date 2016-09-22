package poly.fall16.pro2051.group8.raovat.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.Activities.PostListActivity;
import poly.fall16.pro2051.group8.raovat.Adapters.CategoryAdapter;
import poly.fall16.pro2051.group8.raovat.Objects.CategoryObject;
import poly.fall16.pro2051.group8.raovat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    GridView gvCategory;
    View v;
    ArrayList<CategoryObject> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_category, container, false);
        setViews();
        list = new ArrayList<>();
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));
        list.add(new CategoryObject("Xe cộ", R.drawable.img_xeco));

        CategoryAdapter adapter = new CategoryAdapter(getActivity(), list);
        gvCategory.setAdapter(adapter);


        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        {
                            Intent it = new Intent(getActivity(), PostListActivity.class);
                            startActivity(it);
                            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                        }
                        break;

                }
            }
        });



        return v;
    }

    private void setViews(){
        gvCategory = (GridView) v.findViewById(R.id.gvCategory);
    }

}
