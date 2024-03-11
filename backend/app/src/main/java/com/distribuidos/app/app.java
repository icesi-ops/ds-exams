package com.distribuidos.app;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import spark.Request;
import spark.Response;
import spark.Spark;
import com.google.gson.Gson;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class app {
    private static final String SAMBA_SERVER = "smb://172.18.0.2/storage_samba";
    private static final String USERNAME = "user_samba";
    private static final String PASSWORD = "pass123.";
    private static final String DOMAIN = "WORKGROUP";
    private static final Gson gson = new Gson();
    public static void main(String[] args) {
        Spark.port(4567);
        Spark.externalStaticFileLocation("/public");
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
            response.type("application/json");
        });
        Spark.get("/get_file_list", (request, response) -> {
            try {
                String folderPath = "/home/steb/distribuidos/data"; // Ruta del directorio
                File folder = new File(folderPath);
                String[] fileList = folder.list(); // Lista todos los archivos
                if (fileList != null) {
                    return gson.toJson(Arrays.asList(fileList)); // Convierte la lista a JSON
                } else {
                    return gson.toJson(new ArrayList<>()); // Devuelve una lista vacía si el directorio no tiene archivos
                }
            } catch (Exception e) {
                response.status(500);
                return "Error al obtener la lista de archivos: " + e.getMessage();
            }
        });

        Spark.post("/upload", (request, response) -> {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try {
                Part filePart = request.raw().getPart("file");
                String fileName = getFileName(filePart);
                InputStream fileStream = filePart.getInputStream();
                uploadToSamba(fileStream, fileName);
                return "Archivo cargado y almacenado en Samba exitosamente";
            } catch (Exception e) {
                response.status(500);
                return "Error al cargar el archivo en Samba: " + e.getMessage();
            }
        });
    }
    private static void uploadToSamba(InputStream fileStream, String fileName) throws Exception {
        String authString = USERNAME + ":" + PASSWORD + "@";
        String sambaUrl = "smb://" + authString + SAMBA_SERVER.substring(6) + "/" + fileName;
        System.out.println("Samba URL: " + sambaUrl);
        SmbFile smbFile = new SmbFile(sambaUrl);
        try (SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                smbFileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (jcifs.smb.SmbException e) {
            System.err.println("Error al conectar con el servidor Samba: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error al cargar el archivo en Samba: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
