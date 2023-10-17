//package com.lcc.spi.spidemo;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
//import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
//import com.google.api.services.analyticsreporting.v4.model.*;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.security.GeneralSecurityException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Properties;
//
//public class HelloAnalyticsReporting {
//    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final String KEY_FILE_LOCATION = "D:\\Downloads\\invertible-tree-399102-486f04cc7b21.json";
//    private static final String VIEW_ID = "G-JCW9TDT16V";
//
//    public static void main(String[] args) {
//
////        // 创建一个Properties对象
////        Properties systemProperties = System.getProperties();
////
////        // 设置http代理主机和端口
////        systemProperties.setProperty("http.proxyHost", "127.0.0.1");
////        systemProperties.setProperty("http.proxyPort", "4780");
////
////        // 应用更改
////        System.setProperties(systemProperties);
//
//        try {
//
//            AnalyticsReporting service = initializeAnalyticsReporting();
//
//
//
//            GetReportsResponse response = getReport(service);
//            printResponse(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Initializes an Analytics Reporting API V4 service object.
//     *
//     * @return An authorized Analytics Reporting API V4 service object.
//     * @throws IOException
//     * @throws GeneralSecurityException
//     */
//    private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {
//
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        GoogleCredential credential = GoogleCredential.fromStream(Files.newInputStream(Paths.get(KEY_FILE_LOCATION))).createScoped(AnalyticsReportingScopes.all());
//
//        // Construct the Analytics Reporting service object.
//        return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
//                .setApplicationName(APPLICATION_NAME).build();
//    }
//
//    /**
//     * Queries the Analytics Reporting API V4.
//     *
//     * @param service An authorized Analytics Reporting API V4 service object.
//     * @return GetReportResponse The Analytics Reporting API V4 response.
//     * @throws IOException
//     */
//    private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
//        // Create the DateRange object.
//        DateRange dateRange = new DateRange();
//        dateRange.setStartDate("7DaysAgo");
//        dateRange.setEndDate("today");
//
//        // Create the Metrics object.
//        Metric sessions = new Metric()
//                .setExpression("ga:sessions")
//                .setAlias("sessions");
//
//        Dimension pageTitle = new Dimension().setName("ga:pageTitle");
//
//        // Create the ReportRequest object.
//        ReportRequest request = new ReportRequest()
//                .setViewId(VIEW_ID)
//                .setDateRanges(Arrays.asList(dateRange))
//                .setMetrics(Arrays.asList(sessions))
//                .setDimensions(Arrays.asList(pageTitle));
//
//        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
//        requests.add(request);
//
//        // Create the GetReportsRequest object.
//        GetReportsRequest getReport = new GetReportsRequest()
//                .setReportRequests(requests);
//
//        // Call the batchGet method.
//        System.out.println("调用google服务器===========");
//        AnalyticsReporting.Reports.BatchGet batchGet = service.reports().batchGet(getReport);
//
//        GetReportsResponse response = batchGet.execute();
//
//        // Return the response.
//        return response;
//    }
//
//    /**
//     * Parses and prints the Analytics Reporting API V4 response.
//     *
//     * @param response An Analytics Reporting API V4 response.
//     */
//    private static void printResponse(GetReportsResponse response) {
//
//        for (Report report : response.getReports()) {
//            ColumnHeader header = report.getColumnHeader();
//            List<String> dimensionHeaders = header.getDimensions();
//            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
//            List<ReportRow> rows = report.getData().getRows();
//
//            if (rows == null) {
//                System.out.println("No data found for " + VIEW_ID);
//                return;
//            }
//
//            for (ReportRow row : rows) {
//                List<String> dimensions = row.getDimensions();
//                List<DateRangeValues> metrics = row.getMetrics();
//
//                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
//                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
//                }
//
//                for (int j = 0; j < metrics.size(); j++) {
//                    System.out.print("Date Range (" + j + "): ");
//                    DateRangeValues values = metrics.get(j);
//                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
//                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
//                    }
//                }
//            }
//        }
//    }
//}
