package br.toe.utils;

import java.io.*;
import java.nio.file.*;

public abstract class FileUtils {

    private FileUtils(){}

    public static String read(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException ex) {
            throw new GeneralException(ex.getMessage(), ex);
        }
    }

}
