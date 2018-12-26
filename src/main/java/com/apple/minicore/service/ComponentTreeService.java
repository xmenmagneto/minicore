package com.apple.minicore.service;

import com.apple.minicore.model.Component;
import com.apple.minicore.model.ComponentTreeNode;
import com.apple.minicore.repository.ComponentRepository;
import com.apple.minicore.util.LevelUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Provides function to return Tree Structure Component List
 */
@Service
public class ComponentTreeService {

    @Autowired
    private ComponentRepository componentRepository;


    @Autowired
    private ComponentService componentService;

    /**
     * Given the id of root component, get its sub component tree List
     *
     * {@link ComponentTreeNode} is Tree structure, it contains its sub components on next level
     * @param id if of root component
     *
     * @return {@link ComponentTreeNode}
     */
    public List<ComponentTreeNode> getComponentTree(Integer id) {
        // Calculate current level based on its id
        String curLevel = getCurLevel(id);

        // Next level is current level + "." + current id
        String nextLevel = LevelUtil.calculateLevel(curLevel, id);

        // Create a flat list to contain Tree Structure Component
        List<ComponentTreeNode> subComponentTree = new ArrayList<>();

        // Get all sub components from database, excluding the root component
        List<Component> subComponents = getAllSubComponents(nextLevel);

        // No sub component found
        if (CollectionUtils.isEmpty(subComponents)) {return subComponentTree;}

        // Transform each component into tree node and put into list
        for (Component comp : subComponents) {
            ComponentTreeNode componentTreeNode = ComponentTreeNode.adapt(comp);
            subComponentTree.add(componentTreeNode);
        }
        // Return the tree structure list of sub component
        return compListToTree(subComponentTree, nextLevel);
    }



    /**
     * Get the level of current component given its id
     *
     * @param id
     * @return
     */
    private String getCurLevel(Integer id) {
        Component component = componentService.getComponent(id);
        return component.getLevel();
    }

    /**
     * Given a level prefix, look for all sub components, store them into a list, excluding the root component
     * For example given 0.1.  search for all sub components like 0.1.1, 0.1.2, etc.
     *
     * @param prefix
     * @return
     */
    List<Component> getAllSubComponents(String prefix) {
        List<Component>  subComponents = new ArrayList<>();
        subComponents.addAll(componentRepository.findByLevelLike(prefix));
        subComponents.addAll(componentRepository.findByLevelStartingWith(prefix + ".%"));
        return subComponents;
    }


    /**
     * Given a flat sub component list, process the list and return a children TreeNode list at given level
     *
     * @param subComponents a flat sub component list
     * @param level current level to be processed
     * @return {@link ComponentTreeNode}
     */
    private List<ComponentTreeNode> compListToTree(List<ComponentTreeNode> subComponents, String level) {
        // Corner case
        if (CollectionUtils.isEmpty(subComponents)) {return subComponents;}

        // Use multiMap to store <Level, Components> info, components on the same level will be grouped together
        ListMultimap<String, ComponentTreeNode> levelMap = ArrayListMultimap.create();
        // Create root list to hold the direct children of root component
        List<ComponentTreeNode> rootList = new ArrayList<>();

        // Populate multiMap and rootList
        for (ComponentTreeNode componentTreeNode : subComponents) {
            String curLevel = componentTreeNode.getLevel();
            levelMap.put(curLevel, componentTreeNode);
            if (curLevel.equals(level)) {
                rootList.add(componentTreeNode);
            }
        }

        // Sort the rootList based on component name
        Collections.sort(rootList, comptComparator);

        // Recursively set children for component Tree Node on each level
        transformToTree(rootList, level, levelMap);
        return rootList;
    }

    /**
     * Recursively set next level for Component TreeNode on current level
     *
     * @param rootList
     * @param level
     * @param levelMap
     */
    private void transformToTree(List<ComponentTreeNode> rootList, String level, ListMultimap<String, ComponentTreeNode> levelMap) {
        // Process each component TreeNode individually, they are on the same level
        for (int i = 0; i < rootList.size(); i++) {
            ComponentTreeNode componentTreeNode = rootList.get(i);
            // Calculate next level: level + "." + id
            String nextLevel = LevelUtil.calculateLevel(level, componentTreeNode.getId());
            // Get next level list from map
            List<ComponentTreeNode> nextLevelList = levelMap.get(nextLevel);

            if (CollectionUtils.isNotEmpty(nextLevelList)) {
                // Sort the next level list by component name
                Collections.sort(nextLevelList, comptComparator);
                // Set the next level for current component
                componentTreeNode.setChildren(nextLevelList);
                // Recursively process next level
                transformToTree(nextLevelList, nextLevel, levelMap);
            }
        }
    }


    /**
     * Custom comparator to compare Component TreeNode based on component name
     */
    private Comparator<ComponentTreeNode> comptComparator = new Comparator<ComponentTreeNode>() {
        @Override
        public int compare(ComponentTreeNode o1, ComponentTreeNode o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
}
