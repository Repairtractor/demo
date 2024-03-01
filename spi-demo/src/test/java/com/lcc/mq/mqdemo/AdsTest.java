package com.lcc.mq.mqdemo;

import com.alibaba.excel.EasyExcel;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v14.resources.CustomerClient;
import com.google.ads.googleads.v14.services.*;
import com.google.auth.oauth2.UserCredentials;
import com.lcc.mq.mqdemo.excel.GoogleAdsExcelDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class AdsTest {


//    private static final String DEVELOPER_TOKEN = "C36whFbU6_V289FqCDaP_Q";
    private static final String DEVELOPER_TOKEN = "PZwDUjowUICm5cf-63pEwg";
    private static final String OAUTH_CLIENT_ID = "921140185195-32ll89m93grvsvl2htvucmp5bv0vovlb.apps.googleusercontent.com";
    private static final String OAUTH_CLIENT_SECRET = "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";
    private static final String REFRESH_TOKEN = "1//0eUuO1k7LnElXCgYIARAAGA4SNwF-L9IrrlkqBDZeCq3jExQeEJgZr__mI2bFjB8WjwrL2HkVSFLZVqdTr2YSzDK9LvHcX3sdNgY";
//    private static final String OPERATING_CUSTOMER_ID = "7545974356";
        private static final String OPERATING_CUSTOMER_ID = "5184849929";

    //    private static final String LOGIN_CUSTOMER_ID = "7545974356";
    private static final String LOGIN_CUSTOMER_ID = "8104976978";

    GoogleAdsClient client = null;

    /**
     * 通用查询api
     */
    GoogleAdsServiceClient googleAdsServiceClient;

    /**
     * 账号查询api
     */
    CustomerServiceClient customerServiceClient;


    AdServiceClient adServiceClient;

    /**
     * 创建添加 adGroupAd api
     */
    AdGroupAdServiceClient adGroupAdServiceClient;

    @BeforeEach
    public void bef() {

//        System.setProperty("http.proxySet", "true");
//        System.setProperty("http.proxyHost", "shproxy.yintaerp.com");
//        System.setProperty("http.proxyPort", "8118");

        UserCredentials credentials =
                UserCredentials.newBuilder()
                        .setClientId(OAUTH_CLIENT_ID)
                        .setClientSecret(OAUTH_CLIENT_SECRET)
                        .setRefreshToken(REFRESH_TOKEN)
                        .build();

        client = GoogleAdsClient.newBuilder()
                // Sets the developer token which enables API access.
                .setDeveloperToken(DEVELOPER_TOKEN)
                // Sets the OAuth credentials which provide Google Ads account access.
                .setCredentials(credentials)
                // Optional: sets the login customer ID. This is required when the Google account
                // authenticated with the refresh token does not have direct access to
                // OPERATING_CUSTOMER_ID and the access is via a manager account. In this case, specify
                // the manager account ID as LOGIN_CUSTOMER_ID.
                .setLoginCustomerId(Long.valueOf(LOGIN_CUSTOMER_ID))
                .build();

        adGroupAdServiceClient = client.getLatestVersion().createAdGroupAdServiceClient();


        googleAdsServiceClient = client.getLatestVersion().createGoogleAdsServiceClient();

        customerServiceClient = client.getLatestVersion().createCustomerServiceClient();
    }

    @AfterEach
    public void aft() {
        googleAdsServiceClient.close();
    }


    /**
     * 测试连通性
     * 获取首页广告信息
     */
    @Test
    public void connectivityTest() {
        String query = "SELECT ad_group_ad.ad.final_urls,ad_group_ad.ad.id,campaign.name,ad_group.name,\n" +
                "ad_group_ad.policy_summary.approval_status,\n" +
                "ad_group_ad.ad.type,\n" +
                "ad_group_ad.ad.name," +
                "metrics.clicks,\n" +
                "metrics.impressions,\n" +
                "metrics.cost_micros\n" +
                "FROM ad_group_ad\n" +
                "WHERE segments.date DURING LAST_7_DAYS\n" +
                "AND ad_group_ad.status != 'REMOVED'\n";
        //+                "AND ad_group_ad.ad.id='659480114374'";

        GoogleAdsServiceClient.SearchPagedResponse search = googleAdsServiceClient.search(OPERATING_CUSTOMER_ID, query);


        log.info("获取google广告数据成功==========================构建excel实体=====================");






        for (GoogleAdsRow googleAdsRow : search.iterateAll()) {
            System.out.println(googleAdsRow);
        }



    }


    /**
     * 获取所有广告信息并导出excel
     */
    @Test
    public void allInfoTest() {
        String query = "SELECT ad_group_ad.ad.final_urls,ad_group_ad.ad.id,campaign.name,campaign.id,ad_group.name,\n" +
                "ad_group_ad.policy_summary.approval_status,\n" +
                "ad_group_ad.ad.type,\n" +
                "ad_group_ad.ad.name," +
                "metrics.clicks,\n" +
                "metrics.impressions,\n" +
                "metrics.cost_micros\n" +
                "FROM ad_group_ad\n" +
                "WHERE   segments.date >= '2023-09-21' AND segments.date <= '2023-10-17'\n" +
                "AND ad_group_ad.status != 'REMOVED'";

        GoogleAdsServiceClient.SearchPagedResponse search = googleAdsServiceClient.search(OPERATING_CUSTOMER_ID, query);

        log.info("获取google广告数据成功===============================================");




        List<GoogleAdsExcelDTO> result=new ArrayList<>();

        for (GoogleAdsRow googleAdsRow : search.iterateAll()) {
            GoogleAdsExcelDTO googleAdsExcelDTO = convert(googleAdsRow);
            googleAdsExcelDTO.setCustomer_name("hernest");
            googleAdsExcelDTO.setCustomer_id("8173406042");
            result.add(googleAdsExcelDTO);
        }

        EasyExcel.write("D:\\worker\\worker-ocument\\2023-10-08ads.xls", GoogleAdsExcelDTO.class).sheet("数据").doWrite(result);

    }


    private GoogleAdsExcelDTO convert(GoogleAdsRow googleAdsRow){
        GoogleAdsExcelDTO googleAdsExcelDTO = new GoogleAdsExcelDTO();
        googleAdsExcelDTO.setCampaign_name(googleAdsRow.getCampaign().getName());
        googleAdsExcelDTO.setAd_id(Objects.toString(googleAdsRow.getAdGroupAd().getAd().getId()));
        googleAdsExcelDTO.setClicks(googleAdsRow.getMetrics().getClicks());
        googleAdsExcelDTO.setCost_micros(googleAdsRow.getMetrics().getCostMicros());
        googleAdsExcelDTO.setImpressions(googleAdsRow.getMetrics().getImpressions());

        return googleAdsExcelDTO;
    }

    /**
     * 获取经理账号下关联的所有客户账号
     */
    @Test
    public void allAccountTest() {
        log.info("调用获取所有账号");
        ListAccessibleCustomersResponse listAccessibleCustomersResponse = customerServiceClient.listAccessibleCustomers(
                ListAccessibleCustomersRequest.newBuilder().build());
        log.info("" + listAccessibleCustomersResponse.getResourceNamesCount());

        for (String name : listAccessibleCustomersResponse.getResourceNamesList()) {
            System.out.println(name);
        }
    }

    /**
     * 根据经理账户获取所有普通账户
     */
    @Test
    public void allLowerAccountTest() {
        log.info("根据经理账户获取所有有效下级账户");
        String query = "SELECT customer_client_link.client_customer, customer_client_link.status,customer_client_link.manager_link_id FROM\n" +
                "    customer_client_link ";

//        String query="SELECT customer_client.id, customer_client.descriptive_name FROM customer_client";

        GoogleAdsServiceClient.SearchPagedResponse search = googleAdsServiceClient.search(OPERATING_CUSTOMER_ID, query);

        for (GoogleAdsRow googleAdsRow : search.iterateAll()) {
            CustomerClient customerClient = googleAdsRow.getCustomerClient();
            System.out.println(customerClient);
        }
    }


}

