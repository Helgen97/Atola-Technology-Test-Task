package nodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileNode implements Node{

    private String fileName;
    private String filePath;
    private long fileSize;

    @Override
    public String toString() {
        return String.format("%s:%s\n", fileName, fileSize);
    }
}
