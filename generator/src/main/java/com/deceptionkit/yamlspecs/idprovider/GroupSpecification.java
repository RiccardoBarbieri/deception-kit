package com.deceptionkit.yamlspecs.idprovider;

import com.deceptionkit.model.Group;

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
        this.definitions = definitions;
    }


    public List<Group> getGroups() {
        List<Group> groups = new ArrayList<>();
        for (GroupDefinition groupDefinition : definitions) {
            groups.add(groupDefinition.getGroup());
        }
        return groups;
    }

}
