//package com.lcc.spi.spidemo;
//
//import cn.hutool.core.date.DateUtil;
//import com.google.analytics.data.v1beta.*;
//import com.google.auth.oauth2.AccessToken;
//import com.google.auth.oauth2.GoogleCredentials;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//
//@Slf4j
//public class Auth {
//    private static final String ACCESS_TOKEN = "ya29.a0AfB_byBrvf8LMHUzENj1q9RTAwP3oJ4CXFOBWuf8R-YasZDU3MLNYsr7P0dmudWsYfKONqMShHGOosYhN69XvSnvXQnl_sk0rkiCGVu-roXLZpEKihjfTVDhICd2-qwghbPlzlo8RD655XMUunGD_Pomv38mZfRGbM16aCgYKAZESARASFQGOcNnCWIe97e9gSf0BaKnD8tXGog0171";
//    private static final String REFRESH_TOKEN = "1//09l3_DLJnAhQICgYIARAAGAkSNwF-L9IrNm_iUEUFsbOWN9fus43wkQDgN4lUoe_clXCEPVk8tNaVBk2dbECmogvi6tS0RMTeUe0";
//
//
//    BetaAnalyticsDataClient betaAnalyticsDataClient;
//
//
//    public static void main(String[] args) throws IOException {
//
//        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(ACCESS_TOKEN, DateUtil.parse("2023-12-20")));
//
//
//        BetaAnalyticsDataSettings settings = BetaAnalyticsDataSettings
//                .newBuilder()
//                .setCredentialsProvider(() -> credentials)
//                .build();
//
//
//        // Using a default constructor instructs the client to use the credentials
//        // specified in GOOGLE_APPLICATION_CREDENTIALS environment variable.
//        try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create(settings)) {
//
//            RunReportRequest request =
//                    RunReportRequest.newBuilder()
//                            .setProperty("properties/" + "329520236")
//                            .addDimensions(Dimension.newBuilder().setName("city"))
//                            .addMetrics(Metric.newBuilder().setName("activeUsers"))
//                            .addDateRanges(DateRange.newBuilder().setStartDate("2020-03-31").setEndDate("today"))
//                            .build();
//
//            // Make the request.
//            RunReportResponse response = analyticsData.runReport(request);
//
//            System.out.println("Report result:");
//            // Iterate through every row of the API response.
//            for (Row row : response.getRowsList()) {
//                System.out.printf("%s, %s%n", row.getDimensionValues(0).getValue(), row.getMetricValues(0).getValue());
//            }
//        }
//    }
//
//    @BeforeEach
//    public void bef() {
//        try {
//            GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(ACCESS_TOKEN, DateUtil.parse("2023-12-20")));
//            BetaAnalyticsDataSettings settings = BetaAnalyticsDataSettings
//                    .newBuilder()
//                    .setCredentialsProvider(() -> credentials)
//                    .build();
//            this.betaAnalyticsDataClient = BetaAnalyticsDataClient.create(settings);
//        } catch (Exception e) {
//            log.error("权限验证出现错误！请重试{}", e.getMessage());
//        }
//    }
//
//    @AfterEach
//    public void aft() {
//        this.betaAnalyticsDataClient.close();
//    }
//
//
//    /**
//     * Google Analytics（分析）事件数据的自定义报告
//     */
//    @Test
//    public void runReportTest() {
//        RunReportRequest request = RunReportRequest.newBuilder()
//                .setProperty("properties/" + "329520236")
//                .addDimensions(Dimension.newBuilder().setName("country"))
//                .addMetrics(Metric.newBuilder().setName("eventCount"))
//                .addDateRanges(DateRange.newBuilder().setStartDate("2023-09-01").setEndDate("today"))
//                .build();
//
//        RunReportResponse response = betaAnalyticsDataClient.runReport(request);
//
//        //打印响应结果
//        response.getRowsList().forEach(it -> {
//            System.out.print(response.getDimensionHeaders(0).getName() + ": ");
//            it.getDimensionValuesList().stream().map(DimensionValue::getValue).forEach(System.out::print);
//            System.out.print("\t");
//            System.out.print(response.getMetricHeaders(0).getName() + ": ");
//            it.getMetricValuesList().stream().map(MetricValue::getValue).forEach(System.out::print);
//            System.out.println();
//        });
//
//    }
//
//    /**
//     * report的实时数据
//     */
//    @Test
//    public void realTimeReportTest() {
//        RunRealtimeReportRequest request = RunRealtimeReportRequest.newBuilder()
//                .setProperty("properties/" + "329520236")
//                .addDimensions(Dimension.newBuilder().setName("country"))
//                .addMetrics(Metric.newBuilder().setName("screenPageViews"))
//                .build();
//
//        RunRealtimeReportResponse response = betaAnalyticsDataClient.runRealtimeReport(request);
//        //打印响应结果
//        response.getRowsList().forEach(it -> {
//            System.out.print(response.getDimensionHeaders(0).getName() + ": ");
//            it.getDimensionValuesList().stream().map(DimensionValue::getValue).forEach(System.out::print);
//            System.out.print("\t");
//            System.out.print(response.getMetricHeaders(0).getName() + ": ");
//            it.getMetricValuesList().stream().map(MetricValue::getValue).forEach(System.out::print);
//            System.out.println();
//        });
//    }
//
//}
//
