package nodes;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.*;

@Getter
@Setter
public class DirectoryNode implements Node {

    private String directoryName;
    private int elements;
    private List<Node> children;

    public DirectoryNode(String directoryName, int elements) {
        this.directoryName = directoryName;
        this.elements = elements;
        this.children = new ArrayList<>();
    }

    public void addChildren(Node children) {
        this.children.add(children);
    }

    public long getDirectorySize(Path path) {
        long size = 0;

        for (Node node : children) {
            if (node instanceof FileNode fileNode) {
                if(fileNode.getFilePath().contains(path.toString())) {
                    size += fileNode.getFileSize();
                }
            } else if (node instanceof DirectoryNode directory) {
                size += directory.getDirectorySize(path);
            }
        }

        return size;
    }

    public List<String> getFileDuplicates() {
        Map<String, List<String>> filesMap = new HashMap<>();
        List<String> duplicates = new ArrayList<>();
        walkTreeAndFillMap(this, filesMap);

        for (List<String> fileList : filesMap.values()) {
            if (fileList.size() > 1) {
                duplicates.addAll(fileList);
            }
        }

        return duplicates;
    }

    private void walkTreeAndFillMap(Node node, Map<String, List<String>> filesMap) {
        if (node instanceof FileNode file) {
            String key = file.getFileSize() + ":" + file.getFileName();

            if (filesMap.containsKey(key)) {
                filesMap.get(key).add(file.getFilePath());
            } else {
                List<String> fileList = new ArrayList<>();
                fileList.add(file.getFilePath());
                filesMap.put(key, fileList);
            }
        } else {
            for (Node child : ((DirectoryNode) node).getChildren()) {
                walkTreeAndFillMap(child, filesMap);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(directoryName).append("|").append(children.size()).append("\n");
        for (Node node : children) {
            stringBuilder.append(node.toString());
        }
        return stringBuilder.toString();
    }
}
