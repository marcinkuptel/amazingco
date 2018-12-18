package com.kuptel.Organization;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuptel.Organization.Model.Node;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class OrganizationJSONDataSource implements OrganizationDataSource {

    private static String ORG_STRUCTURE_RESOURCE = "/org-structure.json";

    @Override
    @Async
    public CompletableFuture<List<Node>> getOrganizationStructure() {

        InputStream in = getClass().getResourceAsStream(ORG_STRUCTURE_RESOURCE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String content = reader.lines().collect(Collectors.joining());

        Gson gson = new Gson();
        List<Node> nodes = gson.fromJson(content, new TypeToken<List<Node>>(){}.getType());

        return CompletableFuture.completedFuture(nodes);
    }
}
