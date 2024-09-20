package cn.vonce.sql.compile.processor;

import cn.vonce.sql.compile.annotation.AndroidCope;
import cn.vonce.sql.compile.annotation.NotSupportAndroid;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/9/20 14:24
 */
@SupportedAnnotationTypes({"cn.vonce.sql.compile.annotation.AndroidCope"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AndroidProcessor extends AbstractProcessor {
    private Messager messager; //有点像Logger,用于输出信息
    private Filer filer; //可以获得Build Path，用于生成文件
    public static final String PREFIX = "$";

    // init做一些初始化操作
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        messager.printMessage(Diagnostic.Kind.NOTE, "AndroidProcessor process!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("1!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        try {
            for (TypeElement typeElement : annotations) {
                for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                    AndroidCope androidCope = element.getAnnotation(AndroidCope.class);
                    System.out.println("2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + androidCope);
                    if (androidCope == null) {
                        continue;
                    }
                    Element enclosingElement = element.getEnclosingElement();
                    //获取父元素的全类名,用来生成包名
                    String packageName = ((PackageElement) enclosingElement).getQualifiedName().toString();
                    String className = element.getSimpleName().toString();
                    try {
                        System.out.println("3################################################");
                        String code = buildCode(element);
                        System.out.println(code);
                        // 创建文件之前，检查是否已存在
//                        FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, packageName, className);
//                        System.out.println(existingFile);
//                        if (existingFile != null) {
//                            System.out.println(existingFile.getName());
//                            // 文件存在，可以选择覆盖或者抛出异常
//                            System.out.println(existingFile.delete());
//                            File file = new File(existingFile.toUri());
//                            System.out.println(file.delete());
//                        }
                        JavaFileObject fileObject = filer.createClassFile(packageName + "." + className, enclosingElement);
                        Writer writer = fileObject.openWriter();
                        //写入java代码
                        writer.write(code);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("4@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return true;
    }

    private String buildCode(Element element) {
        try {
            //获取源码根目录
            URL url = getClass().getClassLoader().getResource("");
            String sourceRoot = url.getPath().substring(1, url.getPath().lastIndexOf("/target/classes/")) + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
            String javaFilePath = sourceRoot + ((PackageElement) element.getEnclosingElement()).getQualifiedName().toString().replace(".", File.separator) + File.separator + element.getSimpleName().toString() + ".java";
            //获取编译单元
            CompilationUnit compilationUnit = this.getCompilationUnit(javaFilePath);
            TypeDeclaration typeDeclaration = null;
            if (compilationUnit != null && compilationUnit.getTypes() != null && compilationUnit.getTypes().size() > 0) {
                NodeList<TypeDeclaration<?>> typeDeclarations = compilationUnit.getTypes();
                typeDeclaration = typeDeclarations.get(0);
            }
            if (typeDeclaration != null) {
//                typeDeclaration.setName(typeDeclaration.getNameAsString() + "2");
                List<ConstructorDeclaration> constructorDeclarationList = typeDeclaration.getConstructors();
                System.out.println(constructorDeclarationList + ".................");
                constructorDeclarationList.get(0).setName(constructorDeclarationList.get(0).getNameAsString() + "2");
                List<MethodDeclaration> methodDeclarationList = typeDeclaration.getMethods();
                for (MethodDeclaration methodDeclaration : methodDeclarationList) {
                    Optional<AnnotationExpr> notSupportAndroidAnnotation = methodDeclaration.getAnnotationByClass(NotSupportAndroid.class);
                    if (notSupportAndroidAnnotation.isPresent()) {
                        methodDeclaration.remove();
                    }
                }
                compilationUnit.getImport(0).remove();
                compilationUnit.getImport(0).remove();
                Optional<AnnotationExpr> androidCopeAnnotation = typeDeclaration.getAnnotationByClass(AndroidCope.class);
                if (androidCopeAnnotation.isPresent()) {
                    typeDeclaration.remove(androidCopeAnnotation.get());
                }
            }
            return compilationUnit.toString();
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        }
        return null;
    }

    private CompilationUnit getCompilationUnit(String javaFilePath) throws FileNotFoundException {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> result = javaParser.parse(new File(javaFilePath));
        Optional<CompilationUnit> compilationUnitOptional = result.getResult();
        if (compilationUnitOptional.isPresent()) {
            return compilationUnitOptional.get();
        }
        return null;
    }

}
