package MPA2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serato, Jay Vince on April 13, 2018.
 */
class Regex {
    private Regex parent;
    private List<Regex> children;
    private List<String> contents;
    private boolean star;
    private boolean done;
    private int childrenInsp = 0;

    Regex(List<String> contents, boolean star, Regex parent) {
        this.contents = contents;
        this.star = star;
        this.parent = parent;
        done = false;
    }

    Regex(String contents) {
        List<String> list = new ArrayList<>();
        list.add(contents);
        this.contents = list;
        star = false;
        parent = null;
    }

    public int getChildrenInsp() {
        return childrenInsp;
    }

    public void setChildrenInsp(int childrenInsp) {
        this.childrenInsp = childrenInsp;
    }

    public Regex getParent() {
        return parent;
    }

    public void setParent(Regex parent) {
        this.parent = parent;
    }

    public List<Regex> getChildren() {
        return children;
    }

    public void setChildren(List<Regex> children) {
        this.children = children;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String toString() {
        String s = "";
        for (String str : contents) {
            s = s.concat(str + " ");
        }
        if (this.getChildren() == null) {
            return s;
        } else {
            for (Regex r : this.getChildren()) {
                s = s.concat(r.toString());
            }
            return s;
        }
    }

    public boolean isUnion() {
        boolean union = false;
        int parentheses = 0;
        for (char c : getContents().get(0).toCharArray()) {
            if (c == '(') {
                parentheses++;
            } else if (c == ')') {
                parentheses--;
            } else if (c == 'U' && parentheses == 0) {
                union = true;
            }
        }
        return union;
    }

    public Regex clearAllDoneStatus(Regex root) {
        root.setChildrenInsp(0);
        root.setDone(false);
        for (Regex child : root.getChildren()) {
            root.setChildrenInsp(0);
            root.setDone(false);
            if (child.getChildren() != null && child.getChildren().size() > 0) {
                clearAllDoneStatus(child);
            }
        }
        return root;
    }
}
