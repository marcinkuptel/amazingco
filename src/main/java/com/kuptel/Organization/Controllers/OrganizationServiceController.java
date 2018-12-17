package com.kuptel.Organization.Controllers;

import com.kuptel.Organization.Model.Node;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value="/v1/organization")
public class OrganizationServiceController {

    @RequestMapping(value = "/{nodeId}", method = RequestMethod.GET)
    public List<Node> getChildren(@PathVariable("nodeId") String nodeId) {
        return Arrays.asList(new Node("root"), new Node("a"));
    }

}
