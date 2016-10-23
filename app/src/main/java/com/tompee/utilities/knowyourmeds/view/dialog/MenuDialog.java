package com.tompee.utilities.knowyourmeds.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.custom.TextDrawable;

import java.util.ArrayList;
import java.util.List;

public class MenuDialog extends Dialog implements View.OnClickListener,
        DialogInterface.OnShowListener, Animator.AnimatorListener {
    private final MenuDialogListener mListener;
    private List<View> mMenuList;
    private FloatingActionButton mExit;

    public MenuDialog(Context context, Medicine medicine, MenuDialogListener listener) {
        super(context, android.R.style.Theme_Wallpaper_NoTitleBar);
        mListener = listener;
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_menu, null);
        mExit = (FloatingActionButton) view.findViewById(R.id.exit);
        mExit.setOnClickListener(this);
        mExit.setImageDrawable(new TextDrawable(getContext().getResources(), "x",
                ContextCompat.getColor(getContext(), R.color.dark_text)));
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDarkAlpha);
        setContentView(view);
        setCancelable(false);
        setOnShowListener(this);

        mMenuList = new ArrayList<>();
        mMenuList.add(createMenuView(R.string.tab_properties, R.color.properties));
        if (medicine.getBrands() != null && !medicine.getBrands().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_brands, R.color.brand));
        }
        if (medicine.getSbdc() != null && !medicine.getSbdc().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_sbdc, R.color.sbdc));
        }
        if (medicine.getSbd() != null && !medicine.getSbd().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_sbd, R.color.sbd));
        }
        if (medicine.getSbdg() != null && !medicine.getSbdg().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_sbdg, R.color.sbdg));
        }
        if (medicine.getScdc() != null && !medicine.getScdc().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_scdc, R.color.scdc));
        }
        if (medicine.getScd() != null && !medicine.getScdc().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_scd, R.color.scd));
        }
        if (medicine.getScdg() != null && !medicine.getScdg().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_scdg, R.color.scdg));
        }
        if (medicine.getUrl() != null) {
            mMenuList.add(createMenuView(R.string.tab_info, R.color.info));
        }
        if (medicine.getInteractions() != null && !medicine.getInteractions().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_interaction, R.color.interaction));
        }
        if (medicine.getSources() != null && !medicine.getSources().isEmpty()) {
            mMenuList.add(createMenuView(R.string.tab_sources, R.color.sources));
        }

        LinearLayout container = (LinearLayout) view.findViewById(R.id.container);
        for (View child : mMenuList) {
            container.addView(child);
        }
    }

    private View createMenuView(int resourceId, int colorId) {
        View menuView = LayoutInflater.from(getContext()).inflate(R.layout.view_menu, null);
        menuView.setVisibility(View.INVISIBLE);
        TextView textView = (TextView) menuView.findViewById(R.id.text);
        textView.setText(resourceId);
        FloatingActionButton button = (FloatingActionButton) menuView.findViewById(R.id.button);
        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.
                getColor(getContext(), colorId)));
        button.setOnClickListener(this);
        menuView.setTag(resourceId);
        return menuView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exit) {
            dismiss();
            return;
        }
        for (View view : mMenuList) {
            if (view.findViewById(R.id.button).equals(v)) {
                mListener.onMenuClicked(getContext().getString((int) view.getTag()));
                dismiss();
                break;
            }
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        animateViewEntry();
        YoYo.with(Techniques.RotateInDownLeft).playOn(mExit);
    }

    private void animateViewEntry() {
        for (View view : mMenuList) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInUp).duration(100).withListener(this).playOn(view);
                break;
            }
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        animateViewEntry();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public interface MenuDialogListener {
        void onMenuClicked(String menuString);
    }
}
