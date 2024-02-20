package compressionDecompression;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public interface Compressible {
    public static byte[] compress(String data) throws IOException {
        byte[] input = data.getBytes();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater)) {
            deflaterOutputStream.write(input);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] compress(File file) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        try (FileInputStream fis = new FileInputStream(file);
             DeflaterOutputStream dos = new DeflaterOutputStream(byteArrayOutputStream, deflater)) {
            int len;
            while ((len = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, len);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
