import java.util.ArrayList;
import java.util.List;

public class TreeNode2 implements Comparable<TreeNode2>{

    private String name;
    private Integer count;
    private TreeNode2 parent;
    private List<TreeNode2> children;
    private TreeNode2 nextHomonym; // next same name node

    public TreeNode2() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    public void Sum(Integer count) {
        this.count =this.count+count;
    }
    public TreeNode2 getParent() {
        return parent;
    }

    public void setParent(TreeNode2 parent) {
        this.parent = parent;
    }

    public List<TreeNode2> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode2> children) {
        this.children = children;
    }

    public TreeNode2 getNextHomonym() {
        return nextHomonym;
    }

    public void setNextHomonym(TreeNode2 nextHomonym) {
        this.nextHomonym = nextHomonym;
    }



    public void addChild(TreeNode2 child) {
        if (this.getChildren() == null) {
            List<TreeNode2> list = new ArrayList<TreeNode2>();
            list.add(child);
            this.setChildren(list);
        } else {
            this.getChildren().add(child);
        }
    }



    public TreeNode2 findChild(String name) {
        List<TreeNode2> children = this.getChildren();
        if (children != null) {
            for (TreeNode2 child : children) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }
        }
        return null;
    }


    @Override
    public int compareTo(TreeNode2 arg0) {
        int count0 = arg0.getCount();
        return count0 - this.count;
    }
}
