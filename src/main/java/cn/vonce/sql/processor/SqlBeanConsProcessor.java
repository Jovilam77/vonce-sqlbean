package cn.vonce.sql.processor;

import cn.vonce.sql.annotation.SqlBeanField;
import cn.vonce.sql.annotation.SqlBeanTable;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

/**
 * 生成表字段常量处理器
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/26 14:21
 */
@SupportedAnnotationTypes({"cn.vonce.sql.annotation.SqlBeanCons"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SqlBeanConsProcessor extends AbstractProcessor {
    public static final String PREFIX = "Sql";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (TypeElement typeElement : annotations) {
            for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                //包裹注解元素的元素, 也就是其父元素, 比如注解了成员变量或者成员函数, 其上层就是该类
                Element enclosingElement = element.getEnclosingElement();
                //获取父元素的全类名,用来生成报名
                String packageName = ((PackageElement) enclosingElement).getQualifiedName().toString() + ".sql";
                String className = PREFIX + element.getSimpleName();
                String tableName = element.getSimpleName().toString();
                String tableAlias = "";
                SqlBeanTable sqlBeanTable = element.getAnnotation(SqlBeanTable.class);
                if (sqlBeanTable != null) {
                    tableName = sqlBeanTable.value();
                    tableAlias = sqlBeanTable.alias();
                }
                try {
                    //创建Java 文件
                    JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(className);
                    Writer writer = javaFileObject.openWriter();
                    try {
                        PrintWriter printWriter = new PrintWriter(writer);
                        printWriter.println("package " + packageName + ";");
                        printWriter.println("\npublic class " + className + " { ");
                        printWriter.println("    public static final String tableName = \"" + tableName + "\";");
                        printWriter.println("    public static final String tableAlias = \"" + tableAlias + "\";");
                        for (Element subElement : element.getEnclosedElements()) {
                            if (subElement.getKind().isField()) {
                                String sqlFieldName = subElement.getSimpleName().toString();
                                SqlBeanField sqlBeanField = subElement.getAnnotation(SqlBeanField.class);
                                if (sqlBeanField != null) {
                                    sqlFieldName = sqlBeanField.value();
                                }
                                printWriter.println("    public static final String " + sqlFieldName + " = \"" + sqlFieldName + "\";");
                            }
                        }
                        printWriter.println("}");
                        printWriter.flush();
                    } finally {
                        writer.close();
                    }
                } catch (IOException e1) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            e1.toString());
                }
            }
        }
        return true;
    }
}
