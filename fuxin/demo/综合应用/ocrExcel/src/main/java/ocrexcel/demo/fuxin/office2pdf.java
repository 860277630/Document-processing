package ocrexcel.demo.fuxin;// Copyright (C) 2003-2022, Foxit Software Inc..


import com.foxit.sdk.PDFException;
import com.foxit.sdk.addon.conversion.Convert;
import com.foxit.sdk.common.Library;

import java.io.File;

import static com.foxit.sdk.common.Constants.*;

public class office2pdf {
    private static String key = "8f0YFcONvRkN+ldwlpAFW0NF+Q/jOhOBvj1efQAKLLKux+nGeHqbsdctYiw0TuZREd2ungiuZk2tsbLXEusM2l+cVxG18jYetvFudmoPiOHiDqfKr2MseAswJLWvtMr+i9zTHDYhDuZd9ESe2YJ6rQxX1i9OK/ahiUGyOcwQKMl4p9zkJ0PYiW6nJlf6YQ2uufXpn4jvJ8crJIPZ0GG561aEKjw5GmrEeaEgpnEsMXzDm3lY8gak9qCq4iHP7Rt/gLYgN81sXm+LiDjkBUJ9458JXwOJMR/CIVqLCceOOK/xVH9tKhWM38L+LZHDPcDKUPl5Q3z/bGam9xWQUjIGFcEzN/aMAsVXohDoX7yFSvGMTx7KXklqx/sJinOfQB2w3NZuQWBhC8FyaqxDbnN8/61jEc7waH/6JBjg2sovJUtAmIJYcTBJXBiaAFhpJ3sODTnVCLrkO90ZS2ujrdoLgoyQb/LizDjjuQL/pGWnp+KGc+OL5QrkU6CIEszPNeUfWzAFLHvIS0KJOkU+FhDh3woANn4KdNxih4fQnOhDSIYuFsf8bLdd2cmRf+c6Kh1Hlz4k5TprGgYHgeR7/fKVFzQU8ThnUgEgmIE69+2l5c89RYwnl8IcCNM9Zkqfs7Q/swaQ8qzSn/lzUH1s9lgLosIp+B9wiAHptqnLdel7nHq7k4CZbPZbGyk2vngYhmterOmM+7ErS3wTlQfr3Xq4INvYgenUbOmazGTHOQ6KMdWiX9a7GyuCVEDtLwiDtvwogj6Hz0L8FBu/wJ5YVG9O8R/4AvJ5vI0rHtPXQm2JijV1MqlUuSX5EXF3TTflhDGrxjyl0WTZePvcFIJgP26SwZhzAfAaLDz9qejSZakPSqFuUY0q201DB8tw4fyITj8hVIruR7IGvq+bnEGSuwI/zKSPNrtpEZPkvhUyitQlebK2AIF6Xc6vlXHnusb4D2N8YGjCTom1BSaNZGcVOmOD9cj6GPw4/uDaKffDfvFYChfN4U1wELZAsvkbY8Gl9oVFqRosOWkfm3kCg7q7+LwtUdgYFSVTqnFgO4zyNNC9Cje/aNrEL775/KVuZcWZTpEggxYuldML8qw8z6sHxO84/W4u/t9ZNBMGfp+g3RSEVGmE8iDRE2ehhIqo26injEF+d4f62BF5bjJfrzUZI6vbMkwUrj/0y5HF095aLx9nSGTYqcrP/4V53wgwBRnKCu76sHXGD+F+6FLzhN4PvMDaY+V7lYJj9J6fR0qbuGh9nokSbCWUMQMEjAyRCGhzQ28DP6ewi46MSVRzbZvdTolWnN/qsDs9Q593SE8YdswZxicGwsCZkCspArE=";
    private static String sn = "Izt0owtF5aH9zVuoR62u2FasDHg1UPvSg/JURm8gJMUB5Tb1eDgiUg==";
    private static String output_path = "src/main/java/com/example/demo/fuxin/filePackage/";

    // You can also use System.load("filename") instead. The filename argument must be an absolute path name.
    static {
        String os = System.getProperty("os.name").toLowerCase();
        String lib = "fsdk_java_";
        if (os.startsWith("win")) {
            lib += "win";
        } else if (os.startsWith("mac")) {
            lib += "mac";
        } else {
            lib += "linux";
      	}
        if (System.getProperty("sun.arch.data.model").equals("64")) {
            lib += "64";
        } else {
            lib += "32";
        }
        System.loadLibrary(lib);
    }

    private static void createResultFolder(String output_path) {
        File myPath = new File(output_path);
        if (!myPath.exists()) {
            boolean mkdir = myPath.mkdirs();
            System.out.println(mkdir);
        }
    }

    public static void main(String[] args) {
        // Initialize library
        int error_code = Library.initialize(sn, key);
        System.out.println(error_code);
        if (error_code != e_ErrSuccess) {
            if (e_ErrInvalidLicense == error_code)
                System.out.println("[Failed] Current used Foxit PDF SDK key information is invalid.");
            else
                System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        try {
            createResultFolder(output_path);
            {
                String pdf_file_path = "D:/PDF/pdf/1.pdf";
                String saved_xml_path = output_path + "pdf2xml_result.xml";
                boolean toXML = Convert.toXML(pdf_file_path, "", saved_xml_path, "", true);
                System.out.println(toXML);
            }
        } catch (PDFException e) {
            int last_error = e.getLastError();
            switch (last_error) {
                case e_ErrNoConversionModuleRight:
                    System.out.println("[Failed] Conversion module is not contained in current Foxit PDF SDK keys.");
                    break;
                case e_ErrNoMicroOfficeInstalled:
                    System.out.println("[Failed] No Microsoft Office is installed in current system, so fail to do conversion from Word/Excel/PowerPoint to PDF.");
                    break;
                default:
                    System.out.println(e.getMessage());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Library.release();
    }



}
