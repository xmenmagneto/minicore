package com.apple.minicore.request;

public class ParentChildPair {

    private int parentId;
    private int childId;

    public ParentChildPair(int parentId, int childId) {
        this.parentId = parentId;
        this.childId = childId;
    }

    public ParentChildPair() {
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }
}
