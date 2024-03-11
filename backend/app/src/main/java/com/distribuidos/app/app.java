package com.distribuidos.app;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import spark.Request;
import spark.Response;
import spark.Spark;
import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

public class app {

    private static final String SAMBA_SERVER = "smb://172.18.0.2/storage_samba";
    private static final String USERNAME = "user_samba";
    private static final String PASSWORD = "pass123.";
    private static final String DOMAIN = "WORKGROUP";
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Spark.port(4567);

        // Endpoint para listar archivos PDF
        Spark.get("/get_pdf_list", (request, response) -> {
            try {
                String pdfFolderPath = "/home/steb/distribuidos/data";
                File pdfFolder = new File(pdfFolderPath);
                String[] pdfList = pdfFolder.list((dir, name) -> name.endsWith(".pdf"));
                return gson.toJson(Arrays.asList(pdfList));
            } catch (Exception e) {
                response.status(500);
                return "Error al obtener la lista de archivos PDF: " + e.getMessage();
            }
        });

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
        // Construye la URL con la información de autenticación
        String sambaUrl = SAMBA_SERVER + "/" + fileName;
        String authString = DOMAIN + ";" + USERNAME + ":" + PASSWORD + "@";
        String sambaPath = "smb://" + authString + sambaUrl;
    
        // Crea un SmbFile usando la URL
        SmbFile smbFile = new SmbFile(sambaPath);
        try (SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                smbFileOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }
    


    private static String getUsername() {
        return USERNAME;
    }

    private static String getPassword() {
        return PASSWORD;
    }
}
