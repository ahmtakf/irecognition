package com.huawei.wireless.irecognition.service;

import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.opencv.core.CvType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.DirectBuffer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.*;
import java.nio.*;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

@Service
public class RecognitionService {

    private static final String trainingDir = "training-dir";
    private static int width = 200;
    private static int height = 200;

    public BufferedImage trainAndSave(MultipartFile file, String fileName){

        long startTime = System.nanoTime();

        BufferedImage bufferedImage = resize( getBufferedImageFromFile(file), width, height);
        MatVector images = new MatVector(1);

        Mat labels = new Mat(1, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        //Three different algorithms
        //FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
        //FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
        FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

        try {
            ImageIO.write(bufferedImage, FilenameUtils.getExtension(fileName), new File("temp." + FilenameUtils.getExtension(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Mat img = imread("temp." + FilenameUtils.getExtension(fileName), CV_LOAD_IMAGE_GRAYSCALE);

        int label = Integer.parseInt(fileName.split("_")[0]);
        images.put(0, img);

        System.out.println("Image : " + img.toString());

        labelsBuf.put(0, label);

        System.out.println("Label : " + label);

        faceRecognizer.train(images, labels);

        System.out.println("Training is done!");

        faceRecognizer.save(trainingDir + "\\" + fileName.split("\\.")[0] + ".xml");

        long endTime = System.nanoTime();
        System.out.println("Add Execution time: " + (endTime - startTime));

        //delete temp file
        File tempDelete = new File("temp." + FilenameUtils.getExtension(fileName));
        tempDelete.delete();

        return bufferedImage;
    }

    public String findPersonId(MultipartFile testFile) {

        long startTime = System.nanoTime();

        File root = new File(trainingDir);

        FilenameFilter imgFilter = (dir, name) -> {
            name = name.toLowerCase();
            return name.endsWith(".xml");
        };

        BufferedImage temp = resize(getBufferedImageFromFile(testFile), width, height);

        try {
            ImageIO.write(temp, FilenameUtils.getExtension(testFile.getOriginalFilename()), new File("temp." + FilenameUtils.getExtension(testFile.getOriginalFilename())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Mat testImage = imread("temp." + FilenameUtils.getExtension(testFile.getOriginalFilename()), CV_LOAD_IMAGE_GRAYSCALE);

        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);

        File[] trainFiles = root.listFiles(imgFilter);

        Double min = Double.MAX_VALUE;
        String bestMatch = null;
        for (File file : trainFiles) {
            //Three different algorithms
            //FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
            //FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
            FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

            faceRecognizer.read(trainingDir + "\\" + file.getName());
            System.out.println(((LBPHFaceRecognizer) faceRecognizer).getLabels().getIntBuffer().get());
            faceRecognizer.predict(testImage, label, confidence);
            int predictedLabel = label.get(0);

            if (confidence.get() < min) {
                min = confidence.get();
                bestMatch = file.getName().split("\\.")[0];
            }
            System.out.println("Predicted label: " + predictedLabel + " Confidence: " + confidence.get());
        }

        System.out.println("Training is done! " + bestMatch);
        long endTime = System.nanoTime();
        System.out.println("Execution time: " + (endTime - startTime));

        //delete temp file
        File tempDelete = new File("temp." + FilenameUtils.getExtension(testFile.getOriginalFilename()));
        tempDelete.delete();

        return bestMatch;
    }

    private BufferedImage getBufferedImageFromFile(MultipartFile testFile) {
        BufferedImage bImageFromConvert= null;
        try {
            byte[] imageBytes = testFile.getBytes();
            InputStream in = new ByteArrayInputStream(imageBytes);
            bImageFromConvert = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bImageFromConvert;
    }

    private BufferedImage resize(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        //below three lines are for RenderingHints for better image quality at cost of higher processing time
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }

}
