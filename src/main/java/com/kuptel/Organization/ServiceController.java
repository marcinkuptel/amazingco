package com.kuptel.Organization;

import com.kuptel.Organization.Exceptions.AncestorViolationException;
import com.kuptel.Organization.Exceptions.NodeUnknownException;
import com.kuptel.Organization.Exceptions.ParentUpdateFailedException;
import com.kuptel.Organization.Service.OrganizationService;
import com.kuptel.Organization.Service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The only REST controller in the project, exposing two endpoints:
 * 1) /v1/organization/{nodeId}/descendants - returns all descendants of node with id=<i>nodeId</i>.
 * 2) /v1/organization/{nodeId}/parent - changes the parent of node with id=<i>nodeId</i> to the parent whose id
 * is in the body of the request.
 */
@RestController
@RequestMapping(value = "/v1/organization")
public class ServiceController {

    private OrganizationService organizationService;

    @Autowired
    public ServiceController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @RequestMapping(value = "/{nodeId}/descendants", method = RequestMethod.GET)
    public ResponseEntity<List<Node>> getDescendants(@PathVariable("nodeId") String nodeId) {
        List<Node> descendantsOfNode = organizationService.getDescendantsOfNode(nodeId);
        return new ResponseEntity<>(descendantsOfNode, HttpStatus.OK);
    }

    @RequestMapping(value = "/{nodeId}/parent/{parentId}", method = RequestMethod.POST)
    public ResponseEntity changeParent(
            @PathVariable("nodeId") String nodeId,
            @PathVariable("parentId") String newParentId) {

        ServiceResponse response = organizationService.changeParentOfNode(nodeId, newParentId);

        switch(response){
            case OK: return new ResponseEntity(HttpStatus.OK);
            case NODE_UNKNOWN: throw new NodeUnknownException();
            case PARENT_UPDATE_FAILED: throw new ParentUpdateFailedException();
            case ANCESTOR_VIOLATION: throw new AncestorViolationException();
            default: return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
