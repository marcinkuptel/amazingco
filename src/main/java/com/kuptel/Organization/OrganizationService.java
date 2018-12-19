package com.kuptel.Organization;

import com.kuptel.Organization.Model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    private OrganizationDataSource dataSource;
    private List<Node> nodes;
    private Map<String, Node> nodeRef;

    @Autowired
    public OrganizationService(@Qualifier("sql-source") OrganizationDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Node> getDescendantsOfNode(String nodeId) {
        Node node = nodeRef.get(nodeId);
        if (node != null) {
            Queue<Node> queue = new LinkedList<>();
            queue.add(node);
            List<Node> result = new ArrayList<>();

            while(!queue.isEmpty()) {
                Node n = queue.remove();
                result.addAll(n.getChildren());
                queue.addAll(n.getChildren());
            }

            return result;

        } else {
            throw new NodeUnknownException();
        }
    }

    public void changeParentOfNode(String nodeId, String newParentId) {

        Node node = nodeRef.get(nodeId);
        Node newParent = nodeRef.get(newParentId);

        if (node != null && newParent != null) {

            boolean success = dataSource.changeParentOfNode(nodeId, newParentId);

            if (success) {
                Node currentParent = nodeRef.get(node.getParent());
                currentParent.removeChild(node);
                newParent.addChild(node);
                node.setParent(newParentId);
            } else {
                throw new ParentUpdateFailedException();
            }
        } else {
            throw new NodeUnknownException();
        }
    }

    @PostConstruct
    private void loadOrganizationStructure() {
        nodes = dataSource.getOrganizationStructure();
        nodeRef = nodes.stream().collect(Collectors.toMap(node -> node.getId(), node -> node));

        for(Node n : nodes){
            Node parent = nodeRef.get(n.getParent());
            if (parent != null) {
                parent.addChild(n);
            }
        }
    }
}
