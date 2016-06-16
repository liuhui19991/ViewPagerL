//package cn.world.viewpager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//import com.xcjy.tianjin.activity.R;
//
//import android.R.integer;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.os.Handler;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//import android.widget.LinearLayout;
//
///**
// * 广告图片自动轮播控件</br>
// *
// */
//public class ImageCycleView extends LinearLayout {
//	/**
//	 * 上下文
//	 */
//	private Context mContext;
//	/**
//	 * 图片轮播视图
//	 */
//	private ViewPager mAdvPager = null;
//	/**
//	 * 滚动图片视图适配
//	 */
//	private ImageCycleAdapter mAdvAdapter;
//	/**
//	 * 图片轮播指示器控件
//	 */
//	private ViewGroup mGroup;
//
//	private static final int count = 1000;
//
//	/**
//	 * 图片轮播指示个图
//	 */
//	private ImageView mImageView = null;
//
//	/**
//	 * 滚动图片指示视图列表
//	 */
//	private ImageView[] mImageViews = null;
//
//	/**
//	 * 图片滚动当前图片下标
//	 */
//
//	private boolean isStop;
//
//	/**
//	 * 游标是圆形还是长条，要是设置为0是长条，要是1就是圆形 默认是圆形
//	 */
//	public int stype = 1;
//
//	/**
//	 * @param context
//	 */
//	public ImageCycleView(Context context) {
//		super(context);
//	}
//
//	int currentNum;
//	DisplayImageOptions options;
//
//	/**
//	 * @param context
//	 * @param attrs
//	 */
//	@SuppressLint("Recycle")
//	public ImageCycleView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		mContext = context;
//		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
//		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
//		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
//		// 滚动图片右下指示器视
//		mGroup = (ViewGroup) findViewById(R.id.viewGroup);
//
//		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.imageloading)
//				.showImageForEmptyUri(R.drawable.imageloading)
//				.showImageOnFail(R.drawable.imageloading).cacheInMemory(true)
//				.cacheOnDisk(true).considerExifParams(true)
//				.displayer(new RoundedBitmapDisplayer(0)).build();
//	}
//
//	/**
//	 * 触摸停止计时器，抬起启动计时器
//	 */
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_UP) {
//			// 开始图片滚动
//			startImageTimerTask();
//		} else {
//			// 停止图片滚动
//			stopImageTimerTask();
//		}
//		return super.dispatchTouchEvent(event);
//	}
//
//	/**
//	 * 装填图片数据
//	 *
//	 * @param imageUrlList
//	 * @param imageCycleViewListener
//	 */
//	public void setImageResources(List<BannerData> imageUrlList,
//			ImageCycleViewListener imageCycleViewListener, int stype) {
//		this.stype = stype;
//		// 清除
//		mGroup.removeAllViews();
//		// 图片数量
//		final int imageCount = imageUrlList.size();
//		mImageViews = new ImageView[imageCount];
//		for (int i = 0; i < imageCount; i++) {
//			mImageView = new ImageView(mContext);
//			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
//					LayoutParams.WRAP_CONTENT);
//			params.leftMargin = 30;
//			mImageView.setScaleType(ScaleType.CENTER_CROP);
//			mImageView.setLayoutParams(params);
//
//			mImageViews[i] = mImageView;
//			if (i == 0) {
//				if (this.stype == 1)
//					mImageViews[i]
//							.setBackgroundResource(R.drawable.banner_dian_focus);
//				else
//					mImageViews[i]
//							.setBackgroundResource(R.drawable.cicle_banner_dian_focus);
//			} else {
//				if (this.stype == 1)
//					mImageViews[i]
//							.setBackgroundResource(R.drawable.banner_dian_blur);
//				else
//					mImageViews[i]
//							.setBackgroundResource(R.drawable.cicle_banner_dian_blur);
//			}
//			mGroup.addView(mImageViews[i]);
//		}
//
//		mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList,
//				imageCycleViewListener);
//		mAdvPager.setAdapter(mAdvAdapter);
//		 int m = count / 2 % imageUrlList.size();
//		 int current = count / 2 - m;
//		mAdvPager.setCurrentItem(current);
////		mAdvPager.setCurrentItem(0);
//		startImageTimerTask();
//	}
//
//	/**
//	 * 图片轮播(手动控制自动轮播与否，便于资源控件）
//	 */
//	public void startImageCycle() {
//		startImageTimerTask();
//	}
//
//	/**
//	 * 暂停轮播—用于节省资源
//	 */
//	public void pushImageCycle() {
//		stopImageTimerTask();
//	}
//
//	/**
//	 * 图片滚动任务
//	 */
//	private void startImageTimerTask() {
//		stopImageTimerTask();
//		// 图片滚动
//		mHandler.postDelayed(mImageTimerTask, 3000);
//	}
//
//	/**
//	 * 停止图片滚动任务
//	 */
//	private void stopImageTimerTask() {
//		isStop = true;
//		mHandler.removeCallbacks(mImageTimerTask);
//	}
//
//	private Handler mHandler = new Handler();
//
//	/**
//	 * 图片自动轮播Task
//	 */
//	private Runnable mImageTimerTask = new Runnable() {
//		@Override
//		public void run() {
//			mAdvPager.setCurrentItem(mAdvPager.getCurrentItem() + 1);
//
//			if (!isStop) { // if isStop=true //当你退出后 要把这个给停下来 不然 这个一直存在
//				// 就一直在后台循环
//				mHandler.postDelayed(mImageTimerTask, 3000);
//			}
//		}
//	};
//
//	/**
//	 * 轮播图片监听
//	 *
//	 * @author minking
//	 */
//	private final class GuidePageChangeListener implements OnPageChangeListener {
//
//		boolean isScrolled = false;
//
//		@Override
//		public void onPageScrollStateChanged(int state) {
//			if (state == ViewPager.SCROLL_STATE_IDLE)
//				startImageTimerTask();
//
//			switch (state) {
//			case 1:// 手势滑动
//				isScrolled = false;
//				break;
//			case 2:// 界面切换
//				isScrolled = true;
//				break;
//			case 0:// 滑动结束
//
//				/*// 当前为最后一张，此时从右向左滑，则切换到第一张
//				if (mAdvPager.getCurrentItem() == mAdvPager.getAdapter()
//						.getCount() - 1 && !isScrolled) {
//					mAdvPager.setCurrentItem(0);
//				}
//				// 当前为第一张，此时从左向右滑，则切换到最后一张
//				else if (mAdvPager.getCurrentItem() == 0 && !isScrolled) {
//					mAdvPager
//							.setCurrentItem(mAdvPager.getAdapter().getCount() - 1);
//				}*/
//				break;
//			}
//
//		}
//
//		 @Override
//         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//         }
//
//		@Override
//		public void onPageSelected(int index) {
//
//			index = index % mImageViews.length;
//			// 设置当前显示的图片
//			// 设置图片滚动指示器背
//			if (stype == 1)
//				mImageViews[index]
//						.setBackgroundResource(R.drawable.banner_dian_focus);
//			else
//				mImageViews[index]
//						.setBackgroundResource(R.drawable.cicle_banner_dian_focus);
//			for (int i = 0; i < mImageViews.length; i++) {
//				if (index != i) {
//					if (stype == 1)
//						mImageViews[i]
//								.setBackgroundResource(R.drawable.banner_dian_blur);
//					else
//						mImageViews[i]
//								.setBackgroundResource(R.drawable.cicle_banner_dian_blur);
//				}
//			}
//		}
//	}
//
//	private class ImageCycleAdapter extends PagerAdapter {
//
//
//		/**
//		 * 图片资源列表
//		 */
//		private List<BannerData> mAdList = new ArrayList<BannerData>();
//
//		/**
//		 * 广告图片点击监听
//		 */
//		private ImageCycleViewListener mImageCycleViewListener;
//
//		private Context mContext;
//
//		public ImageCycleAdapter(Context context, List<BannerData> adList,
//				ImageCycleViewListener imageCycleViewListener) {
//			this.mContext = context;
//			this.mAdList = adList;
//			mImageCycleViewListener = imageCycleViewListener;
//		}
//
//		@Override
//		public int getCount() {
//			return count;
//		}
//
//		@Override
//		public boolean isViewFromObject(View view, Object obj) {
//			return view == obj;
//		}
//
//		@Override
//		public Object instantiateItem(ViewGroup container, final int position) {
//			String imageUrl = mAdList.get(position % mAdList.size())
//					.getBanner();
//			ImageView imageView  = new ImageView(mContext);
//				imageView.setLayoutParams(new LayoutParams(
//						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//
//				// test
//				imageView.setScaleType(ScaleType.FIT_XY);
//				imageView.setTag(mAdList.get(position % mAdList.size()));//414注释
//				// 设置图片点击监听
//				imageView.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						mImageCycleViewListener.onImageClick(
//								position % mAdList.size(), v);
//					}
//				});
//
//			imageView.setTag(mAdList.get(position % mAdList.size()).url);
//
//
//			container.addView(imageView);
//
//			ImageLoader.getInstance()
//					.displayImage(imageUrl, imageView, options);
//			return imageView;
//		}
//
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			mAdvPager.removeView((ImageView) object);
//		}
//
//	}
//
//	/**
//	 * 轮播控件的监听事件
//	 *
//	 * @author minking
//	 */
//	public static interface ImageCycleViewListener {
//
//		/**
//		 * 单击图片事件
//		 *
//		 * @param position
//		 * @param imageView
//		 */
//		public void onImageClick(int position, View imageView);
//	}
//
//}
