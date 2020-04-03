package pers.agent.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Objects;

/**
 * @author jiangpeng
 * @date 2019/12/2717:19
 */
public class JpClassFileTransformerDemo implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        System.out.println("loader className: " + className);

        if (!className.equalsIgnoreCase("pers/app/domain/Dog")) {
            return null;
        }

        return getBytesFromFile("D:\\learning\\lesson-01\\agent-01\\src\\main\\resources\\class-agent\\Dog.class");
    }

    private static byte[] getBytesFromFile(String filePath) {
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

    public static void main(String[] args) {
        getBytesFromFile("/class-agent/Dog.class");
    }
}
