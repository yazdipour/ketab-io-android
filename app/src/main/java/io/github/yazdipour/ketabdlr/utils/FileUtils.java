package io.github.yazdipour.ketabdlr.utils;

public class FileUtils {
    //    public static String fileSha1(final File file) throws Exception {
//        final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
//
//        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
//            final byte[] buffer = new byte[1024];
//            for (int read = 0; (read = is.read(buffer)) != -1; ) {
//                messageDigest.update(buffer, 0, read);
//            }
//        }
//        // Convert the byte to hex format
//        try (Formatter formatter = new Formatter()) {
//            for (final byte b : messageDigest.digest()) {
//                formatter.format("%02x", b);
//            }
//            return formatter.toString();
//        }
//    }
//    public static boolean doesExistsAndBiggerThan(File file, int megaBytes) {
//        return file.exists() && file.length() > megaBytes * Math.pow(10, 6);
//    }
//
//    public static File getFolderCreateIfNotExist(String path) {
//        File folder = new File(path);
//        if (!folder.exists()) folder.mkdirs();
//        return folder;
//    }
}
