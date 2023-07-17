package com.efub.bageasy.global.service;


import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.efub.bageasy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.efub.bageasy.global.exception.ErrorCode.*;


@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new CustomException(FILE_CONVERT_ERROR));
        return uploadS3(uploadFile, dirName);
    }


    private String uploadS3(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }


    // 이미지 업로드
    public List<String> upload(List<MultipartFile> multipartFile) {
        List<String> imgUrlList = new ArrayList<>();

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가
        for (MultipartFile file : multipartFile) {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket+"/post/image", fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                imgUrlList.add(amazonS3Client.getUrl(bucket+"/post/image", fileName).toString());
            } catch(IOException e) {
                throw new CustomException(FILE_UPLOAD_ERROR);
            }
        }
        return imgUrlList;
    }

    public String getFileUrl(MultipartFile multipartFile){
       String url= String.valueOf(amazonS3Client.getUrl(bucket,multipartFile.getOriginalFilename()));
       return url;
    }

    // 이미지 삭제
    public void deleteImage(String imageUrl) throws IOException{

        String imageName = getFileNameFromURL(imageUrl);

        try {
            amazonS3Client.deleteObject(bucket,"post/image/"+imageName);
        }catch (SdkClientException e){
            throw new CustomException(FILE_DELETE_ERROR);
        }

    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            //throw new PrivateException(Code.WRONG_INPUT_IMAGE);
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            //throw new PrivateException(Code.WRONG_IMAGE_FORMAT);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


    public static String getFileNameFromURL(String url) {

        return url.substring(url.lastIndexOf('/') + 1, url.length());

    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    public void removeFile(List<MultipartFile> multipartFileList){
        for(MultipartFile multipartFile:multipartFileList){
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket,multipartFile.getOriginalFilename()));
        }
    }

    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

}