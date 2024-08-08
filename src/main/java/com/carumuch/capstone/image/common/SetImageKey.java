package com.carumuch.capstone.image.common;

public class SetImageKey {
    public static final String FILE_EXTENSION_SEPARATOR=".";

    public static String getFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        /*확장자를 제외한 파일이름 반환*/
        return originalFileName.substring(0,fileExtensionIndex);
    }

    public static String buildImageKey(String originalFileName){
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        /*파일 확장자 반환*/
        String fileExtension = originalFileName.substring(fileExtensionIndex);

        /*확장자를 제외한 파일이름 반환*/
        String fileName = originalFileName.substring(0,fileExtensionIndex);

        /*파일업로드시간*/
        String fileUploadTime = String.valueOf(System.currentTimeMillis());


        /*파일업로드 시간 중간에 삽입하여 파일이름 재지정*/
        return fileName + "_" + fileUploadTime + fileExtension;
    }
}
