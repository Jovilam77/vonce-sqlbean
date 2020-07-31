package cn.vonce.sql.spring.processor;

import cn.vonce.sql.processor.SqlConstantProcessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
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
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SpringSqlConstantProcessor extends SqlConstantProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        return super.process(annotations, env);
    }

}
