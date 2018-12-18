package com.kuptel.Organization.Model;

public class Node {
    private String id;
    private String parent;
    private String root;
    private int height;

    public Node(String id, String parent, String root, int height){
        this.id = id;
        this.setParent(parent);
        this.setRoot(root);
        this.setHeight(height);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
