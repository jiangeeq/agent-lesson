import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author jiangpeng
 * @date 2019/12/3115:20
 */
public class FileUtils {
    public static byte[] getBytesFromFile(String filePath) {
        try (InputStream is = new FileInputStream(new File(filePath))) {

            byte[] bytes = new byte[is.available()];
            // Read in the bytes
            int offset = 0;
            int numRead;
            while (offset < bytes.length
                    && (numRead = Objects.requireNonNull(is).read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + filePath);
            }
            System.out.println("bytes size: " + bytes.length);
            return bytes;
        } catch (Exception e) {
            System.out.println("error occurs in _ClassTransformer!"
                    + e.getClass().getName());
            return null;
        }
    }

    public static void s9() throws IOException {
        try (InputStream resourceAsStream = FileUtils.class.getResourceAsStream("/class-agent/MyMain.class");
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            while (resourceAsStream.read(buffer) != -1) {
                out.write(buffer);
            }
            final byte[] data = out.toByteArray();
            System.out.println(data.length);
        }
    }

    public static void s23() throws IOException {
        try (InputStream resourceAsStream = FileUtils.class.getResourceAsStream("/class-agent/MyMain.class")) {
            byte[] buffer = new byte[1024];
            byte[] data = new byte[resourceAsStream.available()];
            int i = 0;
            int len;
            while ((len = resourceAsStream.read(buffer)) != -1) {
                System.arraycopy(buffer, 0, data, i, len);
                i += len;
            }
            System.out.println(data.length);

        }
    }
}
