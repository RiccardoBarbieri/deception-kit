package com.deceptionkit.cli.utils;

import com.deceptionkit.cli.config.ConfigLoader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PingUtils {

    private static final String pingUrl = ConfigLoader.getInstance().getBaseUrl() + "/ping";

    public static boolean pingDeceptionCore() {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(pingUrl);

            HttpResponse response = httpClient.execute(httpGet);

            return EntityUtils.toString(response.getEntity()).equals("pong");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
