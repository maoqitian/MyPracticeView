package com.starschina.sdk.demo.common.Epg;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class EpgListEntity {

    private List<EpgEntity> rows;

    public List<EpgEntity> getRows() {
        return rows;
    }

    public void setRows(List<EpgEntity> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
    }
}
