package com.deceptionkit.database.validation.schedule;

import com.deceptionkit.database.validation.model.Tld;
import com.deceptionkit.database.validation.repository.TldRepository;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class TldUpdateSchedule {

    private final Logger logger = LoggerFactory.getLogger(TldUpdateSchedule.class);
    private TldRepository tldRepository;

    @Autowired
    private void setTldRepository(TldRepository tldRepository) {
        this.tldRepository = tldRepository;
    }

    //                    ┌───────────── second (0–59)
    //                    │ ┌───────────── minute (0–59)
    //                    │ │ ┌───────────── hour (0–23)
    //                    │ │ │ ┌───────────── day of the month (1–31)
    //                    │ │ │ │ ┌───────────── month (1–12)
    //                    │ │ │ │ │ ┌───────────── day of the week (0–6) (Sunday to Saturday;
    //                    │ │ │ │ │ │                                   7 is also Sunday on some systems)
    //                    │ │ │ │ │ │
    //                    │ │ │ │ │ │
    // @Scheduled(cron = "* * * * * *")

//    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = "0 0 0 1 */1 *")
    @EventListener(ApplicationReadyEvent.class)
    public void updateTld() {
        logger.debug("Updating TLDs");

        List<Tld> recentTlds = getRecentTlds();
        logger.debug("Recent TLDs: " + recentTlds.size());
        tldRepository.findAll().forEach(tld -> {
            if (!recentTlds.contains(tld)) {
                logger.debug("Detected missing TLD: " + tld.getTld() + ", removing");
                tldRepository.delete(tld);
            }
        });
        recentTlds.forEach(tld -> {
            if (!tldRepository.existsByTld(tld.getTld())) {
                logger.debug("Adding TLD: " + tld.getTld());
                tldRepository.insert(tld);
            }
        });

        logger.debug("TLDs updated");
    }

    private List<Tld> getRecentTlds() {
        List<String> tldsStrings = fetchRecentTlds();
        List<Tld> tlds = new ArrayList<>();
        tldsStrings.forEach(tldString -> {
            Tld tld = new Tld();
            tld.setTld(tldString);
            tlds.add(tld);
        });
        return tlds;
    }

    private List<String> fetchRecentTlds() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://data.iana.org/TLD/tlds-alpha-by-domain.txt");
            List<String> tlds = httpClient.execute(request, response -> {
                if (response.getStatusLine().getStatusCode() != 200) {
                    logger.error("Error fetching TLDs: " + response.getStatusLine().getReasonPhrase());
                }
                String allTlds = EntityUtils.toString(response.getEntity());
                List<String> temp = Stream.of(allTlds.split("\n")).map(String::toLowerCase).filter(tld -> !tld.startsWith("#")).toList();
                return temp;
            });
            logger.debug("Fetched TLDs: " + tlds.size());
            return tlds;
        } catch (Exception e) {
            logger.error("Error fetching TLDs", e);
        }
        return new ArrayList<>();
    }

}
