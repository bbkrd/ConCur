/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bbk.outputpdf.files;

import com.google.common.io.Files;
import de.bbk.outputpdf.util.Frozen;
import ec.nbdemetra.ws.Workspace;
import ec.nbdemetra.ws.WorkspaceFactory;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author s4504ch
 */
public class HTMLFiles {

    private String currentDir;

    private static HTMLFiles hTMLFiles;

    public static HTMLFiles getInstance() {
        if (hTMLFiles == null) {
            hTMLFiles = new HTMLFiles();
            return hTMLFiles;
        } else {
            return hTMLFiles;
        }
    }

    private HTMLFiles() {
    }

    public boolean selectFolder() {
        JFileChooser fileChooser;
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (currentDir != null) {
            fileChooser.setCurrentDirectory(new File(currentDir));
        }
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            currentDir = fileChooser.getSelectedFile().getAbsolutePath();
            fileChooser.setCurrentDirectory(new File(currentDir));
            return true;
        }
        return false;

    }

    private File selectFileName(File file) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter defaultFilter = new FileNameExtensionFilter("HTML (.html)", "html");
        fileChooser.addChoosableFileFilter(defaultFilter);
        fileChooser.setDialogTitle("The file " + file.getName() + " already exists.");
        fileChooser.setCurrentDirectory(file);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * Creats all files in the selected folder
     *
     * @param html
     * @param saItemName
     */
    public void creatHTMLFile(String html, String saItemName) {
        try {
            saItemName = removeCharacters(saItemName);

            StringBuilder fileName = new StringBuilder();
            fileName.append(currentDir);
            fileName.append("\\").append(Frozen.removeFrozen(saItemName));
            fileName.append(".html");
            //   fileName = NumberForFile(fileName);
            File file = new File(fileName.toString());
            if (file.exists()) {
                file = selectFileName(file);
            }
            if (file != null) {
                Files.write(html, file, Charset.defaultCharset());
                Desktop.getDesktop().open(file);
            }//ToDo löschen
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    /**
     * Saves the html file in subfolder for each mulitdoc
     *
     * @param html
     * @param multiName
     * @param saItemName
     */
    public void creatHTMLFile(String html, String multiName, String saItemName) {

        try {
            Workspace workspace = WorkspaceFactory.getInstance().getActiveWorkspace();
            String wsName = workspace.getName();

            multiName = removeCharacters(multiName);
            saItemName = removeCharacters(saItemName);

            StringBuilder fileName = new StringBuilder();
            fileName.append(currentDir).append("\\").append(wsName).append("\\").append(multiName);

            java.nio.file.Files.createDirectories(Paths.get(fileName.toString()));
            fileName.append("\\").append(saItemName);
            fileName = NumberForFile(fileName);
            fileName.append(".html");
            File pic = new File(fileName.toString());

            Files.write(html, pic, Charset.defaultCharset());
            Desktop.getDesktop().open(pic);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public void createHTMLTempFiles(String html) {

        try {
            File pic = File.createTempFile("Test", ".html");
            Files.write(html, pic, Charset.defaultCharset());
            Desktop.getDesktop().open(pic);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }

    }

    private String removeCharacters(String name) {

        name = name.replace("\\", "");
        name = name.replace("/", "-");
        name = name.replace(":", "."); //nur Windows
        name = name.replace("*", "");
        name = name.replace("?", "");
        name = name.replace("<", "");
        name = name.replace(">", "");
        name = name.replace("|", "");
        name = name.replace("\n", "-");
        name = name.replace(" ", "");
        return name;

    }

    private StringBuilder NumberForFile(StringBuilder path) {
        int counter = 0;
        while (java.nio.file.Files.exists(Paths.get(path.toString() + ".html"))) {
            counter++;

            if (counter == 1) {
                path.append(" (1) ");
            } else {
                //befor this all spaces are removed
                int startindex;
                String strToReplace = " (" + String.valueOf(counter - 1) + ") ";
                startindex = path.indexOf(strToReplace);
                path.replace(startindex, startindex + strToReplace.length(), " (" + String.valueOf(counter) + ") ");
            }
        }
        return path;
    }
}