package com.jansing.office.converters;

import com.jansing.office.utils.Collections3;
import com.jansing.office.utils.ProcessUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jansing on 16-12-17.
 */
public class LinuxOfficeConverter {
    private static final Logger logger = Logger.getLogger(LinuxOfficeConverter.class.getName());
    public static final String EXTENTION_PDF = ".pdf";

    public static File convertToPdf(File inputFile, String destinationPath) throws Exception {
        if (inputFile == null || !inputFile.exists()) {
            throw new FileNotFoundException("找不到要转换的文件: " + inputFile.getAbsolutePath());
        }
        File destinationDir = null;
        if (destinationPath == null) {
            throw new IllegalArgumentException("请选择一个目的路径");
        } else if (!(destinationDir = new File(destinationPath)).exists()) {
            destinationDir.mkdirs();
        } else if (!destinationDir.isDirectory()) {
            throw new NotDirectoryException("目的路径不是一个有效的路径:" + destinationPath);
        }
        long startTime = System.currentTimeMillis();
        Process process = ProcessUtil.exec(getCommand(inputFile.getAbsolutePath(), destinationPath));
        process.waitFor();
        long conversionTime = System.currentTimeMillis() - startTime;

        //转换结果
        List<String> results = IOUtils.readLines(process.getInputStream());
        if (results.get(0).startsWith("Error")) {
            throw new Exception(Collections3.convertToString(results, "\n") + "exit code: " + process.exitValue());
        }
        File destinationFile = new File(destinationPath + File.separator + FilenameUtils.getBaseName(inputFile.getName()) + EXTENTION_PDF);
        if (destinationFile.exists()) {
            logger.info(String.format("successful conversion: %s [%db] to %s in %dms", inputFile.getName(), inputFile.length(), EXTENTION_PDF, conversionTime));
            return destinationFile;
        }
        return null;
    }

    public static String[] getCommand(String inputFile, String outputDir) {
        return new String[]{"/bin/sh", "-c",
                "soffice --headless --convert-to pdf:writer_pdf_Export " + inputFile + " --outdir " + outputDir
        };
    }


}
