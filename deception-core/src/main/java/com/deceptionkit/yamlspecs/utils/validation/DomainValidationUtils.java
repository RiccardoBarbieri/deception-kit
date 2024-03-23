package com.deceptionkit.yamlspecs.utils.validation;

import com.deceptionkit.database.validation.repository.TldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DomainValidationUtils {

    private final Logger logger = LoggerFactory.getLogger(DomainValidationUtils.class);

    private static TldRepository tldRepository;

    private final String domainRegex = "^(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$";

    public Boolean idDomainValid(String domain) {
        String tld = domain.substring(domain.lastIndexOf(".") + 1).toLowerCase();
        Boolean tldValid = tldRepository.existsByTld(tld);
        Boolean domainValid = domainValid(domain.toLowerCase());
        logger.debug("Validating domain: {} | TLD: {}, Domain: {}", domain, tldValid, domainValid);
        return tldValid && domainValid;
    }

    private Boolean domainValid(String domain) {
        return domain.toLowerCase().matches(domainRegex);
    }

    @Autowired
    public void setTldRepository(TldRepository tldRepository) {
        DomainValidationUtils.tldRepository = tldRepository;
    }

    public DomainValidationUtils() {
    }
}

// print patterns that match the following regex
// ^(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,}$
// Example: www.google.com
// Example: www.google.co.uk
// Example: www.google.com.au
// Example: www.google.com.au
// Example: www.google.com.au

