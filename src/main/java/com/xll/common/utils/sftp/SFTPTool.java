package com.xll.common.utils.sftp;

import com.jcraft.jsch.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

public class SFTPTool {
    private Logger logger = LoggerFactory.getLogger(SFTPTool.class);
    private ChannelSftp sftp = null;
    private Session sshSession = null;

    SFTPTool() {
    }

    public SFTPTool(String sftpip, int sftpport, String sftpusername, String sftppassword) throws JSchException {
        this.sftp = new ChannelSftp();
        JSch jsch = new JSch();
        jsch.getSession(sftpusername, sftpip, sftpport);
        this.sshSession = jsch.getSession(sftpusername, sftpip, sftpport);
        this.logger.info("Session created");
        this.sshSession.setPassword(sftppassword);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        this.sshSession.setConfig(sshConfig);
        this.sshSession.setTimeout(30000);
        this.sshSession.connect();
        Channel channel = this.sshSession.openChannel("sftp");
        channel.connect();
        this.sftp = (ChannelSftp)channel;
    }

    public void disconnect() {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
            } else if (this.sftp.isClosed()) {
                System.out.println("sftp is closed already");
            }
        }

        if (this.sshSession != null) {
            this.sshSession.disconnect();
        }

    }

    public Vector listFiles(String directory) throws SftpException {
        return this.sftp.ls(directory);
    }

    public int upload(String directory, String uploadFile) throws SftpException {
        this.sftp.cd(directory);
        File file = new File(uploadFile);
        if (!file.exists()) {
            this.logger.info("文件 {} 不存在", uploadFile);
            return 1;
        } else {
            try {
                this.sftp.put(new FileInputStream(file), file.getName());
                return 0;
            } catch (FileNotFoundException var5) {
                this.logger.error("文件不存在", var5);
                return 1;
            }
        }
    }

    public int download(String directory, String downloadFile, String saveFile) throws SftpException {
        this.sftp.cd(directory);
        File file = new File(downloadFile);
        if (!file.exists()) {
            this.logger.info("文件 {} 不存在", downloadFile);
            return 1;
        } else {
            try {
                this.sftp.get(downloadFile, new FileOutputStream(file));
                return 0;
            } catch (FileNotFoundException var6) {
                this.logger.error("文件不存在", var6);
                return 1;
            }
        }
    }

    public void delete(String directory, String deleteFile) throws SftpException {
        this.sftp.cd(directory);
        this.sftp.rm(deleteFile);
    }

    public void createDir(String filepath) {
        boolean bcreated = false;
        boolean bparent = false;
        File file = new File(filepath);
        String ppath = file.getParent().replace("\\", "//");

        try {
            this.sftp.cd(ppath);
            bparent = true;
        } catch (SftpException var10) {
            bparent = false;
        }

        try {
            if (bparent) {
                try {
                    this.sftp.cd(filepath);
                    bcreated = true;
                } catch (Exception var8) {
                    bcreated = false;
                }

                if (!bcreated) {
                    this.sftp.mkdir(filepath);
                    this.sftp.cd(filepath);
                    bcreated = true;
                }

                return;
            }

            this.createDir(ppath);
            this.sftp.cd(ppath);
            this.sftp.mkdir(filepath);
        } catch (SftpException var9) {
            this.logger.error("mkdir failed : {}", filepath);
            var9.printStackTrace();
        }

        try {
            this.sftp.cd(filepath);
        } catch (SftpException var7) {
            var7.printStackTrace();
            this.logger.error("can not cd into :", filepath);
        }

    }

    public boolean sendFile(String remotePath, String localFileName) throws SftpException {
        if (localFileName != null && !"".equals(localFileName)) {
            if (localFileName.indexOf("/") == -1) {
                localFileName = localFileName.replace("\\\\", "\\");
                localFileName = localFileName.replace("\\", "/");
            }

            this.logger.info("remote path : {} : {} :", remotePath, localFileName);
            String toFileName = localFileName.substring(localFileName.lastIndexOf("/") == -1 ? 0 : localFileName.lastIndexOf("/") + 1, localFileName.length());
            this.logger.info("remote path : {} : {} :", remotePath, toFileName);
            FileInputStream fs = null;
            this.createDir(remotePath);
            File file = new File(localFileName);
            if (!file.exists()) {
                return false;
            } else {
                try {
                    fs = new FileInputStream(file);
                    this.sftp.put(fs, toFileName);
                } catch (FileNotFoundException var15) {
                    var15.printStackTrace();
                } finally {
                    if (fs != null) {
                        try {
                            fs.close();
                        } catch (IOException var14) {
                            var14.printStackTrace();
                        }
                    }

                }

                return true;
            }
        } else {
            return false;
        }
    }

    public int readFile(String remotePath, String localPath, String remoteBakPath, boolean isDel) throws JSchException {
        int readIsSuccess = 0;
        if (remotePath != null && !"".equals(remotePath)) {
            try {
                Vector<ChannelSftp.LsEntry> directorys = this.listFiles(remotePath);
                if (directorys.size() > 0) {
                    for(int k = 0; k < directorys.size(); ++k) {
                        ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry)directorys.get(k);
                        if (null != lsEntry) {
                            boolean isRead = true;
                            isRead = judgeFilesName(lsEntry.getFilename());
                            if (isRead) {
                                try {
                                    String filename = lsEntry.getFilename();
                                    if (!".".equals(filename) && !"..".equals(filename)) {
                                        localPath = localPath.lastIndexOf("/") > -1 ? localPath : localPath + "/";
                                        FileUtil.dynamicCreateDir(localPath);
                                        int flag = this.download(remotePath, filename, localPath + "/" + filename);
                                        if (flag == 0) {
                                            readIsSuccess = 1;
                                        } else {
                                            if (!StringUtils.isEmpty(remoteBakPath)) {
                                                this.upload(remoteBakPath, localPath + "/" + filename);
                                            }

                                            if (isDel) {
                                                this.delete(remotePath, filename);
                                            }
                                        }
                                    }
                                } catch (Exception var16) {
                                    readIsSuccess = 3;
                                    var16.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Exception var17) {
                readIsSuccess = 2;
                var17.printStackTrace();
            } finally {
                this.sftp.disconnect();
            }

            return readIsSuccess;
        } else {
            readIsSuccess = 1;
            return readIsSuccess;
        }
    }

    public int uploadStringToFTP(String directory, String fileName, String fileContent) throws SftpException {
        return this.uploadStringToFTP(directory, fileName, fileContent, "GBK");
    }

    public int uploadStringToFTP(String directory, String fileName, String fileContent, String charset) throws SftpException {
        try {
            this.sftp.cd(directory);
            this.sftp.put(new ByteArrayInputStream(fileContent.getBytes(charset)), fileName);
            return 0;
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
            return 1;
        }
    }

    public String readFTPToString(String directory, String fileName) throws SftpException {
        return this.readFTPToString(directory, fileName, "GBK");
    }

    public String readFTPToString(String directory, String fileName, String charset) throws SftpException {
        this.sftp.cd(directory);
        InputStream input = this.sftp.get(fileName);
        StringBuffer sb = new StringBuffer();
        String s = "";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input, charset));

            while((s = in.readLine()) != null) {
                sb.append(s);
                sb.append("\r\n");
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return sb.toString();
    }

    private static boolean judgeFilesName(String fileName) {
        boolean flag = true;
        if ("".equals(fileName) && fileName == null) {
            flag = false;
            return flag;
        } else {
            String[] name = fileName.split("\\.");
            if (name.length == 0) {
                flag = false;
                return flag;
            } else {
                for(int i = 0; i < name.length; ++i) {
                    if (name[i] == null || name[i].equals("")) {
                        flag = false;
                        return flag;
                    }
                }

                return flag;
            }
        }
    }

    public ChannelSftp getSftp() {
        return this.sftp;
    }

    public void setSftp(ChannelSftp sftp) {
        this.sftp = sftp;
    }
}
