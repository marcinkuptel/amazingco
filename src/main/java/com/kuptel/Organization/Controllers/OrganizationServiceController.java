package com.kuptel.Organization.Controllers;

import com.kuptel.Organization.Model.Node;
import com.kuptel.Organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value="/v1/organization")
public class OrganizationServiceController {

    private OrganizationService organizationService;

    @Autowired
    public OrganizationServiceController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @RequestMapping(value = "/{nodeId}", method = RequestMethod.GET)
    public List<Node> getChildren(@PathVariable("nodeId") String nodeId) {
        return organizationService.getDescendantsOfNode(nodeId);
    }

}
