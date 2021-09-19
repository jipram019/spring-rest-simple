package com.sample.java.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class PDFHandler {
    private final static String FILE_PATH = "D:/Works/shipping_label.pdf";
    public PDFHandler(){}

    public byte[] generateAndSavePDFFile(String base64String){
        //byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(base64String);
        byte[] decoded = Base64Utils.decodeFromString(base64String);

        if (decoded != null) {
            // Save the file first
            FileOutputStream fileOutputStream = null;
            {
                try {
                    fileOutputStream = new FileOutputStream(FILE_PATH);
                } catch (FileNotFoundException e) {
                    log.error("File not found", e);
                }
            }

            try {
                fileOutputStream.write(decoded);
                fileOutputStream.flush();
            } catch (IOException e) {
                log.error("Error while writing file", e);
            }

            finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("Error while closing output stream", e);
                }
            }
        }

        return decoded;
    }
}