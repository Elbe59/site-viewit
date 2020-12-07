package tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private static final File dataDirectory = new File("src/test/resources/dataTest");

    public static File createTmpDirectory() throws IOException {
        Path tmpDirectoryPath = Files.createTempDirectory(Paths.get("target/"),"tmp-");
        return tmpDirectoryPath.toFile();
    }

    public static void removeDirectory(File directory) throws IOException {
        Files.delete(directory.toPath());
    }

    public static void duplicateDataToDirectory(File sourceDirectory, File targetdirectory) throws IOException {
        for(File file : sourceDirectory.listFiles()) {
            File dstFile= new File(targetdirectory, file.getName());
            Files.copy(file.toPath(),dstFile.toPath());
        }
    }

    public static void removeDataInDirectory(File directory) {
        for(File file : directory.listFiles()) {
            file.delete();
        }
    }

    public static String getData(File directory, String fileName) throws IOException {
        File file = new File(directory, fileName);
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }

    public static boolean checkIfExist(File directory, String fileName){
        File file = new File(directory, fileName);
        return  file.exists();
    }

	public static File getDatadirectory() {
		return dataDirectory;
	}
    
}
