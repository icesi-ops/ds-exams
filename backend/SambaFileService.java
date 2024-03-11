package com.distri.app;

import org.jcifs.smb.SmbFile;
import org.jcifs.smb.SmbFileOutputStream;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

public class SambaFileService {

    private static final String SAMBA_SERVER = "smb://127.0.0.1/storage_samba";
    private static final String USERNAME = "user_samba";
    private static final String PASSWORD = "pass123.";
    private static final String DOMAIN = "WORKGROUP";

    public static void main(String[] args) {
        Spark.port(4567);

        // Endpoint para listar archivos PDF
        Spark.get("/get_pdf_list", (request, response) -> {
            try {
                String pdfFolderPath = "/home/steb/distribuidos/storage_samba";
                File pdfFolder = new File(pdfFolderPath);
                String[] pdfList = pdfFolder.list((dir, name) -> name.endsWith(".pdf"));
                return Arrays.asList(pdfList);
            } catch (Exception e) {
                response.status(500);
                return "Error al obtener la lista de archivos PDF: " + e.getMessage();
            }
        }, json());

        // Endpoint para subir archivos
        Spark.post("/upload", (request, response) -> {
            try {
                String fileName = request.queryParams("file_name");
                InputStream fileStream = request.raw().getInputStream();
                uploadToSamba(fileStream, fileName);
                return "Archivo cargado y almacenado en Samba exitosamente";
            } catch (Exception e) {
                response.status(500);
                return "Error al cargar el archivo en Samba: " + e.getMessage();
            }
        });
    }

    private static void uploadToSamba(InputStream fileStream, String fileName) throws Exception {
        String sambaPath = SAMBA_SERVER + "/" + fileName;
        SmbFile smbFile = new SmbFile(sambaPath, getUsername(), getPassword());
        try (SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                smbFileOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private static String getUsername() {
        return USERNAME + ":" + PASSWORD;
    }

    private static String getPassword() {
        return DOMAIN;
    }

    private static Route json() {
        return (Request request, Response response) -> {
            response.type("application/json");
            return response.body();
        };
    }
}