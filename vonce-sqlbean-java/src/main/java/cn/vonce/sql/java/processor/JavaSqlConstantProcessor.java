package cn.vonce.sql.java.processor;

import cn.vonce.sql.processor.SqlConstantProcessor;
import cn.vonce.sql.uitls.JavaParserUtil;
import cn.vonce.sql.uitls.StringUtil;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * 生成表字段常量处理器
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/26 14:21
 */
@SupportedAnnotationTypes({"cn.vonce.sql.annotation.SqlTable"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class JavaSqlConstantProcessor extends SqlConstantProcessor {

    private List<FieldDeclaration> fieldDeclarationList = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        return super.process(annotations, env);
    }

    @Override
    public String getTableRemarks(Element element) {
        try {
            String classPath = new File("").getAbsolutePath();
            if (StringUtil.isNotBlank(classPath)) {
                String sourceRoot = classPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
                String javaFilePath = sourceRoot + ((PackageElement) element.getEnclosingElement()).getQualifiedName().toString().replace(".", File.separator) + File.separator + element.getSimpleName().toString() + ".java";
                if (new File(javaFilePath).exists()) {
                    JavaParserUtil.Declaration declaration = JavaParserUtil.getFieldDeclarationList(sourceRoot, javaFilePath);
                    TypeDeclaration<?> typeDeclaration = declaration.getTypeDeclaration();
                    fieldDeclarationList = declaration.getFieldDeclarationList();
                    if (typeDeclaration != null && typeDeclaration.getComment().isPresent()) {
                        return JavaParserUtil.getCommentContent(typeDeclaration.getComment().get().getContent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getFieldRemarks(String sqlFieldName) {
        return JavaParserUtil.getFieldCommentContent(sqlFieldName, fieldDeclarationList);
    }

}
