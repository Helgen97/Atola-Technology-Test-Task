package deserializer;

import nodes.DirectoryNode;
import nodes.FileNode;
import nodes.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FileSystemDeserializer {

    public static DirectoryNode deserializeFileSystemFile(File fileSystemFile) {

        DirectoryNode root = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileSystemFile));
             Scanner scanner = new Scanner(reader)) {

            root = deserializeRootNode(scanner.nextLine());
            deserializeTree(scanner, root.getDirectoryName(), root);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return root;
    }

    private static void deserializeTree(Scanner scanner, String rootPath, DirectoryNode parentNode) {
        for (int i = 0; i < parentNode.getElements(); i++) {
            String line = scanner.nextLine();
            Node node;

            if (line.contains("|")) {
                node = deserializeDirectoryNode(scanner, line, rootPath);
            } else {
                node = deserializeFileNode(rootPath, line);
            }

            parentNode.addChildren(node);
        }
    }

    private static DirectoryNode deserializeRootNode(String line) {
        String[] rootLineParts = line.split("\\|");
        String rootName = rootLineParts[0];
        int elementsCount = Integer.parseInt(rootLineParts[1]);

        return new DirectoryNode(rootName, elementsCount);
    }

    private static Node deserializeFileNode(String rootPath, String line) {
        String[] parts = line.split(":");

        String fileName = parts[0];
        String filePath = String.format("%s/%s", rootPath, fileName);
        long size = Long.parseLong(parts[1]);

        return new FileNode(fileName, filePath, size);
    }

    private static Node deserializeDirectoryNode(Scanner scanner, String line, String rootPath) {
        String[] parts = line.split("\\|");

        String dirName = parts[0];
        int count = Integer.parseInt(parts[1]);
        String path = String.format("%s/%s", rootPath, dirName);

        DirectoryNode node = new DirectoryNode(dirName, count);

        deserializeTree(scanner, path, node);

        return node;
    }
}
