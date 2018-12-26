package com.apple.minicore.service;


import com.apple.minicore.exception.ComponentNotFoundException;
import com.apple.minicore.model.Component;
import com.apple.minicore.model.Owner;
import com.apple.minicore.repository.ComponentRepository;
import com.apple.minicore.repository.OwnerRepository;
import com.apple.minicore.util.LevelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ComponentTreeService componentTreeService;


    /**
     * Add a new Component to database
     *
     * @param component
     */
    public void addComponent(Component component) {
        // Get the owner ids from request
        Set<Owner> requestOwners = component.getOwners();
        Set<Owner> owners = new HashSet<>();
        // Get owner object from database
        for (Owner reqOwner : requestOwners) {
            Integer id = reqOwner.getId();
            Optional<Owner> owner = ownerRepository.findById(id);
            if (owner.isPresent()) {
                owners.add(owner.get());
            }
        }
        component.setOwners(owners);
        componentRepository.save(component);
    }

    /**
     * Retrieve a Component from database given its id
     *
     * @param id
     * @return
     */
    public Component getComponent(Integer id) {
        Optional<Component> component = componentRepository.findById(id);
        if (!component.isPresent()) {
            throw new ComponentNotFoundException("Component not found in database for id: " + id);
        }
        return component.get();
    }



    /**
     * Update the existing component in database
     * If the level is changed, all its sub components also need to be changed accordingly
     *
     * @param component
     */
    public void updateComponent(Integer id, Component component) {
        Component prev = getComponent(id);
        component.setId(id);
        prev.getOwners().clear();

        // If the level of current component is updated, we need to update all its sub components;
        String preLevel = LevelUtil.calculateLevel(prev.getLevel(), component.getId());
        String newLevel = LevelUtil.calculateLevel(component.getLevel(), component.getId());

        if (!preLevel.equals(newLevel)) {
            updateSubComponentLevel(preLevel, newLevel);
        }

        // Save the updated component into database
        componentRepository.save(component);
    }


    /**
     * Get all the sub components with level prefix prevLevel, and update their level prefix to newLevel
     *
     * @param oldPrefix
     * @param newPrefix
     */
    private void updateSubComponentLevel(String oldPrefix, String newPrefix) {
        List<Component> subComponents = componentTreeService.getAllSubComponents(oldPrefix);
        for (Component subComponent : subComponents) {
            String level = subComponent.getLevel();
            String rem = level.substring(oldPrefix.length());
            subComponent.setLevel(newPrefix + rem);
            componentRepository.save(subComponent);
        }
    }


    /**
     * Given a component id, delete this component and all its sub components
     *
     * @param id
     */
    public void deleteComponent(Integer id) {
        Component component = getComponent(id);
        component.getOwners().clear();

        // Delete all the sub components
        String nextLevel = LevelUtil.calculateLevel(component.getLevel(), id);

        List<Component> subComponents = componentTreeService.getAllSubComponents(nextLevel);
        for (Component subComponent : subComponents) {
            subComponent.getOwners().clear();
            componentRepository.delete(subComponent);
        }

        componentRepository.delete(component);
    }


    /**
     * Given the Ids of parent and child component, link the child to parent
     * Then update the levels of all sub component of the child component
     *
     * @param parentId
     * @param childId
     */
    public void linkComponents(Integer parentId, Integer childId) {
        // Get parent and child component
        Component parent = getComponent(parentId);
        Component child = getComponent(childId);

        if (child.getId() == parentId) return;

        // Get the prev and new level for child component
        String prevLevel = child.getLevel();
        String newLevel = LevelUtil.calculateLevel(parent.getLevel(), parentId);


        // Update level and parentId for child component
        child.setLevel(newLevel);
        child.setParentId(parentId);
        componentRepository.save(child);

        // Process sub components of child component
        updateSubComponentLevel(LevelUtil.calculateLevel(prevLevel, childId), LevelUtil.calculateLevel(newLevel, childId));
    }


    /**
     * Given parent Id and child Id, disconnect child component from parent component
     * For child component, set the parentId to -1, and level to "0", and update all sub component of child component
     *
     * @param parentId
     * @param childId
     */
    public void unlinkComponents(Integer parentId, Integer childId) {
        Component child = getComponent(childId);
        if (child.getParentId() != parentId) return;

        updateSubComponentLevel(LevelUtil.calculateLevel(child.getLevel(), childId), LevelUtil.calculateLevel("0", childId));

        child.setParentId(-1);
        child.setLevel("0");
        componentRepository.save(child);
    }
}
