package cn.vonce.sql.processor;

import cn.vonce.sql.annotation.SqlColumn;
import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.uitls.JavaParserUtil;
import cn.vonce.sql.uitls.StringUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * 生成表字段常量处理器
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/26 14:21
 */
public class SqlConstantProcessor extends AbstractProcessor {
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
        try {
            for (TypeElement typeElement : annotations) {
                for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                    SqlTable sqlTable = element.getAnnotation(SqlTable.class);
                    if (sqlTable != null && !sqlTable.constant()) {
                        continue;
                    }
                    List<Element> subElementList = new ArrayList<>();
                    DeclaredType superClassDeclaredType = (DeclaredType) ((TypeElement) element).getSuperclass();
                    do {
                        if (!superClassDeclaredType.toString().equals("java.lang.Object")) {
                            for (Element e : superClassDeclaredType.asElement().getEnclosedElements()) {
                                if (e.getKind().isField() && !e.getModifiers().contains(Modifier.STATIC)) {
                                    subElementList.add(e);
                                }
                            }
                            superClassDeclaredType = (DeclaredType) ((TypeElement) superClassDeclaredType.asElement()).getSuperclass();
                        }
                        if (superClassDeclaredType.toString().equals("java.lang.Object")) {
                            break;
                        }
                    } while (!superClassDeclaredType.toString().equals("java.lang.Object"));
                    for (Element e : element.getEnclosedElements()) {
                        if (e.getKind().isField() && !e.getModifiers().contains(Modifier.STATIC)) {
                            subElementList.add(e);
                        }
                    }
                    Element enclosingElement = element.getEnclosingElement();
                    //获取父元素的全类名,用来生成包名
                    String packageName = ((PackageElement) enclosingElement).getQualifiedName().toString() + ".sql";
                    String className = element.getSimpleName().toString() + PREFIX;
                    try {
                        JavaFileObject fileObject = filer.createSourceFile(packageName + "." + className, enclosingElement);
                        Writer writer = fileObject.openWriter();
                        //写入java代码
                        writer.write(this.buildCode(element, subElementList, sqlTable, packageName, className).toString());
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return true;
    }

    private StringBuffer buildCode(Element element, List<Element> subElementList, SqlTable sqlTable, String packageName, String className) {
        StringBuffer code = new StringBuffer();
        try {
            //获取源码根目录
            URL url = getClass().getClassLoader().getResource("");
            String sourceRoot = url.getPath().substring(1, url.getPath().lastIndexOf("/target/classes/")) + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
            String javaFilePath = sourceRoot + ((PackageElement) element.getEnclosingElement()).getQualifiedName().toString().replace(".", File.separator) + File.separator + element.getSimpleName().toString() + ".java";
            //获取编译单元
            CompilationUnit compilationUnit = this.getCompilationUnit(javaFilePath);
            TypeDeclaration typeDeclaration = null;
            List<FieldDeclaration> fieldDeclarationList = null;
            if (compilationUnit != null && compilationUnit.getTypes() != null && compilationUnit.getTypes().size() > 0) {
                NodeList<TypeDeclaration<?>> typeDeclarations = compilationUnit.getTypes();
                typeDeclaration = typeDeclarations.get(0);
            }
            String schema = "";
            String tableName = element.getSimpleName().toString();
            String tableAlias = "";
            if (sqlTable != null) {
                schema = sqlTable.schema();
                tableName = sqlTable.value();
                tableAlias = sqlTable.alias();
            }
            if (StringUtil.isEmpty(tableAlias)) {
                tableAlias = tableName;
            }
            code.append("/** The code is generated by the Sqlbean. Do not modify!*/\n\n");
            code.append(String.format("package %s;\n\n", packageName));
            code.append("import cn.vonce.sql.bean.Column;\n\n");
            code.append(String.format("public class %s {\n\n", className));
            code.append(String.format("\tpublic static final String _schema = \"%s\";\n", schema));
            code.append(String.format("\tpublic static final String _tableName = \"%s\";\n", tableName));
            code.append(String.format("\tpublic static final String _tableAlias = \"%s\";\n", tableAlias));
            if (typeDeclaration != null) {
                code.append(String.format("\tpublic static final String _remarks = \"%s\";\n", JavaParserUtil.getCommentContent(typeDeclaration.getComment().get().getContent())));
                fieldDeclarationList = JavaParserUtil.getAllFieldDeclaration(sourceRoot, compilationUnit, typeDeclaration);
            }
            code.append(String.format("\tpublic static final String _all = \"%s.*\";\n", tableAlias));
            code.append("\tpublic static final String _count = \"COUNT(*)\";\n");

            //遍历所有字段
            for (Element subElement : subElementList) {
                SqlColumn sqlColumn = subElement.getAnnotation(SqlColumn.class);
                //不存在数据库的字段跳过
                if (sqlColumn != null && sqlColumn.ignore()) {
                    continue;
                }
                String sqlFieldName = subElement.getSimpleName().toString();
                String sqlFieldRemarks = JavaParserUtil.getFieldCommentContent(sqlFieldName, fieldDeclarationList);
                if (sqlColumn != null && StringUtil.isNotEmpty(sqlColumn.value())) {
                    sqlFieldName = sqlColumn.value();
                } else {
                    if (sqlTable != null && sqlTable.mapUsToCc()) {
                        sqlFieldName = StringUtil.humpToUnderline(sqlFieldName);
                    }
                }
                code.append(String.format("\tpublic static final String %s = \"%s\";\n", sqlFieldName, sqlFieldName));
                code.append(String.format("\tpublic static final Column %s$ = new Column(true,_tableAlias,%s,\"\",\"%s\");\n", sqlFieldName, sqlFieldName,sqlFieldRemarks));
            }

            code.append("\n}");
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        }
        return code;
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
