package cn.vonce.sql.compile.util;

import com.esotericsoftware.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/9/20 20:26
 */
import static com.esotericsoftware.asm.Opcodes.ACC_PRIVATE;
import static com.esotericsoftware.asm.Opcodes.ACC_PUBLIC;
import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

public class MethodModifier extends ClassVisitor {

    public MethodModifier(ClassVisitor classVisitor) {
        super(ASM5, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        // 用一个新的 MethodVisitor 包装原来的 MethodVisitor
        return new MethodVisitor(ASM5, mv) {
            private boolean isAnnotatedWithNotSupportAndroid = false;

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                // 检查方法是否有 @NotSupportAndroid 注解
                if ("Lyour/package/NotSupportAndroid;".equals(descriptor)) {
                    isAnnotatedWithNotSupportAndroid = true;
                }
                return super.visitAnnotation(descriptor, visible);
            }

            @Override
            public void visitEnd() {
                if (isAnnotatedWithNotSupportAndroid) {
                    // 打印出找到的注解信息
                    System.out.println("Method " + name + " is annotated with @NotSupportAndroid");

                    // 将方法修改为私有方法
                    int newAccess = (access & ~ACC_PUBLIC) | ACC_PRIVATE;
                    MethodVisitor modifiedMethodVisitor = MethodModifier.super.visitMethod(newAccess, name, descriptor, signature, exceptions);

                    // 将方法体重新写入
                    mv = modifiedMethodVisitor;
                }
                super.visitEnd();
            }
        };
    }

    public static byte[] modifyClass(byte[] classBytes) {
        ClassReader classReader = new ClassReader(classBytes);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        // 修改类
        ClassVisitor classVisitor = new MethodModifier(classWriter);
        classReader.accept(classVisitor, 0);

        return classWriter.toByteArray();
    }

    // 保存修改后的 .class 文件
    public static void saveClassToFile(String filePath, byte[] classBytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(classBytes);
        }
    }

    public static void main(String[] args) throws Exception {
        // 读取User.class字节码
        byte[] classBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("E:\\IdeaProjects\\vonce-sqlbean\\vonce-sqlbean-core\\target\\classes\\cn\\vonce\\sql\\bean\\Select.class"));

        // 修改带有 @NotSupportAndroid 注解的方法的访问权限
        byte[] modifiedClassBytes = modifyClass(classBytes);

        // 保存修改后的 .class 文件
        saveClassToFile("E:\\IdeaProjects\\vonce-sqlbean\\vonce-sqlbean-core\\target\\classes\\cn\\vonce\\sql\\bean\\Select.class", modifiedClassBytes);
    }
}