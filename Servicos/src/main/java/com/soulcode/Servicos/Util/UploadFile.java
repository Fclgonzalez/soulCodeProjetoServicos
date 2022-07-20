package com.soulcode.Servicos.Util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UploadFile {

    public static void saveFile (String uploadDir, String fileName, MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDir); // faz a leitura do caminho do arquivo

        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // cria o caminho se ele não existe
        }

        // tenta fazer upload do arquivo
        // inputStream  tenta fazer a leitura do aquico que estamos querendo subir, faz a leitura byte por byte
        try(InputStream inputStream = file.getInputStream()) {

            // neste momento o arquivo é salvo no diretório que passamos na assinatura do método
            Path filePath = uploadPath.resolve(fileName);
            // neste momento fazemos a cópia do arquivo
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new IOException("Não foi possível enviar seu arquivo");
        }
    }
}
