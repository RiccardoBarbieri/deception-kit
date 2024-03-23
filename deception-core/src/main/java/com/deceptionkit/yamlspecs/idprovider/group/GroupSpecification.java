package com.deceptionkit.yamlspecs.idprovider.group;

import com.deceptionkit.model.Group;
import com.deceptionkit.yamlspecs.utils.validation.ValidationUtils;

import java.util.ArrayList;
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
        if (!ValidationUtils.validatePositiveInteger(total)) {
            throw new IllegalArgumentException("Invalid total: " + total);
        }
        this.definitions = definitions;
    }


    public List<Group> convertGroups() {
        List<Group> groups = new ArrayList<>();
        for (GroupDefinition groupDefinition : definitions) {
            groups.add(groupDefinition.convertGroup());
        }
        return groups;
    }

}
