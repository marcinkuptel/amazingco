package com.kuptel.Organization.Repository;

import com.kuptel.Organization.Node;
import java.util.List;
import java.util.Map;

/**
 Interface representing a repository for the data that represents
 the structure of Amazing Co.
 */
public interface Repository {

    /**
     * Used to get the current structure of Amazing Co.
     *
     * @return Nodes that make up the structure of Amazing Co.
     */
    List<Node> getOrganizationStructure();

    /**
     * Changes the parent of the node identified by <i>nodeId</i>.
     *
     * @param nodeId Node, which parent needs to be changed.
     * @param parentId Id of the new parent node.
     * @param newHeight The height of the node with <i>nodeId</i> after parent change.
     * @param heightUpdates Each element in the list contains two elements:
     *                      1) Id of the node whose children should have a new height.
     *                      2) New height that the children nodes should be updated to.
     * @return Custom response indicating whether the operation was successful.
     */
    RepositoryResponse changeParentOfNode(String nodeId, String parentId, int newHeight,
                                          List<Map.Entry<String, Integer>> heightUpdates);
}
