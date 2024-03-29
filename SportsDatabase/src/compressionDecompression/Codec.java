package compressionDecompression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public abstract class Codec implements Compressible, Decompressible {
	
	public abstract File compressToTextFile(String str);
	public abstract String decompressToString();
	
	public byte[] compress(String data) throws IOException {
		byte[] input = data.getBytes();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Deflater deflater = new Deflater();
		try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater)) {
			deflaterOutputStream.write(input);
		}
		return byteArrayOutputStream.toByteArray();
	}

	public byte[] compress(File file) throws IOException {
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
	
    public String decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
        Inflater inflater = new Inflater();
        try (InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream, inflater);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inflaterInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toString();
        }
    }

    public String decompress(File compressedFile) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Inflater inflater = new Inflater();
        try (FileInputStream fis = new FileInputStream(compressedFile);
             InflaterInputStream iis = new InflaterInputStream(fis, inflater)) {
            int len;
            while ((len = iis.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        }
        return byteArrayOutputStream.toString();
    }
	
}
