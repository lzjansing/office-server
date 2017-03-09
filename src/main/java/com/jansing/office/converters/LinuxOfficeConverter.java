package com.jansing.office.converters;

import com.jansing.common.utils.Collections3;
import com.jansing.common.utils.Message;
import com.jansing.office.utils.ProcessUtil;
import com.jansing.web.utils.FileTransmitUtil;
import com.jansing.web.utils.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;
import java.util.List;

/**
 * Created by jansing on 16-12-17.
 */
public class LinuxOfficeConverter {
    private static final Logger logger = LoggerFactory.getLogger(LinuxOfficeConverter.class);
    public static final String EXTENSION_HTML = ".html";
    public static final String EXTENSION_PDF = ".pdf";
    public static final String EXTENSION_XLS = ".xls";
    public static final String EXTENSION_XLSX = ".xlsx";

    public static File convertToPdf(File inputFile, String destinationPath) throws Exception {
        checkout(inputFile, destinationPath);

        Message message = new Message();
        File destinationFile = null;
        long startTime = System.currentTimeMillis();
        if (inputFile.getName().endsWith(EXTENSION_PDF)) {
            //本身就是pdf文档，无需转换
            try {
                if (!inputFile.equals(new File(destinationPath + File.separator + inputFile.getName()))) {
                    destinationFile = FileUtil.copy(inputFile, destinationPath, inputFile.getName());
                } else {
                    destinationFile = inputFile;
                }
                message.setCode(Message.SUCCESS);
            } catch (Exception e) {
                message.setCode(Message.FAIL);
                message.setMessage(e.getMessage());
            }
        } else {
            Process process = ProcessUtil.exec(getCommand(inputFile.getAbsolutePath(), destinationPath));
            process.waitFor();

            List<String> results = IOUtils.readLines(process.getInputStream());
            if (results.get(0).startsWith("Error")) {
                message.setCode(Message.FAIL);
                message.setMessage(Collections3.convertToString(results, "\n") + "exit code: " + process.exitValue());
            } else {
                message.setCode(Message.SUCCESS);
                destinationFile = new File(destinationPath + File.separator
                        + FilenameUtils.getBaseName(inputFile.getName())
                        + (isXls(inputFile.getName()) ? EXTENSION_HTML : EXTENSION_PDF));
            }
        }
        long conversionTime = System.currentTimeMillis() - startTime;

        if (Message.FAIL.equals(message.getCode()) || destinationFile == null || !destinationFile.exists()) {
            logger.error(String.format("failed conversion: [%s] - %s", inputFile.getName(), message.getMessage()));
            throw new Exception(message.getMessage());
        } else {
            logger.info(String.format("successful conversion: %s [%db] to %s in %dms", inputFile.getName(), inputFile.length(), EXTENSION_PDF, conversionTime));
            return destinationFile;
        }
    }

    private static void checkout(File inputFile, String destinationPath) throws FileNotFoundException, NotDirectoryException {
        if (!FileTransmitUtil.askFileExist(inputFile)) {
            throw new FileNotFoundException("找不到要转换的文件: " + inputFile.getAbsolutePath());
        }
        if (destinationPath == null) {
            throw new IllegalArgumentException("请选择一个目的路径");
        }
        FileUtil.mkdirsIfNotExisted(destinationPath);
    }

    public static String[] getCommand(String inputFile, String outputDir) {
        String convertTo = " pdf:writer_pdf_Export ";
        if (isXls(inputFile)) {
            convertTo = " html ";
        }
        return new String[]{"/bin/sh", "-c",
                "soffice --headless --convert-to " + convertTo + inputFile + " --outdir " + outputDir
        };
    }

    public static boolean isXls(String fileName) {
        if (fileName != null) {
            return fileName.endsWith(EXTENSION_XLSX) || fileName.endsWith(EXTENSION_XLS);
        }
        return false;
    }


}
