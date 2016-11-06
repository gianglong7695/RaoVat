package poly.fall16.pro2051.group8.raovat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;

import poly.fall16.pro2051.group8.raovat.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    View v;
    Button btAddView;
    private BubblesManager bubblesManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_message, container, false);
        setViews();
        // configure bublle manager
        initializeBubbleManager();

        btAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNotification();
            }
        });


        return v;
    }


    public void setViews(){
        btAddView = (Button) v.findViewById(R.id.btAddView);
    }

    private void addNewNotification() {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_bubble_view, null);
        // this method call when user remove notification layout
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                Toast.makeText(getApplicationContext(), "Bubble removed !",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // this methoid call when cuser click on the notification layout( bubble layout)
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(getApplicationContext(), "Clicked !",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // add bubble view into bubble manager
        bubblesManager.addBubble(bubbleView, 60, 20);
    }

    /**
     * Configure the trash layout with your BubblesManager builder.
     */
    private void initializeBubbleManager() {
        bubblesManager = new BubblesManager.Builder(getActivity())
                .setTrashLayout(R.layout.notification_trash_layout)
                .build();
        bubblesManager.initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }
}
