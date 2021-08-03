package com.ecc.setubot.utils;

import java.io.*;
import java.security.MessageDigest;

public class FileUtils {

    public static File fileCheck(String filePath) throws IOException {
        File file = new File(filePath);

        if (file.exists()) {
            return file;
        }
        File parentFile = file.getParentFile();
        if (null != parentFile && !parentFile.exists()) {
            fileDirsCheck(parentFile);
        }
        file.createNewFile();

        return file;
    }

    public static void fileDirsCheck(File file) {
        fileDirsCheck(file, null);
    }

    public static void fileDirsCheck(String filePath) {
        fileDirsCheck(null, filePath);
    }

    /**
     * 检验文件夹，不存在则创建
     * 参数二选一，优先使用file
     *
     * @param file     文件
     * @param filePath 文件夹路径
     */
    public static void fileDirsCheck(File file, String filePath) {
        File fileDirs = null;
        fileDirs = null == file ? new File(filePath) : file.getParentFile();

        if (null != fileDirs && !fileDirs.exists()) {
            //可创建多级目录，并且只能创建目录，无法用来创建文件
            fileDirs.mkdirs();
        }
    }

    /**
     * 检验文件
     *
     * @param filePath 文件路径
     * @return 指定路径的文件
     */
    public static boolean exists(String filePath) {
        File file = new File(filePath);

        //存在则直接返回
        return file.exists();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean remove(String filePath) {
        File file = new File(filePath);
        //存在则删除
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * 复制文件
     *
     * @param srcPath 原文件完整路径
     * @param dstPath 目标文件夹路径
     * @throws IOException 文件处理异常
     */
    public static void copy(String srcPath, String dstPath) throws IOException {
        String newFileName = getFileName(srcPath);
        dstPath = dstPath + File.separator + newFileName;

        FileInputStream inputStream = new FileInputStream(srcPath);
        FileOutputStream outputStream = new FileOutputStream(dstPath);
        byte[] byteBuffer = new byte[1024 * 8];

        int len = 0;
        while ((len = inputStream.read(byteBuffer)) != -1) {
            outputStream.write(byteBuffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
    }

    /**
     * 移动文件
     * 原理是直接修改文件完整路径，以达到移动的目的
     *
     * @param srcPath 原文件完整路径
     * @param dstPath 目标文件夹路径
     * @throws IOException 文件处理异常
     */
    public static boolean move(String srcPath, String dstPath) throws IOException {
        if (StringUtils.isEmpty(srcPath)) {
            throw new IOException("Source file path can not be empty.");
        }
        if (StringUtils.isEmpty(dstPath)) {
            throw new IOException("Target file path can not be empty.");
        }
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            throw new IOException("Source file does not exist.");
        }
        //获取原文件名称+后缀
        String srcFileName = srcPath.substring(srcPath.lastIndexOf(File.separator) + 1);

        return srcFile.renameTo(new File(dstPath + File.separator + srcFileName));
    }

    /**
     * 根据文件路径获取文件名称
     *
     * @param filePath 文件路径
     * @return 文件名称
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "";
        }
        if (!filePath.contains(File.separator)) {
            return filePath;
        }
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 根据文件路径获取目录
     *
     * @param fileFullPath 文件路径
     * @return 文件夹路径
     */
    public static String getFilePath(String fileFullPath) {
        if (StringUtils.isEmpty(fileFullPath)) {
            return "";
        }
        if (!fileFullPath.contains(File.separator)) {
            return fileFullPath;
        }
        //截取最后一个分隔符前面的字段
        return fileFullPath.substring(0, fileFullPath.lastIndexOf(File.separator));
    }

    /**
     * 读取指定目录下所有文件，文件夹以及文件夹下的子文件的列表
     *
     * @param path 指定目录
     * @return 文件对象列表
     */
    public static File[] getListFiles(String path) {
        if (exists(path)) {
            return null;
        }
        return new File(path).listFiles();
    }

    /**
     * 读取指定目录下所有文件，文件夹的名称
     *
     * @param path 指定目录
     * @return 文件，文件夹名称列表
     */
    public static String[] getFileNames(String path) {
        if (!exists(path)) {
            return null;
        }
        return new File(path).list();
    }

    /**
     * 读取指定目录下所有文件，文件夹的名称
     *
     * @param path 指定目录
     * @return 文件，文件夹名称列表
     */
    public static File[] getFiles(String path) {
        if (!exists(path)) {
            return null;
        }
        return new File(path).listFiles();
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        if (!fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String read(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        char[] charBuffer = new char[8092];
        int len = -1;
        StringBuilder stringBuilder = new StringBuilder("");
        while ((len = fileReader.read(charBuffer)) != -1) {
            stringBuilder.append(String.valueOf(charBuffer, 0, len));
        }
        return stringBuilder.toString();
    }

    public static String md5(String path) {
        return md5(new File(path));
    }

    public static String md5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(bytesToHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String bytesToHex(byte[] md5Array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < md5Array.length; i++) {
            int tmp = 0xff & md5Array[i];
            String hexString = Integer.toHexString(tmp);
            if (hexString.length() == 1) {
                stringBuilder.append("0").append(hexString);
            } else {
                stringBuilder.append(hexString);
            }
        }
        return stringBuilder.toString();
    }
}
