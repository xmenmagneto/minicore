package com.apple.minicore.model;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree Structure type for Component.
 * Each ComponentTreeNode contains all information of this Component, and an additional children List
 */
public class ComponentTreeNode extends Component {

    private List<ComponentTreeNode> children = new ArrayList<>();


    /**
     * Transform a Component type to ComponentTreeNode type
     *
     * @param component
     * @return
     */
    public static ComponentTreeNode adapt(Component component) {
        ComponentTreeNode componentTreeNode = new ComponentTreeNode();
        BeanUtils.copyProperties(component, componentTreeNode);
        return componentTreeNode;
    }


    public List<ComponentTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<ComponentTreeNode> children) {
        this.children = children;
    }
}
