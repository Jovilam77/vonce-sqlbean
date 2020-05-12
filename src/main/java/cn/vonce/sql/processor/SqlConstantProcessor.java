package cn.vonce.sql.processor;

import cn.vonce.common.utils.StringUtil;
import cn.vonce.sql.annotation.SqlColumn;
import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.Column;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.*;
import java.util.Set;

/**
 * 生成表字段常量处理器
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/26 14:21
 */
@SupportedAnnotationTypes({"cn.vonce.sql.annotation.SqlConstant"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SqlConstantProcessor extends AbstractProcessor {
    private Messager messager; //有点像Logger,用于输出信息
    private Filer filer; //可以获得Build Path，用于生成文件
    public static final String PREFIX = "Sql";

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
                    Element enclosingElement = element.getEnclosingElement();
                    //获取父元素的全类名,用来生成包名
                    String packageName = ((PackageElement) enclosingElement).getQualifiedName().toString() + ".sql";
                    String className = PREFIX + element.getSimpleName();
                    String schema = "";
                    String tableName = element.getSimpleName().toString();
                    String tableAlias = "";
                    SqlTable sqlTable = element.getAnnotation(SqlTable.class);
                    if (sqlTable != null) {
                        schema = sqlTable.schema();
                        tableName = sqlTable.value();
                        tableAlias = sqlTable.alias();
                    }
                    if (StringUtil.isEmpty(tableAlias)) {
                        tableAlias = tableName;
                    }
                    FieldSpec _schemaField = FieldSpec.builder(String.class, "_schema")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", schema)
                            .build();
                    FieldSpec _tableNameField = FieldSpec.builder(String.class, "_tableName")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", tableName)
                            .build();
                    FieldSpec _tableAliasField = FieldSpec.builder(String.class, "_tableAlias")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", tableAlias)
                            .build();
                    FieldSpec _allField = FieldSpec.builder(String.class, "_all")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", tableAlias + ".*")
                            .build();
                    FieldSpec _countField = FieldSpec.builder(String.class, "_count")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", "COUNT(*)")
                            .build();
                    TypeSpec.Builder sqlBeanConsBuilder = TypeSpec.classBuilder(className)
                            .addModifiers(Modifier.PUBLIC)
                            .addField(_schemaField)
                            .addField(_tableNameField)
                            .addField(_tableAliasField)
                            .addField(_allField)
                            .addField(_countField);
                    for (Element subElement : element.getEnclosedElements()) {
                        if (subElement.getKind().isField() && !subElement.getModifiers().contains(Modifier.STATIC)) {
                            String sqlFieldName = subElement.getSimpleName().toString();
                            SqlColumn sqlColumn = subElement.getAnnotation(SqlColumn.class);
                            if (sqlColumn != null && StringUtil.isNotEmpty(sqlColumn.value())) {
                                sqlFieldName = sqlColumn.value();
                            }
                            FieldSpec sqlField = FieldSpec.builder(Column.class, sqlFieldName)
                                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                    .initializer(CodeBlock.builder().add("new $T($L,$L,$S,\"\")", Column.class, "_schema", "_tableAlias", sqlFieldName).build())
                                    .build();
                            sqlBeanConsBuilder.addField(sqlField);
                        }
                    }
                    JavaFile javaFile = JavaFile.builder(packageName, sqlBeanConsBuilder.build())
                            .addFileComment(" This codes are generated automatically. Do not modify!")
                            .build();
                    try {
                        javaFile.writeTo(filer);
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

}
