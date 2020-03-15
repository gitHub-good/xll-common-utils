package com.xll.common.utils.sftp;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    public static String SEGM_NEWLINE = "\n";
    public static String SEGM_COLUMN = ";";
    public static String SEGM_HOLDER = "---";

    public FileUtil() {
    }

    public static boolean copyFile(File file, String newFilePath, String newFileName) {
        boolean result = true;
        if (!file.isDirectory()) {
            InputStream is = null;
            if (StringUtils.isEmpty(newFileName)) {
                newFileName = file.getName();
            }

            try {
                is = new FileInputStream(file);
                result = copyFile(is, newFilePath, newFileName, (Integer)null);
            } catch (FileNotFoundException var19) {
                var19.printStackTrace();
                result = false;
            } catch (IOException var20) {
                var20.printStackTrace();
                result = false;
            } finally {
                if (null != is) {
                    try {
                        is.close();
                    } catch (IOException var18) {
                        var18.printStackTrace();
                    }
                }

            }

            return result;
        } else {
            if (StringUtils.isEmpty(newFileName)) {
                System.out.println("### WARN!!! The file is directory ! So the newFileName is unavailable!!!");
            }

            File[] childFileArray = file.listFiles();
            File[] var5 = childFileArray;
            int var6 = childFileArray.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                File childFile = var5[var7];
                result = copyFile(childFile, newFilePath + "/" + file.getName(), (String)null);
                if (!result) {
                    break;
                }
            }

            return result;
        }
    }

    public static File[] getFileArray(String path) {
        File file = new File(path);
        File[] fileArray = null;
        if (file.exists() && file.isDirectory()) {
            fileArray = file.listFiles();
        }

        return fileArray;
    }

    public static boolean copyFile(InputStream is, String newFilePath, String newFileName, Integer buffer) throws IOException {
        boolean result = false;
        if (null != is && !StringUtils.isEmpty(newFilePath) && !StringUtils.isEmpty(newFileName)) {
            if (null != buffer) {
                if (buffer <= 0) {
                    throw new IOException("### The stream buffer's size not greater than 0,please theck this param!");
                }
            } else {
                buffer = 4096;
            }

            File newDirectary = new File(newFilePath);
            if (!newDirectary.exists()) {
                newDirectary.mkdirs();
            }

            File newFile = new File(newFilePath + "/" + newFileName);
            OutputStream os = new FileOutputStream(newFile);
            byte[] size = new byte[buffer];
            boolean var9 = false;

            int index;
            while((index = is.read(size)) > 0) {
                os.write(size, 0, index);
            }

            if (null != is) {
                is.close();
            }

            if (null != os) {
                os.flush();
                os.close();
            }

            result = true;
            return result;
        } else {
            throw new IOException("### the InputStream or new file's path or new file's name is null!!!!!!!! - -!see my eyes, if i know you, you will be dead!\nInputStream:" + is + "\nnewFilePath:" + newFilePath + "\nnewFileName:" + newFileName);
        }
    }

    public static void moveFile(String sourcePath, String sourceFile, String dscPath, String doneFileSuffix) {
        dscPath = dscPath.endsWith("/") ? dscPath : dscPath + "/";
        sourcePath = sourcePath.endsWith("/") ? sourcePath : sourcePath + "/";
        File oldFile = new File(sourcePath + sourceFile);

        try {
            dynamicCreateDir(dscPath);
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        String newFile = dscPath + sourceFile;
        File fnewpath = new File(dscPath);
        if (!fnewpath.exists()) {
            fnewpath.mkdirs();
        }

        File fnew = new File(newFile);
        oldFile.renameTo(fnew);
    }

    public static void moveFile(File sourceFile) {
        String newPath = sourceFile.getParent() + "/.done/" + DateUtil.day() + "/";
        File fnewpath = new File(newPath);
        if (!fnewpath.exists()) {
            fnewpath.mkdirs();
        }

        File fnew = new File(newPath + sourceFile.getName());
        sourceFile.renameTo(fnew);
    }

    public static InputStream getInputStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static OutputStream getFileOutputStream(String path, boolean isAppend) throws FileNotFoundException {
        try {
            return new FileOutputStream(path, isAppend);
        } catch (FileNotFoundException var3) {
            throw var3;
        }
    }

    public static File createFileAsFile(String filepath, String FileName) throws Exception {
        File file = null;

        try {
            if (null != filepath && "".equals(filepath.trim())) {
                String ch = filepath.substring(filepath.length() - 1, filepath.length());
                if (!ch.equals("/") && !ch.equals("\\")) {
                    filepath = filepath + "/";
                }

                if (!(new File(filepath)).isDirectory()) {
                    file = new File(filepath);
                    file.mkdirs();
                }
            }

            if (null != FileName && "".equals(FileName.trim())) {
                file = new File(filepath + FileName);
                file.createNewFile();
            }

            return file;
        } catch (Exception var4) {
            throw var4;
        }
    }

    public static void createFile(String path, String fileName, String content) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(path + "/" + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] byteArray = content.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        byte[] buffer = new byte[1024];
        boolean var9 = false;

        int byteread;
        while((byteread = in.read(buffer)) != -1) {
            fos.write(buffer, 0, byteread);
        }

        fos.flush();
        fos.close();
    }

    public static boolean CreateFile(String filepath, String FileName) throws Exception {
        try {
            if (null != filepath && "".equals(filepath.trim())) {
                String ch = filepath.substring(filepath.length() - 1, filepath.length());
                if (!ch.equals("/") && !ch.equals("\\")) {
                    filepath = filepath + "/";
                }
            }

            if (!(new File(filepath)).isDirectory()) {
                (new File(filepath)).mkdirs();
            }

            return null != FileName && "".equals(FileName.trim()) && !(new File(filepath + FileName)).isFile() ? (new File(filepath + FileName)).createNewFile() : true;
        } catch (Exception var3) {
            throw var3;
        }
    }

    public static void writeFile(String path, String source, boolean isAppend) throws Exception {
        byte[] bytes = source.getBytes();
        OutputStream os = getFileOutputStream(path, isAppend);
        os.write(bytes);
        os.close();
    }

    public static void writeFile(String path, String source) throws Exception {
        byte[] bytes = source.getBytes();
        OutputStream os = getFileOutputStream(path, false);
        os.write(bytes);
        os.close();
    }

    public static void writeFile(String filePath, String fileName, InputStream in) throws Exception {
        BufferedOutputStream outStream = null;
        BufferedInputStream inputStream = null;

        try {
            outStream = new BufferedOutputStream(new FileOutputStream(createFileAsFile(filePath, fileName)));
            inputStream = new BufferedInputStream(in);

            int c;
            while((c = inputStream.read()) != -1) {
                outStream.write(c);
                outStream.flush();
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            throw var9;
        } finally {
            if (outStream != null) {
                outStream.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

    }

    public static String readFile(String path) throws Exception {
        String output = "";
        File file = new File(path);
        if (!file.exists()) {
            throw new Exception("file:" + path + " Does not exist!");
        } else {
            if (file.isFile()) {
                BufferedReader input = new BufferedReader(new FileReader(file));
                StringBuffer buffer = new StringBuffer();
                String text = "";

                while((text = input.readLine()) != null) {
                    buffer.append(text).append("\n");
                }

                output = buffer.toString();
                if (input != null) {
                    input.close();
                }
            } else if (file.isDirectory()) {
                String[] dir = file.list();
                output = output + "Directory contents:\n";

                for(int i = 0; i < dir.length; ++i) {
                    output = output + dir[i] + "\n";
                }
            }

            return output;
        }
    }

    public static String readFile(InputStream stream) throws Exception {
        byte[] b = new byte[1024];
        int len = 0;

        int temp;
        for(boolean var3 = false; (temp = stream.read()) != -1; ++len) {
            b[len] = (byte)temp;
        }

        stream.close();
        String result = new String(b, 0, len);
        return result;
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.isFile() ? file.delete() : false;
        } else {
            return false;
        }
    }

    private static ArrayList<File> filePattern(File file, Pattern p) {
        if (file == null) {
            return null;
        } else {
            ArrayList list;
            if (file.isFile()) {
                Matcher fMatcher = p.matcher(file.getName());
                if (fMatcher.matches()) {
                    list = new ArrayList();
                    list.add(file);
                    return list;
                }
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    list = new ArrayList();

                    for(int i = 0; i < files.length; ++i) {
                        ArrayList<File> rlist = filePattern(files[i], p);
                        if (rlist != null) {
                            list.addAll(rlist);
                        }
                    }

                    return list;
                }
            }

            return null;
        }
    }

    public static File[] refreshFileList(String... para) {
        String strPath = "";
        String s = "";
        if (para.length > 1) {
            strPath = para[0];
            s = para[1];
        } else if (para.length == 1) {
            strPath = para[0];
            s = "*";
        } else if (para.length == 0) {
            return null;
        }

        File dir = new File(strPath);
        s = s.replace('.', '#');
        s = s.replaceAll("#", "\\\\.");
        s = s.replace('*', '#');
        s = s.replaceAll("#", ".*");
        s = s.replace('?', '#');
        s = s.replaceAll("#", ".?");
        s = "^" + s + "$";
        Pattern p = Pattern.compile(s);
        ArrayList<?> list = filePattern(dir, p);
        if (list != null) {
            File[] files = new File[list.size()];
            list.toArray(files);
            return files != null && files.length != 0 ? files : null;
        } else {
            return null;
        }
    }

    public static File dynamicCreateDir(String path) throws IOException {
        File dir = new File(path);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return dir;
            } else {
                throw new IOException("File exists of that name");
            }
        } else {
            dir.mkdirs();
            return dir;
        }
    }

    public static boolean canRead(String fromPath, File file, String doneFileName, String readLock) {
        boolean canRead = true;
        String fileName = file.getName();
        File doneFile = new File(fromPath + "/" + fileName + doneFileName);
        if (!doneFile.exists()) {
            canRead = false;
        }

        if (null != readLock && "".equals(readLock.trim())) {
            if (!readLock.startsWith(".")) {
                readLock = "." + readLock;
            }

            File lockFile = new File(fromPath + "/" + fileName + readLock);
            if (lockFile.exists()) {
                canRead = false;
            }
        }

        return canRead;
    }

    public static String getFilePath(String first) {
        return first.substring(first.indexOf("file:") + 5, first.indexOf("?") > -1 ? first.indexOf("?") : first.length());
    }

    public static String getDoneFileSuffix(String doneFileName) {
        String doneSuffix = null;
        if (null != doneFileName && "".equals(doneFileName.trim())) {
            if (doneFileName.indexOf("}") > -1) {
                doneSuffix = doneFileName.substring(doneFileName.lastIndexOf("}") + 1);
            } else {
                doneSuffix = doneFileName;
            }

            if (doneSuffix.indexOf(".") != 0) {
                doneSuffix = "." + doneSuffix;
            }
        }

        return doneSuffix;
    }

    public static byte[] getByte(File file) {
        byte[] ret = null;

        try {
            if (file == null) {
                return null;
            }

            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];

            int n;
            while((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }

            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return ret;
    }
}
