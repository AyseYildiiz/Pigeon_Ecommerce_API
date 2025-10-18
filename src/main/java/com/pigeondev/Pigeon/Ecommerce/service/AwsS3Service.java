package com.pigeondev.Pigeon.Ecommerce.service;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Slf4j
public class AwsS3Service {

    private final String bucketName = "pigeon-ecommerce";

    @Value("${aws.s3.access}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret}")
    private String awsS3SecretKey;

    public String saveImageToS3(MultipartFile photo) {
        try {

            String S3FileName = photo.getOriginalFilename();
            //Create aws credentials using the access and secret key
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            //Create S3 client with credentials and region
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_WEST_2)
                    .build();

            //Get input stream from photo
            InputStream inputStream = photo.getInputStream();

            //Set metadata for the object
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            //Create a put request to upload the image to S3
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, S3FileName, inputStream, metadata);
            s3Client.putObject(putObjectRequest);

            return "https://" + bucketName + "s3.eu-west-2.amazonaws.com/" + S3FileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save image to AWS S3" + e.getMessage());
        }
    }
}
