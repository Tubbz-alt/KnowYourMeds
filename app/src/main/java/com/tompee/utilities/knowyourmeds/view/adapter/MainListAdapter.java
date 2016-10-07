package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.ListSwipeHolder;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.custom.SwipeListItemView;

import java.util.List;

public class MainListAdapter extends ArrayAdapter<Medicine> implements SwipeListItemView.SwipeListItemViewListener {
    private final Context mContext;
    private final boolean mWithIcon;
    private final boolean mSwipeable;
    private final boolean mIsFullLayoutSupported;

    public MainListAdapter(Context context, List<Medicine> medList, boolean isFullLayoutSupported,
                           boolean withIcon, boolean swipeable) {
        super(context, R.layout.list_main, medList);
        mContext = context;
        mIsFullLayoutSupported = isFullLayoutSupported;
        mWithIcon = withIcon;
        mSwipeable = swipeable;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ListSwipeHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_main, parent, false);
            holder = new ListSwipeHolder();
            holder.setFrontView(view.findViewById(R.id.front_view));
            holder.setBackView(view.findViewById(R.id.back_view));
            view.setTag(holder);
        }
        ((SwipeListItemView) view).setEnableSwipeDetection(mSwipeable);
        ((SwipeListItemView) view).setOnClickBackgroundColor(R.color.colorListBackground);

        TextView name = (TextView) view.findViewById(R.id.med_name);
        name.setText(getItem(position).getName());
        ImageView image = (ImageView) view.findViewById(R.id.presc_icon);

        if (mIsFullLayoutSupported) {
            TextView prescText = (TextView) view.findViewById(R.id.presc_text);
            if (getItem(position).isPrescribable()) {
                image.setBackgroundResource(R.drawable.ic_rx_on);
                prescText.setText(mContext.getString(R.string.property_prescribable_yes));
            } else {
                image.setBackgroundResource(R.drawable.ic_rx_off);
                prescText.setText(R.string.property_prescribable_no);
            }

            ImageView ttyView = (ImageView) view.findViewById(R.id.tty_icon);
            TextView ttyText = (TextView) view.findViewById(R.id.tty_text);
            String tty = getItem(position).getTty();
            if (tty != null) {
                switch (tty) {
                    case RxNavWrapper.BRAND:
                        ttyView.setBackgroundResource(R.drawable.shape_tty_brand);
                        ttyText.setText(R.string.property_brands);
                        break;
                    case RxNavWrapper.INGREDIENT:
                        ttyView.setBackgroundResource(R.drawable.shape_tty_ingredients);
                        ttyText.setText(R.string.property_ingredient);
                        break;
                    case RxNavWrapper.SCDC:
                        ttyView.setBackgroundResource(R.drawable.shape_tty_scdc);
                        ttyText.setText(R.string.tab_scdc);
                        break;
                    case RxNavWrapper.SBDC:
                        ttyView.setBackgroundResource(R.drawable.shape_tty_sbdc);
                        ttyText.setText(R.string.tab_sbdc);
                        break;
                    case RxNavWrapper.SBD:
                        ttyView.setBackgroundResource(R.drawable.shape_tty_sbd);
                        ttyText.setText(R.string.tab_sbd);
                        break;
                    case RxNavWrapper.SBDG:
                        ttyView.setBackgroundResource(R.drawable.shape_tty_sbdg);
                        ttyText.setText(R.string.tab_sbdg);
                        break;
                    case RxNavWrapper.SCD:
                        ttyView.setBackgroundResource(R.drawable.shape_tty_scd);
                        ttyText.setText(R.string.tab_scd);
                        break;
                    case RxNavWrapper.SCDG:
                        ttyView.setBackgroundResource(R.drawable.shape_tty_scdg);
                        ttyText.setText(R.string.tab_scdg);
                        break;
                }
            }
        } else {
            if (mWithIcon) {
                if (getItem(position).isPrescribable()) {
                    image.setBackgroundResource(R.drawable.ic_rx_on);
                } else {
                    image.setBackgroundResource(R.drawable.ic_rx_off);
                }
            } else {
                image.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public void onBackViewClicked(int position) {
    }
}
