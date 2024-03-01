package com.lcc.mq.mqdemo.excel;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoogleAdsExcelDTO {


    /**
     * 客户账户名称
     */
    private String customer_name;

    /**
     * 客户账号id
     */
    private String customer_id;

    /**
     * 广告系列名称
     */
    private String campaign_name;

    /**
     * 广告系列id
     *
     */
    private String campaign_id;

    /**
     * 广告组id
     */
    private String ad_group_id;

    /**
     * 广告组名
     */
    private String ad_group_name;

    /**
     * 广告id
     */
    private String ad_id;

    /**
     * 点击次数
     */
    private Long clicks;

    /**
     * 费用
     */
    private Long cost_micros;

    /**
     * 展示次数
     */
    private Long impressions;
}
