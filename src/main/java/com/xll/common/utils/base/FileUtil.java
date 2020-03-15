package com.xll.common.utils.base;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class FileUtil {
    private static final int BUFFER_SIZE = 16384;

    public FileUtil() {
    }

    public static void touchPath(String path) {
        try {
            File filePath = new File(path);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static File checkExist(String filepath) {
        File file = new File(filepath);

        try {
            if (file.exists()) {
                if (!file.isDirectory()) {
                    file.createNewFile();
                }
            } else {
                File file2 = new File(file.getParent());
                file2.mkdirs();
                if (!file.isDirectory()) {
                    file.createNewFile();
                }
            }

            return file;
        } catch (Exception var3) {
            var3.printStackTrace();
            return file;
        }
    }

    public static void makeMoreDir(String path) {
        if (null != path) {
            path = path.trim();
            StringTokenizer st = new StringTokenizer(path, System.getProperties().getProperty("file.separator"));
            String path1 = st.nextToken() + System.getProperties().getProperty("file.separator");
            if (System.getProperties().getProperty("file.separator").equals(path.subSequence(0, 1))) {
                path1 = System.getProperties().getProperty("file.separator") + path1;
            }

            String path2 = path1;

            while(st.hasMoreTokens()) {
                path1 = st.nextToken() + System.getProperties().getProperty("file.separator");
                path2 = path2 + path1;
                File inbox = new File(path2);
                if (!inbox.exists()) {
                    inbox.mkdir();
                }
            }

        }
    }

    public static boolean copy(InputStream src, String dstPath) {
        try {
            File dstFile = new File(dstPath);
            byte[] buffer = new byte[65536];
            int length = 0;
            FileOutputStream os = new FileOutputStream(dstFile);
            try {
                while((length = src.read(buffer, 0, buffer.length)) != -1) {
                    os.write(buffer, 0, length);
                }
            } finally {
                close((OutputStream)os);
            }

            return true;
        } catch (Exception var10) {
            var10.printStackTrace();
            return false;
        }
    }

    public static void copy(File sourceFile, String targetFilePath) throws IOException {
        copy(sourceFile, new File(targetFilePath));
    }

    public static void copy(File src, File dst) {
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream(src), 16384);
            out = new BufferedOutputStream(new FileOutputStream(dst), 16384);
            byte[] buffer = new byte[16384];
            boolean var5 = false;

            int len;
            while((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception var18) {
            var18.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

            if (null != out) {
                try {
                    out.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

        }

    }

    public static boolean copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;

            for(int i = 0; i < file.length; ++i) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
                    byte[] buffer = new byte[(int)temp.length()];
                    input.read(buffer);
                    output.write(buffer);
                    output.flush();
                    output.close();
                    input.close();
                }

                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }

            return true;
        } catch (Exception var9) {
            return false;
        }
    }

    public static boolean copyFileAsStream(String file1, String file2) {
        File temp = new File(file1);
        if (temp.isFile()) {
            try {
                checkExist(file2);
                FileInputStream input = new FileInputStream(temp);
                FileOutputStream output = new FileOutputStream(file2);
                byte[] buffer = new byte[(int)temp.length()];
                input.read(buffer);
                output.write(buffer);
                output.flush();
                output.close();
                input.close();
                return true;
            } catch (Exception var6) {
                var6.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static void rename(String path, String suffix) {
        try {
            File file = new File(path);
            File dstFile = new File(path + "." + suffix);
            file.renameTo(dstFile);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static String[] toFileList(String path) {
        File file = new File(path);
        String[] listfilenames = file.list();
        return null == listfilenames ? new String[0] : listfilenames;
    }

    public static List<File> getFiles(File folder) {
        List<File> files = new ArrayList();
        iterateFolder(folder, files);
        return files;
    }

    private static void iterateFolder(File folder, List<File> files) {
        File[] flist = folder.listFiles();
        files.add(folder);
        if (flist != null && flist.length != 0) {
            File[] var3 = flist;
            int var4 = flist.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File f = var3[var5];
                if (f.isDirectory()) {
                    iterateFolder(f, files);
                } else {
                    files.add(f);
                }
            }
        } else {
            files.add(folder);
        }

    }

    public static List<File> findFiles(String baseDirName, String... targetFileName) {
        ArrayList fileList = new ArrayList();

        try {
            String tempName = null;
            File baseDir = new File(baseDirName);
            if (baseDir.exists() && baseDir.isDirectory()) {
                String[] filelist = baseDir.list();

                for(int i = 0; i < filelist.length; ++i) {
                    File readfile = new File(baseDirName + "/" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        tempName = readfile.getName();
                        String[] var8 = targetFileName;
                        int var9 = targetFileName.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            String target = var8[var10];
                            if (wildcardMatch(target, tempName)) {
                                File src = new File(readfile.getAbsoluteFile().toString());
                                fileList.add(src);
                            }
                        }
                    }
                }
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        return fileList;
    }

    public static void findRecuFiles(String baseDirName, List<File> fileList, String... targetFileName) {
        try {
            String tempName = null;
            File baseDir = new File(baseDirName);
            if (baseDir.exists() && baseDir.isDirectory()) {
                String[] filelist = baseDir.list();

                for(int i = 0; i < filelist.length; ++i) {
                    File readfile = new File(baseDirName + "/" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        tempName = readfile.getName();
                        String[] var8 = targetFileName;
                        int var9 = targetFileName.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            String target = var8[var10];
                            if (wildcardMatch(target, tempName)) {
                                File src = new File(readfile.getAbsoluteFile().toString());
                                fileList.add(src);
                            }
                        }
                    } else if (readfile.isDirectory()) {
                        findRecuFiles(baseDirName + File.separator + filelist[i], fileList, targetFileName);
                    }
                }
            } else {
                System.out.println("未找到文件");
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

    }

    public static boolean wildcardMatch(String pattern, String str) {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;

        for(int patternIndex = 0; patternIndex < patternLength; ++patternIndex) {
            char ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                while(strIndex < strLength) {
                    if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
                        return true;
                    }

                    ++strIndex;
                }
            } else if (ch == '?') {
                ++strIndex;
                if (strIndex > strLength) {
                    return false;
                }
            } else {
                if (strIndex >= strLength || ch != str.charAt(strIndex)) {
                    return false;
                }

                ++strIndex;
            }
        }

        return strIndex == strLength;
    }

    public static void toFileList(String strPath, List<String> filelist) {
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for(int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) {
                    toFileList(files[i].getAbsolutePath(), filelist);
                } else {
                    filelist.add(files[i].getName());
                }
            }

        }
    }

    public static void close(InputStream stream) {
        try {
            stream.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void close(OutputStream stream) {
        try {
            stream.flush();
            stream.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void close(Reader reader) {
        try {
            reader.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void close(Writer writer) {
        try {
            writer.flush();
            writer.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void deleteAllSubFiles(String dirPath) throws Exception {
        try {
            File dir = new File(dirPath);
            if (dir.isDirectory()) {
                File[] subFiles = dir.listFiles();

                for(int i = 0; i < subFiles.length; ++i) {
                    File subFile = subFiles[i];

                    try {
                        subFile.delete();
                    } catch (Exception var6) {
                        throw var6;
                    }
                }
            }

        } catch (Exception var7) {
            throw var7;
        }
    }

    public static boolean isFileExisted(String filePathname) {
        boolean existed;
        try {
            File f = new File(filePathname);
            existed = f.isFile();
        } catch (Exception var3) {
            existed = false;
        }

        return existed;
    }

    public static String removePath(String pathname) {
        String fname = pathname;
        int index = pathname.lastIndexOf(File.separator);
        if (index >= 0) {
            fname = pathname.substring(index + 1);
        }

        return fname;
    }

    public static String removeFileName(String pathname) {
        String fname = pathname;
        int index = pathname.lastIndexOf(File.separator);
        if (index >= 0) {
            fname = pathname.substring(0, index);
        }

        return fname;
    }

    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (!file.exists()) {
            return flag;
        } else {
            return file.isFile() ? deleteFile(sPath) : deleteDirectory(sPath);
        }
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }

        return flag;
    }

    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }

        File dirFile = new File(sPath);
        if (dirFile.exists() && dirFile.isDirectory()) {
            boolean flag = true;
            File[] files = dirFile.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                } else {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                return dirFile.delete();
            }
        } else {
            return false;
        }
    }

    public static boolean deleteFileOrDirectory(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            return file.isFile() ? deleteFile(fileName) : deleteDirectory(fileName);
        }
    }

    public static boolean write(String cont, File dist, String encode) {
        OutputStreamWriter writer = null;

        boolean var5;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(dist), encode);
            writer.write(cont);
            boolean var4 = true;
            return var4;
        } catch (IOException var9) {
            var9.printStackTrace();
            var5 = false;
        } finally {
            close((Writer)writer);
        }

        return var5;
    }

    public static void appendWrite(String fileName, String content) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            close((Writer)writer);
        }

    }

    public static boolean writeBuffered(String cont, File dist, String encode) {
        OutputStreamWriter writer = null;
        BufferedWriter bfw = null;

        boolean var6;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(dist, true), encode);
            bfw = new BufferedWriter(writer);
            bfw.write(cont);
            boolean var5 = true;
            return var5;
        } catch (IOException var10) {
            var10.printStackTrace();
            var6 = false;
        } finally {
            close((Writer)writer);
        }

        return var6;
    }

    public static void createImg(String urlPath, String newImageName) throws Exception {
        URL url = new URL(urlPath);
        DataInputStream dis = new DataInputStream(url.openStream());
        FileOutputStream fos = new FileOutputStream(checkExist(newImageName));
        byte[] buffer = new byte[1024];

        int length;
        while((length = dis.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
        }

        dis.close();
        fos.close();
    }

    public static String read(File src, String detector) {
        StringBuffer res = new StringBuffer();
        String line = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(src), detector));

            while((line = reader.readLine()) != null) {
                res.append(line + "\r\n");
            }

            reader.close();
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return res.toString();
    }

    public static String genPathName() {
        return TimeUtil.toString(Calendar.getInstance(), TimeUtil.pathDf);
    }

    public static String getFileSufix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }

    public static byte[] readToByte(String filePath) {
        File file = new File(filePath);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return filecontent;
    }

    public static byte[] readToByte(InputStream is) throws IOException {
        ByteArrayOutputStream swapStream = null;
        byte[] in2b = null;

        try {
            swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            boolean var4 = false;

            int rc;
            while((rc = is.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }

            in2b = swapStream.toByteArray();
        } catch (IOException var8) {
            var8.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }

            if (swapStream != null) {
                swapStream.close();
            }

        }

        return in2b;
    }
}
