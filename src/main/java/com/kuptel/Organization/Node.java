package com.kuptel.Organization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class Node {
    private String id;
    private String parent;
    private String root;
    private int height;
    @JsonIgnore
    private Set<Node> children = new HashSet<>();

    @JsonCreator
    public Node(@JsonProperty("id") String id,
                @JsonProperty("parent") String parent,
                @JsonProperty("root") String root,
                @JsonProperty("height") int height){
        this.id = id;
        this.parent = parent;
        this.root = root;
        this.height = height;
    }

    public boolean addChild(Node child) {
        if (children.contains(child)) {
            return false;
        } else {
            children.add(child);
            return true;
        }
    }

    public boolean removeChild(Node child) {
        if (children.contains(child)) {
            children.remove(child);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Node))
            return false;
        if (obj == this)
            return true;
        return this.getId() == ((Node) obj).getId();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Set<Node> getChildren() {
        return this.children;
    }

    public String getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }
    public void setParent(String parentId) {
        this.parent = parentId;
    }

    public String getRoot() {
        return root;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) { this.height = height; }
}
