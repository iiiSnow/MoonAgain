package com.zrx.moonagain.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zrx.moonagain.R;
import com.zrx.moonagain.StarBaseAcitivity;
import com.zrx.moonagain.dto.ADModel;
import com.zrx.moonagain.dto.BaseModel;
import com.zrx.moonagain.interfaces.CustomApiCallback;
import com.zrx.moonagain.interfaces.IMoonService;
import com.zrx.moonagain.utils.IntentUtils;
import com.zrx.snowlibrary.utils.ClickUtil;
import com.zrx.snowlibrary.utils.ImageDisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.RestAdapter;
import retrofit.client.Response;

/**
 * Created by Schnee on 2017/2/20.
 */

public class SplashActivity extends StarBaseAcitivity {

    @BindView(R.id.tv_countdown)
    TextView tvCountdown;
    @BindView(R.id.iv_ad)
    ImageView ivAd;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what != 0) {
                tvCountdown.setText(what + "s");
                sendEmptyMessageDelayed(what - 1, 1000);
            } else {
                jumpToNext();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_splash);
        ButterKnife.bind(this);

        handler.sendEmptyMessageDelayed(3, 1000);

        getADUrl();
        tvCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtil.isNotFastClick()) {
                    handler.removeCallbacksAndMessages(null);
                    jumpToNext();
                }
            }
        });

    }

    private void jumpToNext() {
        startActivity(IntentUtils.toMainActivity(SplashActivity.this));
        finish();
    }

    public void getADUrl() {
        String baseApi = "http://bdapp2.bandao.cn/bandao/api_4_0_0";

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(baseApi).build();
        IMoonService moonService = restAdapter.create(IMoonService.class);

        moonService.getAD(new CustomApiCallback<BaseModel<ADModel>>() {
            @Override
            public void success(BaseModel<ADModel> adModelBaseModel, Response response) {
                super.success(adModelBaseModel, response);
                ImageDisplayUtil.showPicture(SplashActivity.this, ivAd, adModelBaseModel.getResponse().get(0).getImgurl());
            }
        });

    }
}
