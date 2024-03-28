package com.deceptionkit.database.validation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("tlds")
public class Tld {

    @Id
    private String tld;

    public Tld() {
    }

    public Tld(String tld) {
        this.tld = tld;
    }

    public String getTld() {
        return tld;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tld other = (Tld) obj;
        return Objects.equals(this.tld, other.tld);
    }

}
