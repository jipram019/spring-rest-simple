package com.sample.java.handler;

import com.sample.java.controller.PDFGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class PDFHandler {
    public PDFHandler(){}

    public ClassPathResource generatePDF(String base64String){
        byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(base64String);

        // Save the file first
        FileOutputStream fileOutputStream = null;
        {
            try {
                fileOutputStream = new FileOutputStream("D:/GDN/java/target/classes/shipping_label.pdf");
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

        ClassPathResource pdfFile = new ClassPathResource("../../../../shipping_label.pdf", PDFGenerator.class);
        return pdfFile;
    }
}