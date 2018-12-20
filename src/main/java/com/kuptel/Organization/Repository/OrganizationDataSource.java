package com.kuptel.Organization.Repository;

import com.kuptel.Organization.Model.Node;
import java.util.List;
import java.util.Map;

public interface OrganizationDataSource {
    List<Node> getOrganizationStructure();
    RepositoryResponse changeParentOfNode(String nodeId, String parentId, int newHeight,
                                          List<Map.Entry<String, Integer>> heightUpdates);
}
