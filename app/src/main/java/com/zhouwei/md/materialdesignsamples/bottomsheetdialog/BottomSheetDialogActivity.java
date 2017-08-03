package com.zhouwei.md.materialdesignsamples.bottomsheetdialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhouwei.md.materialdesignsamples.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 16/12/2.
 */

public class BottomSheetDialogActivity extends AppCompatActivity implements View.OnClickListener {
    private BottomSheetDialog mBottomSheetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_dialog_layout);

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.bottomsheetdialog);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.inflateMenu(R.menu.menu_share);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.item_share) {
                    //弹出shareDialog
                    showShareDialog();
                }
                return false;
            }
        });

        findViewById(R.id.show_bottom).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.show_bottom) {
            showBottomSheetDialog();
        }
    }

    /**
     * share Dialog
     */
    private void showShareDialog() {
        if (mBottomSheetDialog == null) {
            Log.d("BottomSheet", "showShareDialog null");
            mBottomSheetDialog = new BottomSheetDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_share_dialog, null);
            mBottomSheetDialog.setContentView(view);
            mBottomSheetDialog.setCancelable(true);
            mBottomSheetDialog.setCanceledOnTouchOutside(true);
            mBottomSheetDialog.show();
            /*********** 解决下滑隐藏dialog后，再次调用show方法显示时，不能弹出Dialog ***********/
            /**
             * getDelegate()返回AppCompatDelegate，
             * 以往使用AppCompat的唯一入口就是ActionBarActivity类，迫使你使用固定死的一套Activity结构，
             * 使得像PreferenceActivity这样的类无法利用AppCompat的特性。
             * 现在将ActionBarActivity里面的内部逻辑都抽离出来，然后封装成一个单一的代理(delegate)api供调用:AppCompatDelegate。
             * AppCompatDelegate可以被任意带有Window.Callback的安卓对象创建，比如Activity或者Dialog的子类。
             * 你可以通过他的静态方法create()创建:AppCompatDelegate.create(this, null);
             */
            View view1 = mBottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
            /**
             * design_bottom_sheet对应的控件包含了一个app:layout_behavior属性，该属性的值为@string/bottom_sheet_behavior
             * 通过如下方法可获得该属性对应的BottomSheetBehavior。
             * 注意app:layout_behavior属性要作为CoordinatorLayout的子View的LayoutParams，所以CoordinatorLayout是万万不能少的。
             */
            final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view1);
            /**
             * 系统的BottomSheetDialog是基于BottomSheetBehavior封装的,当我们滑动隐藏了BottomSheetBehavior中的View后，
             * 内部是设置了BottomSheetBehavior的状态为STATE_HIDDEN，接着它替我们关闭了Dialog，所以我们再次调用
             * dialog.show()的时候Dialog没法再此打开状态为HIDE的dialog了。
             * 为解决如上问题，在监听到用户滑动关闭BottomSheetDialog后，我们把BottomSheetBehavior的状态设置为
             * BottomSheetBehavior.STATE_COLLAPSED，也就是半个打开状态(BottomSheetBehavior.STATE_EXPANDED为全打开)。
             */
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        Log.d("BottomSheet", "onStateChanged");
                        mBottomSheetDialog.dismiss();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        } else {
            Log.d("BottomSheet", "showShareDialog show");
            mBottomSheetDialog.show();
        }

    }

    private void showBottomSheetDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null);

        handleList(view);

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void handleList(View contentView) {
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        MusicAdapter adapter = new MusicAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setData(mockData());
        adapter.notifyDataSetChanged();
    }

    /**
     * 模拟数据
     *
     * @return
     */
    private List<MusicInfo> mockData() {
        List<MusicInfo> musicInfos = new ArrayList<>();
        MusicInfo musicInfo = new MusicInfo("哪里都是你", "周杰伦");
        musicInfos.add(musicInfo);
        MusicInfo musicInfo1 = new MusicInfo("阳光宅男", "周杰伦");
        musicInfos.add(musicInfo1);
        MusicInfo musicInfo2 = new MusicInfo("可爱女人", "周杰伦");
        musicInfos.add(musicInfo2);
        MusicInfo musicInfo3 = new MusicInfo("火车叨位去", "周杰伦");
        musicInfos.add(musicInfo3);
        MusicInfo musicInfo4 = new MusicInfo("我的地盘", "周杰伦");
        musicInfos.add(musicInfo4);
        MusicInfo musicInfo5 = new MusicInfo("枫", "周杰伦");
        musicInfos.add(musicInfo5);
        MusicInfo musicInfo6 = new MusicInfo("搁浅", "周杰伦");
        musicInfos.add(musicInfo6);
        MusicInfo musicInfo7 = new MusicInfo("一路向北", "周杰伦");
        musicInfos.add(musicInfo7);
        MusicInfo musicInfo8 = new MusicInfo("半岛铁盒", "周杰伦");
        musicInfos.add(musicInfo8);
        MusicInfo musicInfo9 = new MusicInfo("霍元甲", "周杰伦");
        musicInfos.add(musicInfo9);

        return musicInfos;
    }


    public static class MusicAdapter extends RecyclerView.Adapter {
        private List<MusicInfo> mData;

        public void setData(List<MusicInfo> data) {
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_music_item, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MusicViewHolder musicViewHolder = (MusicViewHolder) holder;

            MusicInfo musicInfo = mData.get(position);

            musicViewHolder.name.setText(musicInfo.name);

            musicViewHolder.singer.setText(musicInfo.singer);
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public static class MusicViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView singer;

            public MusicViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.music_name);
                singer = (TextView) itemView.findViewById(R.id.music_singer);
            }
        }
    }
}
