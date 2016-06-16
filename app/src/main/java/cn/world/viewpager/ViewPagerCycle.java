package cn.world.viewpager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

/**
 * Created by liuhui on 2016/6/15.
 */
public class ViewPagerCycle extends LinearLayout{
    private static final String TAG = "MainActivity";
    private MyImageCyclePageAdapter mPageAdapter;
    private Context mContext;
    private ImageView mRedPoint;
    private ViewPager mViewPager;
    private List<ImageView> mList;
    private LinearLayout mLinearLayout;
    private int previousposition = 0;//前一个被选中的position的位置
    private Handler mHandler;
    private int mPointDis; //两点之间的距离
    private ViewpagerCycleListener mViewpagerCycleListener;

    public ViewPagerCycle(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.viewpagercycle,this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_point);
        mRedPoint = (ImageView) findViewById(R.id.iv_red_point);
    }


    public void setImageResource(List list,ViewpagerCycleListener listener) {
        mList = list;
        mViewpagerCycleListener = listener;
        initView();
    }
    private void initView() {
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    mHandler.sendEmptyMessageDelayed(0, 3000);//发送延时3秒的消息
                }
            };
            mHandler.sendEmptyMessageDelayed(0, 3000);//发送延时3秒的消息
        }

        mRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mPointDis = mLinearLayout.getChildAt(1).getLeft() - mLinearLayout.getChildAt(0).getLeft();
            }
        });
        initData();//初始化数据
        mPageAdapter = new MyImageCyclePageAdapter();
        mViewPager.setAdapter(mPageAdapter);
        //设置默认选中的点和图片
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * @param position             当前位置
             * @param positionOffset       移动偏移百分比
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               /* int leftMargin;
                if (position % mList.size() == 1) {
                    leftMargin = (int) (mPointDis * (position % mList.size() + positionOffset));
                } else {
                    leftMargin = (int) (mPointDis * (position % mList.size() + positionOffset));

                }
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRedPoint.getLayoutParams();
                mRedPoint.setImageResource(R.drawable.shape_point_red);
                params.leftMargin = leftMargin;
                mRedPoint.setLayoutParams(params);*/
            }

            /**
             * 当viewpager被选中时候调用的方法
             *
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                int newposition = position % mList.size();
                int leftMargin = newposition*mPointDis;
                previousposition = newposition;//用完之后把position赋值给previousposition就是上一个viewpageer所在的地方了
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRedPoint.getLayoutParams();
                mRedPoint.setImageResource(R.drawable.shape_point_red);
                params.leftMargin = leftMargin;
                mRedPoint.setLayoutParams(params);
            }

            /**
             * 当viewpage状态改变时候调用的方法
             *
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0://滑动结束

                        break;
                    case 1://手势滑动

                        break;
                    case 2://界面切换

                        break;
                }
            }
        });
        int m = Integer.MAX_VALUE / 2 % mList.size();
        int currentpager = Integer.MAX_VALUE / 2 - m;
        //设置当前显示的条目
        mViewPager.setCurrentItem(currentpager);
    }

    private void initData() {

            int[] images = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e,};
            LinearLayout.LayoutParams params;
            for (int i = 0; i < images.length; i++) {
            //每循环一次要向linearlayout里面添加一个点的view对象
            ImageView grePoint = new ImageView(mContext);
            grePoint.setImageResource(R.drawable.shape_point_gre);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i != 0) {//当前不是第一个点,需要设置边距
                params.leftMargin = 8;
            }
            grePoint.setLayoutParams(params);
                if (mLinearLayout != null){

                    mLinearLayout.addView(grePoint);
                }
        }
    }


    class MyImageCyclePageAdapter extends PagerAdapter {

        /**
         * @return viewpager的长度
         */
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        /**
         * 判断是否使用缓存
         *
         * @param view
         * @param object
         * @return 返回true 表示使用缓存,否则调用instantiateItem()创建一个新的对象
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object; //此处为固定写法
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //下面两句代码作用一样,第一句为原始
//            mViewPager.removeView(mList.get(position % mList.size()));
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = mList.get(position % mList.size());
            iv.setTag("设置TAG");
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewpagerCycleListener.onClick(position,v);
                }
            });
            container.addView(iv);
            return iv;
        }
    }

    private void setmViewpagerCycleListener(ViewpagerCycleListener listener) {
        mViewpagerCycleListener = listener;
    }
    public interface ViewpagerCycleListener {
        /**
         * 每个viewpager点击时候调用的方法
         */
        public void onClick(int position,View imageView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mHandler.sendEmptyMessageDelayed(0, 3000);
        } else if (ev.getAction() ==MotionEvent.ACTION_CANCEL) {
            mHandler.sendEmptyMessageDelayed(0, 3000);
        } else {
            mHandler.removeCallbacksAndMessages(null);
        }
        return super.dispatchTouchEvent(ev);
    }
}
