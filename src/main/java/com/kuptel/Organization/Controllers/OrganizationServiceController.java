package com.kuptel.Organization.Controllers;

import com.kuptel.Organization.Model.Node;
import com.kuptel.Organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/organization")
public class OrganizationServiceController {

    private OrganizationService organizationService;

    @Autowired
    public OrganizationServiceController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @RequestMapping(value = "/{nodeId}/children", method = RequestMethod.GET)
    public ResponseEntity<List<Node>> getChildren(@PathVariable("nodeId") String nodeId) {
        List<Node> descendantsOfNode = organizationService.getDescendantsOfNode(nodeId);
        return new ResponseEntity<>(descendantsOfNode, HttpStatus.OK);
    }

    @RequestMapping(value = "/{nodeId}/parent", method = RequestMethod.POST)
    public ResponseEntity changeParent(@PathVariable("nodeId") String nodeId, @RequestBody String newParentId) {
        organizationService.changeParentOfNode(nodeId, newParentId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
