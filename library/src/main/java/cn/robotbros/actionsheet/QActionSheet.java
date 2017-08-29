package cn.robotbros.actionsheet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by enix on 29/8/2017.
 */

public class QActionSheet extends Dialog {
    private final String TAG = "QActionSheet";
    private RelativeLayout mParent;
    private View mMaskView;
    private LinearLayout mPanel;
    private ArrayList<String> items;
    private final int ANIMATION_DURATION = 300;
    private String title = null;
    private int initialSelection = 0;
    private OnItemSelectListener onItemSelectListener;

    public interface OnItemSelectListener {
        void onItemSelected(int index, String item);
    }

    public QActionSheet(QActionSheet.Builder builder) {
        super(builder.context, android.R.style.Theme_Light_NoTitleBar);

        Drawable drawable = new ColorDrawable();
        drawable.setAlpha(0);
        getWindow().setBackgroundDrawable(drawable);

        this.items = builder.items;
        this.initialSelection = builder.initialSelection;
        this.title = builder.title;
        this.onItemSelectListener = builder.listener;

        initView();
        initItemViews();
    }

    public static class Builder {
        private Context context;
        private ArrayList<String> items;
        private int initialSelection;
        private String title;
        private OnItemSelectListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder data(ArrayList<String> items) {
            this.items = items;
            return this;
        }

        public Builder initialSelection(int index) {
            initialSelection = index;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder onItemSelectListener(OnItemSelectListener listener) {
            this.listener = listener;
            return this;
        }

        public QActionSheet build() {
            return new QActionSheet(this);
        }
    }

    public void initView() {
        mParent = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams parentParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mParent.setLayoutParams(parentParam);

        mMaskView = new View(getContext());
        RelativeLayout.LayoutParams maskParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMaskView.setLayoutParams(maskParam);
        mMaskView.setBackgroundColor(Color.argb(125, 0, 0, 0));
        mMaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mPanel = new LinearLayout(getContext());
        RelativeLayout.LayoutParams panelParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        panelParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        panelParam.setMargins(dp2px(5), dp2px(0), dp2px(5), dp2px(10));
        mPanel.setOrientation(LinearLayout.VERTICAL);
        mPanel.setLayoutParams(panelParam);
        mPanel.setBackgroundColor(Color.argb(255, 248, 248, 248));

        mParent.addView(mMaskView);
        mParent.addView(mPanel);
    }

    public void initItemViews() {
        if (items == null) {
            Log.e(TAG, "Please call setItems before calling build");
            return;
        }

        if (title != null) {
            TextView titleView = new TextView(getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.bottomMargin = dp2px(1);
            param.gravity = Gravity.CENTER;

            titleView.setPadding(dp2px(0), dp2px(10), dp2px(0), dp2px(10));
            titleView.setGravity(Gravity.CENTER);
            titleView.setBackgroundColor(getColor(android.R.color.white));
            titleView.setLayoutParams(param);
            titleView.setText(title);
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            mPanel.addView(titleView);
        }

        for (int i = 0; i < items.size(); i ++) {
            final String item = items.get(i);
            final int index = i;
            Button btn = new Button(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.bottomMargin = dp2px(1);

            btn.setPadding(dp2px(0), dp2px(15), dp2px(0), dp2px(15));
            btn.setText(item);
            btn.setLayoutParams(params);
            btn.setBackgroundColor(getColor(android.R.color.white));

            if (i == initialSelection) {
                btn.setTextColor(getColor(android.R.color.black));
            } else {
                btn.setTextColor(getColor(android.R.color.darker_gray));
            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemSelectListener != null) {
                        onItemSelectListener.onItemSelected(index, item);
                    }

                    dismiss();
                }
            });
            mPanel.addView(btn);
        }
    }

    @Override
    public void show() {
        super.show();
        getWindow().setContentView(mParent);

        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(ANIMATION_DURATION);
        mMaskView.setAnimation(anim);

        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation anim2 = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
        anim2.setDuration(ANIMATION_DURATION);
        mPanel.setAnimation(anim2);
    }

    @Override
    public void dismiss() {
        AlphaAnimation anim = new AlphaAnimation(1, 0);
        anim.setDuration(ANIMATION_DURATION);
        mMaskView.startAnimation(anim);

        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation anim2 = new TranslateAnimation(type, 0, type, 0, type, 0, type, 1);
        anim2.setDuration(ANIMATION_DURATION);
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                QActionSheet.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mPanel.startAnimation(anim2);
    }

    private int dp2px(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources()
                .getDisplayMetrics());
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public QActionSheet setItems(ArrayList<String> items) {
        this.items = items;
        return this;
    }

    public OnItemSelectListener getOnItemSelectListener() {
        return onItemSelectListener;
    }

    public QActionSheet setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public QActionSheet setTitle(String title) {
        this.title = title;
        return this;
    }

    public QActionSheet setInitialSelection(int initialSelection) {
        this.initialSelection = initialSelection;
        return this;
    }

    private int getColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().getResources().getColor(colorResId, getContext().getTheme());
        } else {
            return getContext().getResources().getColor(colorResId);
        }
    }
}

