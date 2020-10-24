package cn.vonce.sql.spring.config;

import cn.vonce.common.utils.XmlConverUtil;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.uitls.ReflectUtil;
import cn.vonce.sql.uitls.StringUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
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
        //如果未配置则进行配置，否则跳过
        if (beanName == null || beanName.length == 0) {
            //一般spring boot/spring cloud 项目将通过该方式获取完成
            String driverClassName = getDriverClassName(conditionContext);
            //如果找不到可能为spring mvc项目，将尝试从xml或class读取
            if (driverClassName == null) {
                Iterator<String> iterator = conditionContext.getBeanFactory().getBeanNamesIterator();
                while (iterator.hasNext()) {
                    String name = iterator.next();
                    if (name.toLowerCase().indexOf("DataSource".toLowerCase()) > -1) {
                        driverClassName = getDriverClassName(conditionContext, conditionContext.getRegistry().getBeanDefinition(name).getResourceDescription());
                    }
                }
            }
            if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseMysql.class.getName()) && DbType.MySQL == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseMariaDB.class.getName()) && DbType.MariaDB == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseOracle.class.getName()) && DbType.Oracle == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseSqlServer.class.getName()) && DbType.SQLServer == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUsePostgreSql.class.getName()) && DbType.PostgreSQL == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseDB2.class.getName()) && DbType.DB2 == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseDerby.class.getName()) && DbType.Derby == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseSqlite.class.getName()) && DbType.SQLite == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseHSql.class.getName()) && DbType.Hsql == getDbType(driverClassName)) {
                return true;
            } else if (annotatedTypeMetadata.isAnnotated(ConditionalOnUseH2.class.getName()) && DbType.H2 == getDbType(driverClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取jdbc驱动类名(Spring boot)
     *
     * @param conditionContext
     * @return
     */
    private String getDriverClassName(ConditionContext conditionContext) {
        String driverClassName;
        Environment environment = conditionContext.getEnvironment();
        //优先根据指定的key来获取
        driverClassName = environment.getProperty("spring.datasource.driver-class-name");
        if (driverClassName == null) {
            driverClassName = environment.getProperty("spring.datasource.driverClassName");
        }
        if (driverClassName == null) {
            driverClassName = environment.getProperty("spring.datasource.driver-class");
        }
        if (driverClassName == null) {
            driverClassName = environment.getProperty("spring.datasource.driverClass");
        }
        if (driverClassName == null) {
            driverClassName = environment.getProperty("spring.datasource.driver");
        }
        if (driverClassName != null) {
            return driverClassName;
        }
        //如果找不到，那么将从配置文件中模糊匹配
        try {
            List<URL> configUrlList = new ArrayList<>();
            if (environment.getDefaultProfiles() != null) {
                configUrlList.add(getApplicationFileUrl(null));
            }
            if (environment.getActiveProfiles() != null) {
                for (String active : environment.getActiveProfiles()) {
                    configUrlList.add(getApplicationFileUrl(active));
                }
            }
            List<String> driverNameList = new ArrayList<>();
            driverNameList.add("driver-class-name");
            driverNameList.add("driverClassName");
            driverNameList.add("driver-class");
            driverNameList.add("driverClass");
            driverNameList.add("driver");
            for (URL url : configUrlList) {
                Properties properties;
                if (url != null) {
                    //如果是yml配置
                    if (url.getPath().lastIndexOf(".yml") > -1) {
                        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                        yaml.setResources(new InputStreamResource(url.openStream()));
                        properties = yaml.getObject();
                    } else {
                        properties = new Properties();
                        properties.load(url.openStream());
                    }
                    Enumeration enumeration = properties.propertyNames();
                    while (enumeration.hasMoreElements()) {
                        String name = (String) enumeration.nextElement();
                        for (String driverName : driverNameList) {
                            if (name.indexOf(driverName) > -1) {
                                driverClassName = properties.getProperty(name);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return driverClassName;
    }

    /**
     * 获取配置文件url
     *
     * @param active
     * @return
     * @throws MalformedURLException
     */
    private URL getApplicationFileUrl(String active) throws MalformedURLException {
        URL url = null;
        //优先从项目路径查找配置文件
        File file = new File(System.getProperty("user.dir") + "/config/application" + (StringUtil.isEmpty(active) ? "" : "-" + active) + ".properties");
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir") + "/config/application" + (StringUtil.isEmpty(active) ? "" : "-" + active) + ".yml");
        }
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir") + "/application" + (StringUtil.isEmpty(active) ? "" : "-" + active) + ".properties");
        }
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir") + "/application" + (StringUtil.isEmpty(active) ? "" : "-" + active) + ".yml");
        }
        if (file.exists()) {
            url = file.toURI().toURL();
        } else {
            //如果找不到，那么将从classpath查找配置文件
            url = ClassUtils.getDefaultClassLoader().getResource("config/application" + (StringUtil.isEmpty(active) ? "" : "-" + active) + ".properties");
            if (url == null) {
                url = ClassUtils.getDefaultClassLoader().getResource("config/application" + (StringUtil.isEmpty(active) ? "" : "-" + active) + ".yml");
            }
            if (url == null) {
                url = ClassUtils.getDefaultClassLoader().getResource("application" + (StringUtil.isEmpty(active) ? "" : "-" + active) + ".properties");
            }
            if (url == null) {
                url = ClassUtils.getDefaultClassLoader().getResource("application" + (StringUtil.isEmpty(active) ? "" : "-" + active) + ".yml");
            }
        }
        return url;
    }

    /**
     * 获取jdbc驱动类名(Spring mvc)
     *
     * @param conditionContext
     * @param resourceDescription
     * @return
     */
    private String getDriverClassName(ConditionContext conditionContext, String resourceDescription) {
        String driverClassName = null;
        if (StringUtil.isEmpty(resourceDescription)) {
            return null;
        }
        try {
            //先解析文件名及后缀
            int index = resourceDescription.lastIndexOf(".");
            String suffix = resourceDescription.substring(index + 1, resourceDescription.length() - 1);
            String fileName = resourceDescription.substring(resourceDescription.lastIndexOf("[") + 1, index);
            //如果是xml方式配置
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
                //如果xml配置存在则解析xml
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
                                    break;
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
                                        break;
                                    }
                                }
                            } else {
                                driverClassName = getValue(pathFile, driverClassName);
                            }
                        }
                    }
                }
            }
            //如果是类方式配置
            else if (suffix.equals("class")) {
                String className = fileName.replace("/", ".");
                Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
                Map<String, ?> beanMap = conditionContext.getBeanFactory().getBeansOfType(clazz);
                for (Object object : beanMap.values()) {
                    Field[] fields = object.getClass().getDeclaredFields();
                    if (fields != null && fields.length > 0) {
                        for (Field field : fields) {
                            if (field.getName().toLowerCase().indexOf("driver") > -1) {
                                driverClassName = (String) ReflectUtil.instance().get(object.getClass(), object, field.getName());
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return driverClassName;
    }

    /**
     * 获取配置值
     *
     * @param file
     * @param key
     * @return
     * @throws IOException
     */
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
        if ("com.mysql.jdbc.Driver".equals(driverClassName) || "com.mysql.cj.jdbc.Driver".equals(driverClassName) || "org.gjt.mm.mysql.Driver".equals(driverClassName)) {
            return DbType.MySQL;
        } else if ("org.mariadb.jdbc.Driver".equals(driverClassName)) {
            return DbType.MariaDB;
        } else if ("oracle.jdbc.driver.OracleDriver".equals(driverClassName)) {
            return DbType.Oracle;
        } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driverClassName)) {
            return DbType.SQLServer;
        } else if ("org.postgresql.Driver".equals(driverClassName)) {
            return DbType.PostgreSQL;
        } else if ("com.ibm.db2.jcc.DB2Driver".equals(driverClassName)) {
            return DbType.DB2;
        } else if ("org.apache.derby.jdbc.EmbeddedDriver".equals(driverClassName)) {
            return DbType.Derby;
        } else if ("org.sqlite.JDBC".equals(driverClassName)) {
            return DbType.SQLite;
        } else if ("org.hsqldb.jdbcDriver".equals(driverClassName)) {
            return DbType.Hsql;
        } else if ("org.h2.Driver".equals(driverClassName)) {
            return DbType.H2;
        } else {
            return null;
        }
    }

}
