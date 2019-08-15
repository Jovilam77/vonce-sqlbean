package cn.vonce.sql.config;

import cn.vonce.common.uitls.StringUtil;
import cn.vonce.common.uitls.XmlConverUtil;
import cn.vonce.sql.enumerate.DbType;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 根据数据库类型配置SqlBeanConfig
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月25日下午12:7:50
 */
public class ConditionOnDbType implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String[] beanName = conditionContext.getBeanFactory().getBeanNamesForType(SqlBeanConfig.class);
        if (beanName == null || beanName.length == 0) {
            String driverClassName = getDriverClassName(conditionContext.getEnvironment());
            if (driverClassName == null) {
                Iterator<String> iterator = conditionContext.getBeanFactory().getBeanNamesIterator();
                while (iterator.hasNext()) {
                    String name = iterator.next();
                    if (name.toLowerCase().indexOf("DataSource".toLowerCase()) > -1) {
                        driverClassName = getDriverClassName(conditionContext, conditionContext.getRegistry().getBeanDefinition(name).getResourceDescription());
                    }

                }
            }
            if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseMysql.class.getName())) {
                if (DbType.MYSQL == getDbType(driverClassName)) {
                    return true;
                }
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseOracle.class.getName())) {
                if (DbType.ORACLE == getDbType(driverClassName)) {
                    return true;
                }
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseSqlServer.class.getName())) {
                if (DbType.SQLSERVER2008 == getDbType(driverClassName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取jdbc驱动类名
     *
     * @param environment
     * @return
     */
    private String getDriverClassName(Environment environment) {
        String driverClassName = null;
        if (environment.containsProperty("spring.datasource.driverClassName")) {
            driverClassName = environment.getProperty("spring.datasource.driverClassName");
        } else if (environment.containsProperty("spring.datasource.driver-class-name")) {
            driverClassName = environment.getProperty("spring.datasource.driver-class-name");
        } else if (environment.containsProperty("datasource.driverClassName")) {
            driverClassName = environment.getProperty("datasource.driverClassName");
        } else if (environment.containsProperty("datasource.driver-class-name")) {
            driverClassName = environment.getProperty("datasource.driver-class-name");
        } else if (environment.containsProperty("jdbc.driverClassName")) {
            driverClassName = environment.getProperty("jdbc.driverClassName");
        } else if (environment.containsProperty("jdbc.driver-class-name")) {
            driverClassName = environment.getProperty("jdbc.driver-class-name");
        } else if (environment.containsProperty("jdbc.driver-class")) {
            driverClassName = environment.getProperty("jdbc.driver-class");
        } else if (environment.containsProperty("jdbc.driver")) {
            driverClassName = environment.getProperty("jdbc.driver");
        } else if (environment.containsProperty("driverClassName")) {
            driverClassName = environment.getProperty("driverClassName");
        } else if (environment.containsProperty("driver-class-name")) {
            driverClassName = environment.getProperty("driver-class-name");
        } else if (environment.containsProperty("driverClass")) {
            driverClassName = environment.getProperty("driverClass");
        } else if (environment.containsProperty("driver-class")) {
            driverClassName = environment.getProperty("driver-class");
        } else if (environment.containsProperty("driver")) {
            driverClassName = environment.getProperty("driver");
        }
        return driverClassName;
    }

    private String getDriverClassName(ConditionContext conditionContext, String resourceDescription) {
        String driverClassName = null;
        if (StringUtil.isEmpty(resourceDescription)) {
            return null;
        }
        try {
            int index = resourceDescription.lastIndexOf(".");
            String suffix = resourceDescription.substring(index + 1, resourceDescription.length() - 1);
            String fileName = resourceDescription.substring(resourceDescription.lastIndexOf("[") + 1, index);
            if (suffix.equals("xml")) {
                File file;
                if (resourceDescription.indexOf("file") > -1) {
                    //绝对路径
                    file = new File(fileName + ".xml");
                } else {
                    URL url = conditionContext.getClassLoader().getResource(fileName + ".xml");
                    file = new File(url.getPath());
                }
                if (!file.exists()) {
                    Resource resource = conditionContext.getResourceLoader().getResource(fileName + ".xml");
                    file = resource.getFile();
                }
                if (file.exists()) {
                    byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
                    Map<String, Object> xmlMap = XmlConverUtil.xml2JsonStyleMap(new String(bytes));
                    Map<String, Object> beansMap = (Map<String, Object>) xmlMap.get("beans");
                    Object beanObject = beansMap.get("bean");
                    List<Map<String, Object>> mapList;
                    if (beanObject instanceof List) {
                        mapList = (List<Map<String, Object>>) beanObject;
                    } else {
                        mapList = new ArrayList<>();
                        mapList.add((Map<String, Object>) beanObject);
                    }
                    for (Map<String, Object> beanMap : mapList) {
                        if (beanMap.containsKey("property") && beanMap.get("property") instanceof List) {
                            for (Map<String, Object> propertyMap : (List<Map<String, Object>>) beanMap.get("property")) {
                                if (propertyMap.containsKey("name") && ((String) propertyMap.get("name")).indexOf("driver") > -1) {
                                    driverClassName = (String) propertyMap.get("value");
                                }
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(driverClassName) && driverClassName.indexOf("$") > -1) {
                        driverClassName = driverClassName.substring(2, driverClassName.length() - 1);
                        if (beansMap.containsKey("context:property-placeholder")) {
                            Map<String, Object> contextMap = (Map<String, Object>) beansMap.get("context:property-placeholder");
                            String location = (String) contextMap.get("location");
                            String path;
                            if (location.lastIndexOf("/") > -1) {
                                path = location.substring(location.indexOf(":") + 1, location.lastIndexOf("/"));
                            } else {
                                path = location.substring(location.indexOf(":") + 1);
                            }
                            File pathFile = new File(conditionContext.getClassLoader().getResource(path).getPath());
                            if (pathFile.exists() && pathFile.isDirectory()) {
                                for (File itemFile : pathFile.listFiles()) {
                                    String value = getValue(itemFile, driverClassName);
                                    if (StringUtil.isNotEmpty(value)) {
                                        driverClassName = value;
                                    }
                                }
                            } else {
                                driverClassName = getValue(pathFile, driverClassName);
                            }
                        }
                    }
                }
            } else if (suffix.equals("class")) {
                String className = fileName.replace("/", ".");
                Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
                Map<String, ?> beanMap = conditionContext.getBeanFactory().getBeansOfType(clazz);
                for (Object object : beanMap.values()) {
                    Field[] fields = object.getClass().getDeclaredFields();
                    if (fields != null && fields.length > 0) {
                        for (Field field : fields) {
                            if (field.getName().toLowerCase().indexOf("driver") > -1) {
                                field.setAccessible(true);
                                driverClassName = (String) field.get(object);
                                if (driverClassName == null) {
                                    Value valueAnn = field.getAnnotation(Value.class);
                                    if (valueAnn != null) {
                                        String key = valueAnn.value();
                                        key = key.substring(2, key.length() - 1);
                                        driverClassName = conditionContext.getEnvironment().getProperty(key);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            } else {
                return null;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return driverClassName;
    }

    private String getValue(File file, String key) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(file);
        properties.load(inputStream);
        inputStream.close();
        return properties.getProperty(key);
    }

    /**
     * 通过驱动类名获取数据库类型
     *
     * @param driverClassName
     * @return
     */
    private DbType getDbType(String driverClassName) {
        if ("com.mysql.jdbc.Driver".equals(driverClassName)) {
            return DbType.MYSQL;
        } else if ("oracle.jdbc.driver.OracleDriver".equals(driverClassName)) {
            return DbType.ORACLE;
        } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driverClassName)) {
            return DbType.SQLSERVER2008;
        } else {
            return DbType.MYSQL;
        }
    }

}
