package nodes;

import deserializer.FileSystemDeserializer;
import deserializer.FileSystemDeserializerTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DirectoryNodeTest {

    private DirectoryNode directoryNode = null;

    @Before
    public void before() {
        File inputFile = new File(FileSystemDeserializerTest.class.getClassLoader()
                .getResource("input.txt").getFile());
        directoryNode = FileSystemDeserializer.deserializeFileSystemFile(inputFile);
    }

    @After
    public void after() {
        directoryNode = null;
    }

    @Test
    public void getDirectorySize() {
        long actualRootDirectorySize = directoryNode.getDirectorySize(Path.of("root"));
        long expectedRootDirectorySize = 36923799;
        long actualDir_aaDirectorySize = directoryNode.getDirectorySize(Path.of("root/dir_aa"));
        long expectedDir_aaDirectorySize = 35880217;
        long actualDir_mnDirectorySize = directoryNode.getDirectorySize(Path.of("root/dir_mn"));
        long expectedDir_mnDirectorySize = 490672;
        long actualDir_bbDirectorySize = directoryNode.getDirectorySize(Path.of("root/dir_mn/dir_bb/dir_bb"));
        long expectedDir_bbDirectorySize = 0;

        assertEquals(expectedRootDirectorySize, actualRootDirectorySize);
        assertEquals(expectedDir_aaDirectorySize, actualDir_aaDirectorySize);
        assertEquals(expectedDir_mnDirectorySize, actualDir_mnDirectorySize);
        assertEquals(expectedDir_bbDirectorySize, actualDir_bbDirectorySize);
    }

    @Test
    public void getFileDuplicates() {
        String actualOutput = String.join("\n", directoryNode.getFileDuplicates());
        List<String> expectedPaths = Arrays.asList(
                "root/dir_aa/file_tq",
                "root/dir_cd/file_tq",
                "root/dir_kb/dir_bs/file_tq",
                "root/dir_mn/file_tq");
        String expectedOutput = String.join("\n", expectedPaths);
        assertEquals(expectedOutput, actualOutput);
    }
}