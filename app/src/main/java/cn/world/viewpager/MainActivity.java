package cn.world.viewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPagerCycle vp = (ViewPagerCycle) findViewById(R.id.vp);
        int[] images = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e,};
        ImageView iv;
        mList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            iv = new ImageView(this);
            iv.setBackgroundResource(images[i]);
            mList.add(iv);
        }

        vp.setImageResource(mList, new ViewPagerCycle.ViewpagerCycleListener() {
            @Override
            public void onClick(int position, View imageView) {
                String string = (String) imageView.getTag();
                Toast.makeText(MainActivity.this, string + "位置" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
