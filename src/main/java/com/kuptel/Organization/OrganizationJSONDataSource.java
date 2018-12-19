package com.kuptel.Organization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuptel.Organization.Model.Node;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service(value="json-source")
public class OrganizationJSONDataSource implements OrganizationDataSource {

    private static String ORG_STRUCTURE_RESOURCE = "/org-structure.json";

    @Override
    public List<Node> getOrganizationStructure() {

        InputStream in = getClass().getResourceAsStream(ORG_STRUCTURE_RESOURCE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String content = reader.lines().collect(Collectors.joining());

        ObjectMapper objectMapper = new ObjectMapper();
        List<Node> nodes;
        try {
            nodes = objectMapper.readValue(content, new TypeReference<List<Node>>(){});
        } catch (IOException e) {
            nodes = Collections.emptyList();
        }

        return nodes;
    }

    @Override
    public boolean changeParentOfNode(String nodeId, String parentId) {
        return false;
    }
}
