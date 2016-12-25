package com.jansing.office;

import com.jansing.office.utils.ProcessUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jansing on 16-12-20.
 */
public class OfficeConverterTest {

    //只能执行一条，即只执行cp指令而没有执行soffice指令
    @Test
    public void test01() throws InterruptedException, IOException {
        Process process = ProcessUtil.exec(new String[]{"/bin/sh", "-c",
                "cp /home/jansing/wen/file/doc/1.docx /home/jansing/wen/file/doc/1_office.docx",
                "soffice --headless --convert-to pdf:writer_pdf_Export /home/jansing/wen/file/doc/1.docx --outdir /tmp"
        });
        process.waitFor();
    }

}
