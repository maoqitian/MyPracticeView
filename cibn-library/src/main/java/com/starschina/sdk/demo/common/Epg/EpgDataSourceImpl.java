package com.starschina.sdk.demo.common.Epg;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.starschina.sdk.demo.common.Network.NetworkUtils;
import com.starschina.types.SDKConf;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/14.
 */
public class EpgDataSourceImpl implements IEpgDataSource {

    static final String HOST = "https://e.starschina.com";
    HashMap<String, String> mRequestParams;

    public EpgDataSourceImpl(Context context) {
        mRequestParams = getRequestparams(context);
    }

    /**
     * 测试使用
     * @param appkey
     */
    public EpgDataSourceImpl(String appkey) {
        mRequestParams = new HashMap<>();
        mRequestParams.put("appKey", appkey);
        mRequestParams.put("appOs", "Android");
        mRequestParams.put("osVer", "21");
        mRequestParams.put("appVer", "2.0");
    }

    @Override
    public Observable<String> getCurrentEpg(String videoId) {
        mRequestParams.put("vids[]", videoId);

        EpgService service = NetworkUtils.getInstance().getService(EpgService.class, HOST, false);
        return service.getCurrentEpg(mRequestParams);
    }

    @Override
    public Observable<EpgListEntity> getEpgList(String videoId, int index) {
        mRequestParams.put("startDate", getFormerlyTime(index));
        //Log.d("demo", "[getEpgList] startDate:"+getFormerlyTime(index));

        EpgService service = NetworkUtils.getInstance().getService(EpgService.class, HOST, true);
        return service.getEpgs(videoId, mRequestParams);
    }

    private HashMap<String, String> getRequestparams(Context c) {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("appKey", getAppkey(c));
        requestParams.put("appOs", "Android");
        requestParams.put("osVer", Build.VERSION.RELEASE);
        requestParams.put("appVer", SDKConf.mSdkVersion);

        return requestParams;
    }

    private String getFormerlyTime(int i) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        Calendar cal = Calendar.getInstance();
        TimeZone localZone = cal.getTimeZone();
        //设定SDF的时区为本地
        simpleDateFormat.setTimeZone(localZone);
        cal.add(Calendar.DATE, -i);
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String date = simpleDateFormat.format(cal.getTime());
        String result = date.substring(2, date.length());
        return result;
    }

    private String getAppkey(Context context) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
        }

        String ret = null;

        if (info.metaData != null) {
            ret = info.metaData.getString("CIBN_PLAYER_APPKEY");
        }

        return ret;
    }

    interface EpgService {
        @GET("/api/currentepgs")
        Observable<String> getCurrentEpg(@QueryMap Map<String, String> params);

        @GET("/api/channels/{id}/epgs")
        Observable<EpgListEntity> getEpgs(@Path("id") String vid, @QueryMap Map<String, String> params);
    }
}
