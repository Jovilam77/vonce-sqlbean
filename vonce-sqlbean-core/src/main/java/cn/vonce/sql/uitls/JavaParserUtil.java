package cn.vonce.sql.uitls;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Java解析工具
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/7/15 12:05
 */
public class JavaParserUtil {

    public static String getCommentContent(String rawComment) {
        String[] texts = getCommentTexts(rawComment);
        if (texts.length == 1) {
            return texts[0];
        }
        texts = Arrays.stream(texts).filter(text -> text.charAt(0) != '@').toArray(String[]::new);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < texts.length; i++) {
            String text = texts[i];
            content.append(text);
            if (i < texts.length - 1) {
                content.append(" ");
            }
        }
        return content.toString();
    }

    public static String[] getCommentTexts(String rawComment) {
        if (StringUtil.isEmpty(rawComment)) {
            return new String[]{""};
        }
        rawComment = rawComment.replaceAll("\\*", "").replaceAll("\\/", "").replaceAll("\\r", "").trim();
        String[] texts = rawComment.split("\\n");
        return Arrays.stream(texts).filter(text -> !StringUtil.isEmpty(text.trim())).map(text -> text.trim().replace("\"", "\\\"")).toArray(String[]::new);
    }

    public static String getFieldCommentContent(FieldDeclaration fieldDeclaration) {
        if (fieldDeclaration == null) {
            return "";
        }
        Optional<Comment> fieldComment = fieldDeclaration.getComment();
        if (fieldComment.isPresent()) {
            return getCommentContent(fieldComment.get().getContent());
        }
        return "";
    }

    public static String getFieldCommentContent(String fieldName, List<FieldDeclaration> fieldDeclarationList) {
        return getFieldCommentContent(getFieldDeclaration(fieldName, fieldDeclarationList));
    }

    public static FieldDeclaration getFieldDeclaration(String fieldName, List<FieldDeclaration> fieldDeclarationList) {
        if (StringUtil.isEmpty(fieldName) || fieldDeclarationList == null || fieldDeclarationList.size() == 0) {
            return null;
        }
        for (FieldDeclaration fieldDeclaration : fieldDeclarationList) {
            if (fieldDeclaration.getVariables().get(0).getNameAsString().equals(fieldName)) {
                return fieldDeclaration;
            }
        }
        return null;
    }

    public static List<FieldDeclaration> getAllFieldDeclaration(String sourceRoot, CompilationUnit compilationUnit, TypeDeclaration typeDeclaration) {
        List<FieldDeclaration> fieldDeclarationList = new ArrayList<>();
        fieldDeclarationList.addAll(typeDeclaration.getFields());
        String superClassName = null;
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        if (classOrInterfaceDeclarationList != null && classOrInterfaceDeclarationList.size() > 0) {
            NodeList<ClassOrInterfaceType> classOrInterfaceTypeNodeList = classOrInterfaceDeclarationList.get(0).getExtendedTypes();
            if (classOrInterfaceTypeNodeList != null && classOrInterfaceTypeNodeList.size() > 0) {
                superClassName = classOrInterfaceTypeNodeList.get(0).getNameAsString();
            }
        }
        if (StringUtil.isNotEmpty(superClassName) && !"Object".equals(superClassName)) {
            Optional<PackageDeclaration> packageDeclaration = compilationUnit.getPackageDeclaration();
            if (!packageDeclaration.isPresent()) {
                return fieldDeclarationList;
            }
            String superClassPath = getSuperClassPath(sourceRoot, packageDeclaration.get().getNameAsString(), superClassName, compilationUnit.getImports());
            try {
                JavaParser javaParser = new JavaParser();
                ParseResult<CompilationUnit> result = javaParser.parse(new File(sourceRoot + superClassPath));
                if (result == null || !result.isSuccessful() || !result.getResult().isPresent()) {
                    return null;
                }
                CompilationUnit superCompilationUnit = result.getResult().get();
                fieldDeclarationList.addAll(getAllFieldDeclaration(sourceRoot, superCompilationUnit, superCompilationUnit.getTypes().get(0)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fieldDeclarationList;
    }

    public static String getSuperClassPath(String sourceRoot, String packageName, String superClassName, List<ImportDeclaration> importDeclarationList) {
        if (superClassName != null) {
            if (importDeclarationList != null && importDeclarationList.size() > 0) {
                final List<String> importAsteriskList = new ArrayList<>();
                for (ImportDeclaration importDeclaration : importDeclarationList) {
                    String fullName = importDeclaration.getNameAsString();
                    //如果父类就存在于导包的路径中
                    if (!importDeclaration.isAsterisk() && fullName.endsWith("." + superClassName)) {
                        importAsteriskList.clear();
                        return fullName.replace(".", File.separator) + ".java";
                    }
                    //如果是导入全部的
                    if (importDeclaration.isAsterisk()) {
                        importAsteriskList.add(fullName);
                    }
                }
                //从导入的全体包名中查找
                for (String asterisk : importAsteriskList) {
                    String packPath = asterisk.replace(".", File.separator);
                    File directory = new File(sourceRoot + packPath);
                    if (directory != null && directory.exists() && directory.isDirectory()) {
                        for (File file : directory.listFiles()) {
                            if (file.exists() && file.isFile() && file.getName().equals(superClassName + ".java")) {
                                return packPath + File.separator + superClassName;
                            }
                        }
                    }
                }
            }
            return packageName.replace(".", File.separator) + File.separator + superClassName + ".java";
        }
        return null;
    }

}
