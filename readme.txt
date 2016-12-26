
课题：
  是基于该需求，还是抽象该需求
  是专注于解决问题，还是构造通用解决方案
要求：
  1、支持的浏览器（版本）
  2、文档格式doc/docx/xls/xlsx/ppt/pdf
    doc:图表、公式、smart图表
  3、展示效果
  4、并发量/性能要求
office
  https://msdn.microsoft.com/en-us/library/cc313105(v=office.12).aspx
  https://www.libreoffice.org/

pdf.js+openoffice/libreoffice+pyodconverter
  http://ruby-china.org/topics/17309
  pdf.js:基于h5的在浏览器显示pdf文档的脚本
  openoffice/libreoffice:用于加载文档内容
    libreoffice --invisible --convert-to pdf:writer_pdf_Export --outdir /home/me /home/me/test.doc
  pyodconverter:将加载的内容转成pdf



  限制：
    1、支持h5
    2、转成pdf再显示

FlexPaper

docviewer:
  office file -> pdf -> swf -> flash播放
jodconverter + tomcat
  依赖于 openoffice，还有 pdf2json 

crocodoc(business)
  office file -> html5
  pdf -> html5
Docsplit
  基于ruby openoffice，office file -> pdf
  
进度：
1120：测试pdf.js
1.5.188
https://github.com/mozilla/pdf.js/tree/0e2d50f8e67adce0b89a6e5b1deb0a3887df613d
chrome：清晰度、支持页签、支持加密(123456)、文本可复制、图片无错位、按需加载、顺畅放大到300%（500%明显卡顿）、密码
--pdf.js源码需要用node.js编译http://blog.csdn.net/yoie01/article/details/41279979
--sudo apt install npm
--npm install pdfjs-dist --save
从github拉下来的源码需用node.js编译，这里直接下载已编译的文件
https://github.com/mozilla/pdf.js/releases/download/v1.5.188/pdfjs-1.5.188-dist.zip
访问步骤：加载idea/pdf，访问http://localhost:8080/web/viewer.html?file=calrgb.pdf即展现calrgb.pdf文件
http://115.28.177.197:8009/pdf/web/viewer.html?file=test/1.pdf

1128：已测试pdf.js，现考虑如何显示word/excel/ppt
http://www.th7.cn/Program/java/201604/845936.shtml：office文件=>pdf文件
	libreoffice maybe need 5.3
	jodconverter.jar github：https://github.com/mirkonasato/jodconverter
1、启动libreoffice接口，提供转换服务：soffice --headless --accept="socket,host=127.0.0.1,port=2002;urp;" --nofirststartwizard
2、测试jodconvert/libreoffice_example
	org.hyperic.sigar.SigarException: no libsigar-amd64-linux.so in java.library.path
弃用jodconverter，因为soffice启动失败，报IllegalStateException时会突然重启系统。。。改用下面这句指令，无需启用soffice服务。
soffice --headless --convert-to pdf:writer_pdf_Export  /home/jansing/wen/file/doc/1.docx  --outdir  /tmp/
--http://www.artofsolving.com/opensource/jodconverter
http://api.libreoffice.org/examples/examples.html
http://api.libreoffice.org/examples/java/DocumentHandling/
pdf.js:viewer.html->viewer.jsp完成，其中修改的地方用“// modify by jansing”标记

libreoffice : http://api.libreoffice.org/
libreoffice 5.3 sdk java api reference : http://api.libreoffice.org/docs/java/ref/index.html
libreoffice example: http://api.libreoffice.org/examples/examples.html#Java_examples
libreoffice additional examples: https://gerrit.libreoffice.org/gitweb?p=sdk-examples.git;a=blob;f=README;hb=HEAD
Linux下使用libreoffice把doc转换成Pdf	http://blog.csdn.net/pwtitle/article/details/51684685
jodconvert 默认使用 openoffice，对比libreoffice 与openoffice
pdf.js 试试用  jquery.media.js 代替
	





https://technet.microsoft.com/en-us/windows-server-docs/essentials/get-started/get-started
https://technet.microsoft.com/zh-cn/library/jj219456%28v=office.15%29.aspx?f=255&MSPPError=-2147217396



windows2008:
DC服务器：ETOP.COM
15、选择“添加新林”，填写根域名：ETOP.COM 。
林和域功能级别是windows server 2012
活动目录还原密码e-top938ADServer
NetBIOS域名是MCITP
ADDS数据库：C:\WINDOWS\NTDS
ADDS日志：C:\WINDOWS\NTDS
SYSVOL：C:\WINDOWS\NTDS


owa的访问url
redirect:http://officewebapps.etop.com/x/_layouts/xlviewerinternal.aspx?WOPISrc=http%3A%2F%2F192.168.1.106%3A8080%2FtestWOPI-1.0-SNAPSHOT%2Fwopi%2Ffiles%2Fxls
redirect:http://officewebapps.etop.com/wv/wordviewerframe.aspx?WOPISrc=http%3A%2F%2F192.168.1.106%3A8080%2FtestWOPI-1.0-SNAPSHOT%2Fwopi%2Ffiles%2Fdoc
redirect:http://officewebapps.etop.com/wv/wordviewerframe.aspx?WOPISrc=http%3A%2F%2F192.168.1.106%3A8080%2FtestWOPI-1.0-SNAPSHOT%2Fwopi%2Ffiles%2Fdocx
redirect:http://officewebapps.etop.com/wv/wordviewerframe.aspx?PdfMode=1&WOPISrc=http%3A%2F%2F192.168.1.106%3A8080%2FtestWOPI-1.0-SNAPSHOT%2Fwopi%2Ffiles%2Fpdf
redirect:http://officewebapps.etop.com/wv/wordviewerframe.aspx?PdfMode=1&WOPISrc=http%3A%2F%2F192.168.1.106%3A8080%2FtestWOPI-1.0-SNAPSHOT%2Fwopi%2Ffiles%2Fpdf2
redirect:http://officewebapps.etop.com/p/PowerPointFrame.aspx?PowerPointView=ReadingView&WOPISrc=http%3A%2F%2F192.168.1.106%3A8080%2FtestWOPI-1.0-SNAPSHOT%2Fwopi%2Ffiles%2Fppt
redirect:http://officewebapps.etop.com/p/PowerPointFrame.aspx?PowerPointView=ReadingView&WOPISrc=http%3A%2F%2F192.168.1.106%3A8080%2FtestWOPI-1.0-SNAPSHOT%2Fwopi%2Ffiles%2Fpptx



docker:jansing@gyh2hx.xyz,jan
http://www.docker.org.cn/page/resources.html
https://hub.docker.com/
https://hub.docker.com/r/microsoft/windowsservercore/
https://docs.microsoft.com/zh-cn/virtualization/windowscontainers/quick_start/quick_start_windows_server


windowserver2016key:NCPR7-K6YH2-BRXYM-QMPPQ-3PF6X
