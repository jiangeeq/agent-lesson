//import org.junit.Test;
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.FieldVisitor;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.tree.ClassNode;
//import org.objectweb.asm.tree.FieldNode;
//import org.objectweb.asm.tree.MethodNode;
//
//import java.io.File;
//import java.util.List;
//
//import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
//
///**
// * @author jiangpeng
// * @date 2019/12/3115:11
// */
//public class AsmDemo01 {
//    private static String classPath = "D:\\learning\\lesson-04\\src\\main\\resources\\class-agent\\MyMain.class";
//
//    public void getField() {
//        byte[] bytes = FileUtils.getBytesFromFile("");
//        ClassReader cr = new ClassReader(bytes);
//        ClassWriter cw = new ClassWriter(0);
//        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
//            @Override
//            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
//                System.out.println("field: " + name);
//                return super.visitField(access, name, desc, signature, value);
//            }
//
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String desc, String signature,
//                                             String[] exceptions) {
//                System.out.println("method: " + name);
//                return super.visitMethod(access, name, desc, signature, exceptions);
//            }
//        };
//        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
//    }
//
//    @Test
//    public static void getField_treeApi() {
//        byte[] bytes = FileUtils.getBytesFromFile(classPath);
//        ClassReader cr = new ClassReader(bytes);
//        ClassNode cn = new ClassNode();
//        cr.accept(cn, ClassReader.SKIP_DEBUG | ClassReader.SKIP_CODE);
//
//        List<FieldNode> fields = cn.fields;
//        for (int i = 0; i < fields.size(); i++) {
//            FieldNode fieldNode = fields.get(i);
//            System.out.println("field: " + fieldNode.name);
//        }
//        List<MethodNode> methods = cn.methods;
//        for (int i = 0; i < methods.size(); ++i) {
//            MethodNode method = methods.get(i);
//            System.out.println("method: " + method.name);
//        }
//        ClassWriter cw = new ClassWriter(0);
//        cr.accept(cn, 0);
//        byte[] bytesModified = cw.toByteArray();
//    }
//
//    @Test
//    public void addField() {
//        byte[] bytes = FileUtils.getBytesFromFile(classPath);
//        ClassReader cr = new ClassReader(bytes);
//        ClassWriter cw = new ClassWriter(0);
//        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
//            @Override
//            public void visitEnd() {
//                super.visitEnd();
//                FieldVisitor fv = cv.visitField(ACC_PUBLIC, "xyz", "Ljava/lang/String;", null, null);
//                if (fv != null) fv.visitEnd();
//            }
//        };
//        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
//        byte[] bytesModified = cw.toByteArray();
//        FileUtils.writeByteArrayToFile(new File("./MyMain2.class"), bytesModified);
//    }
//
//    public void addField_treeApi() {
//        byte[] bytes = FileUtils.getBytesFromFile(classPath);
//        ClassReader cr = new ClassReader(bytes);
//        ClassNode cn = new ClassNode();
//        cr.accept(cn, ClassReader.SKIP_DEBUG | ClassReader.SKIP_CODE);
//
//        FieldNode fn = new FieldNode(ACC_PUBLIC, "xyz", "Ljava/lang/String;", null, null);
//        cn.fields.add(fn);
//
//        ClassWriter cw = new ClassWriter(0);
//        cn.accept(cw);
//        byte[] bytesModified = cw.toByteArray();
//        FileUtils.writeByteArrayToFile(new File("./MyMain2.class"), bytesModified);
//    }
//
//    @Test
//    public void addMethod() {
//        byte[] bytes = FileUtils.getBytesFromFile(classPath);
//        ClassReader cr = new ClassReader(bytes);
//        ClassWriter cw = new ClassWriter(0);
//        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
//            @Override
//            public void visitEnd() {
//                super.visitEnd();
//                MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "xyz", "(ILjava/lang/String;)V", null, null);
//                if (mv != null) {mv.visitEnd();}
//            }
//        };
//        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
//        byte[] bytesModified = cw.toByteArray();
//        FileUtils.writeByteArrayToFile(new File("./MyMain2.class"), bytesModified);
//    }
//}
