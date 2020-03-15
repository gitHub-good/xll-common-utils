package com.xll.common.utils.base;

public class PathUtil {
    public PathUtil() {
    }

    public static String processFolder(String source) {
        if (!source.startsWith("/")) {
            source = "/" + source;
        }

        if (!source.endsWith("/")) {
            source = source + "/";
        }

        return source;
    }

    public static String findLastLevelFolder(String folder) {
        if (folder.endsWith("/")) {
            folder = folder.substring(0, folder.length() - 1);
        }

        int lastIndex = folder.lastIndexOf("/");
        if (lastIndex != -1 && lastIndex != folder.length() - 1) {
            folder = folder.substring(lastIndex + 1);
        }

        return folder;
    }

    public static void main(String[] args) {
        System.out.println(findLastLevelFolder("/111/2222/"));
        System.out.println(findLastLevelFolder("/111//"));
        System.out.println(findLastLevelFolder("/111/2222"));
    }
}
