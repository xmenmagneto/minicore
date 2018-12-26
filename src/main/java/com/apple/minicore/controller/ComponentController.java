package com.apple.minicore.controller;


import com.apple.minicore.model.Component;
import com.apple.minicore.model.ComponentTreeNode;
import com.apple.minicore.request.ParentChildPair;
import com.apple.minicore.service.ComponentService;
import com.apple.minicore.service.ComponentTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private ComponentTreeService componentTreeService;



    /**
     * Given a component id, GET all its sub components, and display the result in Tree Structure
     *
     * @param id
     * @return list of sub components excluding the root component
     */
    @RequestMapping("/components/getsub/{id}")
    public List<ComponentTreeNode> getSubComponents(@PathVariable Integer id) {
        return componentTreeService.getComponentTree(id);
    }


    /**
     * POST a new component and store into database
     *
     * @param component
     * @return the complete component information stored in database
     */
    @RequestMapping(method = RequestMethod.POST, value = "/components")
    public Component addComponent(@RequestBody Component component) {
        componentService.addComponent(component);
        return componentService.getComponent(component.getId());
    }


    /**
     * Given a component id, GET the information of this component from database
     *
     * @param id
     * @return The component retrieved from database
     */
    @RequestMapping("/components/{id}")
    public Component getComponent(@PathVariable Integer id) {
        return componentService.getComponent(id);
    }


    /**
     * Update a component using PUT method
     *
     * @param component
     * @return The updated Component
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/components/{id}")
    public Component updateComponent(@PathVariable Integer id, @RequestBody Component component) {
        componentService.updateComponent(id, component);
        return componentService.getComponent(id);
    }


    /**
     * Given a component id, delete this component and all its sub components from database
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/components/{id}")
    public String deleteComponent(@PathVariable Integer id) {
        componentService.deleteComponent(id);
        return "The specified component and all its sub components have been deleted!";
    }


    /**
     * Given parentId and childId, link the child component to parent component,
     * and update its sub components accordingly
     *
     * @param pair
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "components/link")
    public String linkComponents(@RequestBody ParentChildPair pair) {
        componentService.linkComponents(pair.getParentId(), pair.getChildId());
        return "Parent " + pair.getParentId() + " and Child " + pair.getChildId() + " has been connected!";
    }


    /**
     * Given parentId and childId, disconnect child component from parent component,
     * and update all its sub component accordingly
     *
     * @param pair
     * @return The list of sub components of parent
     */
    @RequestMapping(method = RequestMethod.POST, value = "components/unlink")
    public String unlinkComponents(@RequestBody ParentChildPair pair) {
        componentService.unlinkComponents(pair.getParentId(), pair.getChildId());
        return "Parent " + pair.getParentId() + " and Child " + pair.getChildId() + " has been disconnected!";
    }

}
