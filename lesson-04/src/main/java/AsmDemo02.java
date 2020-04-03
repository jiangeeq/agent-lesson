//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.FieldVisitor;
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.commons.AdviceAdapter;
//
//import java.io.File;
//
///**
// * @author jiangpeng
// * @date 2019/12/3116:03
// */
//public class AsmDemo02 extends AdviceAdapter {
//    private static String classPath = "D:\\learning\\lesson-04\\src\\main\\resources\\class-agent\\MyMain.class";
//
//    /**
//     * 移除方法和字段
//     * 删掉 abc 字段和 xyz 方法。
//     */
//    public void removeField(){
//        byte[] bytes = FileUtils.getBytesFromFile(classPath);
//        ClassReader cr = new ClassReader(bytes);
//        ClassWriter cw = new ClassWriter(0);
//        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
//            @Override
//            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
//                if ("abc".equals(name)) {
//                    return null;
//                }
//                return super.visitField(access, name, desc, signature, value);
//            }
//
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//                if ("xyz".equals(name)) {
//                    return null;
//                }
//                return super.visitMethod(access, name, desc, signature, exceptions);
//            }
//        };
//
//        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
//        byte[] bytesModified = cw.toByteArray();
//        FileUtils.writeByteArrayToFile(new File("./MyMain2.class"), bytesModified);
//    }
//
//    /**
//     * 修改方法内容
//     * 把方法体的返回值改为 a + 100。
//     */
//    public void modifyMethod(){
//        byte[] bytes = FileUtils.getBytesFromFile(classPath);
//        ClassReader cr = new ClassReader(bytes);
//        // 指定 ClassWriter 自动计算参数
//        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassVisitor cv = new ClassVisitor(ASM7, cw) {
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//
//                if ("foo".equals(name)) {
//                    // 删除 foo 方法
//                    return null;
//                }
//                return super.visitMethod(access, name, desc, signature, exceptions);
//            }
//
//            @Override
//            public void visitEnd() {
//                // 新增 foo 方法
//                MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "foo", "(I)I", null, null);
//
//                mv.visitCode();
//                mv.visitVarInsn(Opcodes.ILOAD, 1);
//                mv.visitIntInsn(Opcodes.BIPUSH, 100);
//                mv.visitInsn(Opcodes.IADD);
//                mv.visitInsn(Opcodes.IRETURN);
//                mv.visitEnd();
//            }
//        };
//        cr.accept(cv, 0);
//        byte[] bytesModified = cw.toByteArray();
//        FileUtils.writeByteArrayToFile(new File("./MyMain.class"), bytesModified);
//    }
//
//    /**
//     * AdviceAdapter 使用
//     */
//    public void ss(){
//        byte[] bytes = FileUtils.getBytesFromFile(classPath);
//        ClassReader cr = new ClassReader(bytes);
//        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassVisitor cv = new ClassVisitor(ASM7, cw) {
//            @Override
//            public MethodVisitor visitMethod(int access, final String name, String desc, String signature, String[] exceptions) {
//
//                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
//                if (!"foo".equals(name)) return mv;
//
//                return new AdviceAdapter(ASM7, mv, access, name, desc) {
//                    @Override
//                    protected void onMethodEnter() {
//                        // 新增 System.out.println("enter " +  name);
//                        super.onMethodEnter();
//                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                        mv.visitLdcInsn("enter " + name);
//                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//                    }
//
//                    @Override
//                    protected void onMethodExit(int opcode) {
//                        // 新增 System.out.println("[normal,err] exit " +  name);
//                        super.onMethodExit(opcode);
//                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                        if (opcode == Opcodes.ATHROW) {
//                            mv.visitLdcInsn("err exit " + name);
//                        } else {
//                            mv.visitLdcInsn("normal exit " + name);
//                        }
//                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//                    }
//                };
//            }
//        };
//        cr.accept(cv, 0);
//        byte[] bytesModified = cw.toByteArray();
//        FileUtils.writeByteArrayToFile(new File("./MyMain.class"), bytesModified);
//    }
//
//
//
//
//
//
//    /*-------------------------给方法加上 try catch---------------------------------*/
//    Label startLabel = new Label();
//
//    @Override
//    protected void onMethodEnter() {
//        super.onMethodEnter();
//        mv.visitLabel(startLabel);
//
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("enter " + name);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//    }
//
//    @Override
//    public void visitMaxs(int maxStack, int maxLocals) {
//        // 生成异常表
//        Label endLabel = new Label();
//        mv.visitTryCatchBlock(startLabel, endLabel, endLabel, null);
//        mv.visitLabel(endLabel);
//
//        // 生成异常处理代码块
//        finallyBlock(ATHROW);
//        mv.visitInsn(ATHROW);
//        super.visitMaxs(maxStack, maxLocals);
//    }
//
//    private void finallyBlock(int opcode) {
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        if (opcode == Opcodes.ATHROW) {
//            mv.visitLdcInsn("err exit " + name);
//        } else {
//            mv.visitLdcInsn("normal exit " + name);
//        }
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//    }
//
//    @Override
//    protected void onMethodExit(int opcode) {
//        super.onMethodExit(opcode);
//        // 处理正常返回的场景
//        if (opcode != ATHROW) finallyBlock(opcode);
//    }
//}
