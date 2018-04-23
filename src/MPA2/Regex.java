package MPA2;

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

    Regex(List<String> contents, boolean star, Regex parent) {
        this.contents = contents;
        this.star = star;
        this.parent = parent;
        done = false;
    }

    Regex(List<String> contents) {
        new Regex(contents, false, null);
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
}
