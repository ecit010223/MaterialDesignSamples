package com.zhouwei.md.materialdesignsamples.behavoir;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhouwei.md.materialdesignsamples.Constant;
import com.zhouwei.md.materialdesignsamples.MaterialDesignSimpleApplication;
import com.zhouwei.md.materialdesignsamples.R;

/**
 * Created by zhouwei on 16/12/16.
 */

public class FloatingHeaderTitleBehavior extends CoordinatorLayout.Behavior<View> {
    private ArgbEvaluator mArgbEvaluator;
    /**
     * Title 的折叠高度
     */
    private int mTitleCollapsedHeight;
    /**
     * titile 初始化Y轴的位置
     */
    private int mTitleInitY;

    private int mMargin;

    public FloatingHeaderTitleBehavior(Context context, AttributeSet attributeSet) {
        mArgbEvaluator = new ArgbEvaluator();
        mTitleCollapsedHeight = context.getResources().getDimensionPixelOffset(R.dimen.collapsedTitleHeight);
        mTitleInitY = context.getResources().getDimensionPixelOffset(R.dimen.title_init_y);
        mMargin = context.getResources().getDimensionPixelOffset(R.dimen.title_margin_left);
    }

    /**
     * 生成Behavior后第一件事就是确定依赖关系,重写Behavior的这个方法来确定你依赖哪些View。
     * 确定你是否依赖于这个View，CoordinatorLayout会将自己所有View遍历判断，
     * 如果确定依赖，这个方法很重要，当所依赖的View变动时会回调这个方法。
     * @param parent
     * @param child  指应用behavior的View
     * @param dependency  担任触发behavior的View，并与child进行互动
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        Log.d(Constant.TAG,"layoutDependsOn");
        return isDependent(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //触发behavior的控件的移动距离
        float dependencyTranslationY = dependency.getTranslationY();
        //触发behavior的控件的高度：400
        int dependencyHeight = dependency.getHeight();
        //收缩后的高度:100
        int collapsedHeight = getCollapsedHeight();

        // 1 - 触发behavior的控件的移动距离与可移动距离的百分比
        float progress = 1.0f - Math.abs( dependencyTranslationY/ (dependencyHeight - collapsedHeight));

        float translationY = (mTitleInitY - mTitleCollapsedHeight) * progress;

        Log.d(Constant.TAG,"dependencyTranslationY:"+dependencyTranslationY+",dependencyHeight:"+dependencyHeight+
                ",collapsedHeight:"+collapsedHeight+",progress"+progress+",translationY:"+translationY);
        //应用behavior的View的在Y轴方向上移动的距离
        child.setTranslationY(translationY);

        // background change
        int startColor = MaterialDesignSimpleApplication.getAppContext().getResources().getColor(R.color.init_bg_color);
        int endColor = MaterialDesignSimpleApplication.getAppContext().getResources().getColor(R.color.end_bg_color);
        child.setBackgroundColor((Integer) mArgbEvaluator.evaluate(progress, endColor, startColor));
        //set margin
        int margin = (int) (mMargin * progress);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        params.setMargins(margin, 0, margin, 0);
        child.setLayoutParams(params);
        return true;
    }

    private boolean isDependent(View dependency) {

        return dependency != null && dependency.getId() == R.id.header_view;
    }

    private int getCollapsedHeight() {
        return MaterialDesignSimpleApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.collapsedTitleHeight);
    }
}
