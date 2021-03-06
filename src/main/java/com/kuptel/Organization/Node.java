package com.kuptel.Organization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing one node in the structure of Amazing Co.
 */
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

    /**
     * Create a parent-child connection between the current
     * node and <i>child</i>.
     *
     * @param child Node to be added as the child of the current node.
     * @return True if the node was added. False if it already exists.
     */
    public boolean addChild(Node child) {
        if (children.contains(child)) {
            return false;
        } else {
            children.add(child);
            return true;
        }
    }

    /**
     * Remove <i>child</i> from the list of child nodes
     * of the current node.
     *
     * @param child Node to be removed.
     * @return True if node was removed. False if it does not exist.
     */
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
