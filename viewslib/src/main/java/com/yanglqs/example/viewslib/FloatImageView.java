package com.yanglqs.example.viewslib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;



/**
 * 可拖动并自动吸附ImageView
 * Created by hasee-pc on 2017/1/11.
 */

public class FloatImageView extends ImageView {
    private int parentWidth;
    private int parentHeigth;
    private int lastX, lastY; // 记录移动的最后的位置
    private int btnHeight, btnWidth;
    private int downx, downy; //记录点下去的位置
    private long downTime;
    private long upTime;
    private int upX, upY;
    private OnSingleTouchListener singleTouchListener;
    private RelativeLayout.LayoutParams lp;
    private View v;
    private ScaleAnimation enterAnim;
    private boolean hasStartFreshWelfareAnimation;
    private boolean hasInitFreshWelfare;
    private int parentLeftPadding;
    private int parentRightPadding;
    private int parentTopPadding;
    private int parentBottomPadding;

    public FloatImageView(Context context) {
        super(context);
    }

    public FloatImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        v = this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        btnHeight = v.getMeasuredHeight();
        btnWidth = v.getMeasuredWidth();
//        Log.d("float-onMeasure","btnWidth:"+btnWidth+" btnHeight:"+btnHeight);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (v == null) {
            v = this;
        }

        // 获取Action
        int ea = event.getAction();
        switch (ea) {
            case MotionEvent.ACTION_DOWN: // 按下
                downTime = System.currentTimeMillis();
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                downx = (int) event.getRawX();
                downy = (int) event.getRawY();
//                Logger.e("downX==" + lastX);
//                Logger.e("downY==" + lastY);
                parentLeftPadding = ((ViewGroup)v.getParent()).getPaddingLeft();
                parentRightPadding = ((ViewGroup)v.getParent()).getPaddingRight();
                parentTopPadding = ((ViewGroup)v.getParent()).getPaddingTop();
                parentBottomPadding = ((ViewGroup)v.getParent()).getPaddingBottom();


                /*
                 * 这里获取的宽高可能会因为父容器的布局参数产生偏差，
                 * 如果发现有误，最好在父容器布局完成后，
                 * 再用setViewHeight()，setViewHeight()方法传入真实的宽高
                 *
                 */
                if (parentWidth == 0) {
                    parentWidth = ((ViewGroup)v.getParent()).getWidth()-(parentLeftPadding+parentRightPadding);

                }

                if (parentHeigth == 0) {
                    parentHeigth = ((ViewGroup)v.getParent()).getHeight()-(parentTopPadding+parentBottomPadding);
                }

                break;
            case MotionEvent.ACTION_MOVE: // 移动
                // 移动中动态设置位置
//                Logger.e("parentHeigth=="+ parentHeigth);
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                int left = v.getLeft() + dx;
                int top = v.getTop() + dy;
                int right = v.getRight() + dx;
                int bottom = v.getBottom() + dy;
                if (left < parentLeftPadding) {
                    left = parentLeftPadding;
                    right = left + btnWidth;
                }
                if (right > parentWidth + parentLeftPadding) {
                    right = parentWidth + parentLeftPadding;
                    left = right - btnWidth;
                }
                if (top < parentTopPadding) {
                    top = parentTopPadding;
                    bottom = top + btnHeight;
                }
                if (bottom > parentHeigth + parentTopPadding) {
                    bottom = parentHeigth + parentTopPadding;
                    top = bottom - btnHeight;
                }
                v.layout(left, top, right, bottom);
//                Log.d("float-move","left:"+left+"top:"+top);
                /*
                 *这里使用setLayoutMargins会出问题，控件自动移动，原因不明
                 */
//                setLayoutMargins(left,top,0,0);
                // 将当前的位置再次设置
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
//                Logger.e("moveLastx=="+ lastX);
//                Logger.e("moveLasty=="+ lastY);

                break;
            case MotionEvent.ACTION_UP: // 抬起
                upTime = System.currentTimeMillis();
                upX = (int) event.getRawX();
                upY = (int) event.getRawY();
//                        // 向四周吸附
                int dx1 = upX - lastX;
                int dy1 = upY - lastY;
                int left1 = v.getLeft() + dx1;
                int top1 = v.getTop() + dy1;
                int right1 = v.getRight() + dx1;
                int bottom1 = v.getBottom() + dy1;
                if (top1 < 100) {  //向上吸附
//                        v.layout(left1, 0, right1, btnHeight);
                    setLayoutMargins(left1 - parentLeftPadding, 0, 0, 0);
                } else if (bottom1 > (parentHeigth - 200)) {   //向下吸附
//                        v.layout(left1, (parentHeigth - btnHeight), right1, parentHeigth);
                    setLayoutMargins(left1 - parentLeftPadding, (parentHeigth - btnHeight), 0, 0);
                } else {
                    if (left1 < (parentWidth / 2)) {  //在父容器的左半边
                        //向左吸附
//                        v.layout(0, top1, btnWidth, bottom1);
                        setLayoutMargins(0, top1, 0, 0);
                    } else {//在父容器的右半边
                        //向右吸附
//                        v.layout((parentWidth - btnWidth), top1, parentWidth, bottom1);
                        setLayoutMargins((parentWidth - btnWidth), top1, 0, 0);
                    }
                }
//                Log.d("float","parentWidth:"+ parentWidth +"parentHeigth:"+ parentHeigth);
//                Log.d("float","btnWidth:"+btnWidth+" btnHeight:"+btnHeight);
                //判断是否是点击
                int MoveX = Math.abs(upX - downx);
                int MoveY = Math.abs(upY - downy);
                if ((upTime - downTime) < 200 && (MoveX < 20 || MoveY < 20)) {
                    if (singleTouchListener != null) {
                        singleTouchListener.onSingleTouch(v);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置控件的Margins值，
     * 这样可以永久保存控件的位置，防止页面内其他控件渲染引起父容器重新布局而导致的回到原位的影响
     */
    private void setLayoutMargins(int left, int top, int right, int bottom) {
        if (lp == null) {
            lp = new RelativeLayout.LayoutParams(btnWidth, btnHeight);
        }
        lp.setMargins(left, top, right, bottom);
        setLayoutParams(lp);
    }

    /**
     * 开启呼吸动画
     */
    public void startFreshWelfare() {
        if (!hasInitFreshWelfare){
            initFreshWelfare();
        }

        if (!hasStartFreshWelfareAnimation) {
            if (v == null) {
                v = this;
            }
            v.startAnimation(enterAnim);
            hasStartFreshWelfareAnimation = true;
        }
    }

    /**
     * 关闭呼吸动画
     */
    public void stopFreshWelfare() {
        if (v == null) {
            v = this;
        }
        v.clearAnimation();
        hasStartFreshWelfareAnimation = false;
    }

    /**
     * 初始化呼吸效果触摸事件和动画
     */
    public void initFreshWelfare() {
        if (v == null) {
            v = this;
        }
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 触摸时取消动画，并缩小，有按下的感觉
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    v.setScaleX(0.8f);
                    v.setScaleY(0.8f);
                    if (v.getAnimation() != null) {
                        v.getAnimation().cancel();
                    }
                } else { // 松手后，恢复大小，并继续呼吸效果
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);
                    freshWelfareAni();
                }

                return false;
            }
        });
        // 放大小时view，完全显示后开始呼吸效果
        if (enterAnim == null) {
            enterAnim = new ScaleAnimation(0f, 0.9f, 0f, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        enterAnim.setDuration(1000);
        // enterAnim.setInterpolator(new AccelerateInterpolator(2f));
        enterAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                freshWelfareAni();
            }
        });
        hasInitFreshWelfare = true;
    }

    private void freshWelfareAni() {
        if (v == null) {
            v = this;
        }
        ScaleAnimation anim = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(850);
        anim.setRepeatMode(Animation.REVERSE); //重复模式 反转
        anim.setRepeatCount(Animation.INFINITE); // 无限循环
        v.setAnimation(anim);
        v.startAnimation(v.getAnimation());
    }


    public void setOnSingleTouch(OnSingleTouchListener listener) {
        this.singleTouchListener = listener;
    }


    public interface OnSingleTouchListener {

        void onSingleTouch(View v);
    }

}


