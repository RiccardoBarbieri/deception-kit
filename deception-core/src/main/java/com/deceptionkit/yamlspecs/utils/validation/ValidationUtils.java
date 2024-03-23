package com.deceptionkit.yamlspecs.utils.validation;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.annotation.Contract;

import java.util.List;

public class ValidationUtils {

    private static final String GENERIC_STRING_REGEX = "^[a-zA-Z0-9_-]+$";
    private static final String GENERIC_STRING_SPACES_REGEX = "^[a-zA-Z0-9_ -]+$";

    public static Boolean validateDomain(String domain) {
        return (new DomainValidationUtils()).idDomainValid(domain);
    }

    public static Boolean validatePositiveInteger(Integer integer) {
        return integer > 0;
    }

    public static Boolean validateGenericString(String string) {
        return string.matches(GENERIC_STRING_REGEX);
    }

    public static Boolean validateGenericStrings(List<String> strings) {
        return strings.stream().allMatch(ValidationUtils::validateGenericString);
    }

    public static Boolean validateUsername(String string) {
        return validateGenericString(string);
    }

    public static Boolean validateGenericStringSpaces(String string) {
        return string.matches(GENERIC_STRING_SPACES_REGEX);
    }

    public static Boolean validateEmail(String email) {
//        return email.matches("^[a-zA-Z0-9_ -]+@[a-zA-Z0-9_ -]+\\.[a-zA-Z0-9_ -]+$");
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }

    public static Boolean validateUrl(String url) {
        UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
        return urlValidator.isValid(url);
    }

    public static Boolean validateUrls(List<String> url) {
        return url.stream().allMatch(ValidationUtils::validateUrl);
    }


}
