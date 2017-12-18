package com.starschina.sdk.demo.common.Epg;

import android.content.Context;
import android.util.Log;

import com.starschina.sdk.demo.common.EventConst;
import com.starschina.sdk.demo.common.SimpleEvent;
import com.starschina.types.DChannel;
import com.starschina.types.Epg;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/14.
 */
public class EpgPresenter {
    IEpgView mEpgView;
    IEpgDataSource mDataSource;

    DChannel mChannel;
    EpgEntity mCurrentEpg;

    public EpgPresenter(Context c, IEpgView epgView, DChannel ch) {
        mEpgView = epgView;
        mChannel = ch;

        mDataSource = new EpgDataSourceImpl(c);
    }

    public void getEpgList(int index) {
        Log.d("demo", "[getEpgList]");

        final Observable<String> currentEpg = mDataSource.getCurrentEpg(mChannel.id+"");
        Observable<EpgListEntity> curEpgObser = currentEpg.flatMap(new Func1<String, Observable<EpgListEntity>>() {
            @Override
            public Observable<EpgListEntity> call(String s) {
                return Observable.just(parseCurrentEpg(s));
            }
        });

        Observable<EpgListEntity> epgList =  mDataSource.getEpgList(mChannel.id+"", index);

        Observable.zip(curEpgObser, epgList,
                new Func2<EpgListEntity, EpgListEntity, EpgListEntity>() {
                    @Override
                    public EpgListEntity call(EpgListEntity curEpgEntity, EpgListEntity listEntity) {
                        if (curEpgEntity != null && curEpgEntity.getRows().size() > 0) {
                            mCurrentEpg = curEpgEntity.getRows().get(0);
                        }
                        if (mCurrentEpg !=null && mChannel.currentEpg == null) {
                            mChannel.currentEpg = new Epg();
                            mChannel.currentEpg.id = mCurrentEpg.getEpgId();
                            mChannel.currentEpg.name = mCurrentEpg.getEpgName();
                            mChannel.currentEpg.startTime = mCurrentEpg.getStartTime();
                            mChannel.currentEpg.endTime = mCurrentEpg.getEndTime();
                            mChannel.nextEpg = new Epg();
                            EpgEntity next = curEpgEntity.getRows().get(1);
                            mChannel.nextEpg.id = next.getEpgId();
                            mChannel.nextEpg.name = next.getEpgName();
                            mChannel.nextEpg.startTime = next.getStartTime();
                            mChannel.nextEpg.endTime = next.getEndTime();
                        }
                        return classify(listEntity);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EpgListEntity>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        mEpgView.showErrorHint("get epg:\n"+e.getMessage());
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(EpgListEntity epglist) {
                        Log.d("demo", "[getEpgList] epgEntities:"+epglist.getRows().size());
                        mEpgView.showEpg(epglist.getRows());

                        EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_UPDATE_EPG, mCurrentEpg));
                    }
                });
    }

    private EpgListEntity parseCurrentEpg(String json) {
        Log.d("demo", "[parseCurrentEpg]");
        EpgListEntity curEpglist = new EpgListEntity();
        List<EpgEntity> epglist = new ArrayList<>();
        curEpglist.setRows(epglist);
        try {
            JSONObject jobj = new JSONObject(json);
            JSONObject rows = jobj.getJSONObject("rows");
            JSONObject epg = rows.getJSONObject(mChannel.id+"");
            JSONObject cur = epg.getJSONObject("current");
            Log.d("demo", "[parseCurrentEpg]:"+cur);
            epglist.add(EpgEntity.parse(cur.toString()));
            JSONObject next = epg.getJSONObject("next");
            Log.d("demo", "[parseCurrentEpg]:"+next);
            epglist.add(EpgEntity.parse(next.toString()));
            return curEpglist;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private EpgListEntity classify(EpgListEntity listEntity) {
        Log.d("demo", "[classify]");
        boolean flag = false;
        if (mCurrentEpg != null) {
            for (EpgEntity epg : listEntity.getRows()) {
                if (epg.getEpgId() == mCurrentEpg.getEpgId()) {
                    flag = true;
                    epg.setSelected(true);
                    epg.setStatus(1);
                }else {
                    if (!flag) {
                        epg.setStatus(0);
                    }
                }
            }
        }else {
            long time =  System.currentTimeMillis();
            for (EpgEntity epg : listEntity.getRows()) {
                if (time >= (long) epg.getStartTime()*1000 && time < (long) epg.getEndTime()*1000) {
                    epg.setSelected(true);
                    epg.setStatus(1);
                }else if (time > (long)epg.getEndTime() *1000) {
                    epg.setStatus(0);
                }
            }
        }

        return listEntity;
    }
}
