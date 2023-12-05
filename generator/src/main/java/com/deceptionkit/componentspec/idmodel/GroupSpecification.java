package com.deceptionkit.componentspec.idmodel;

import java.util.List;

public class GroupSpecification {

    private Integer total;
    private List<GroupDefinition> definitions;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<GroupDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<GroupDefinition> definitions) {
        this.definitions = definitions;
    }
}
