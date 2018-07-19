package config;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import java.text.SimpleDateFormat;
import java.util.*;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;


@SuppressWarnings("Duplicates")
public class MyCommentGenerator implements CommentGenerator{
    private Properties properties;
    private Properties systemPro;
    private boolean suppressDate;
    private boolean suppressAllComments;
    private String currentDateStr;
    public MyCommentGenerator() {
        super();
        properties = new Properties();
        systemPro = System.getProperties();
        suppressDate = false;
        suppressAllComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }

    public void addJavaFileComment(CompilationUnit compilationUnit) {
        // add no file level comments by default
        return;
    }

    /**
     * Adds a suitable comment to warn users that the element was generated, and
     * when it was generated.
     */
    public void addComment(XmlElement xmlElement) {
        return;
    }

    public void addRootComment(XmlElement rootElement) {
        // add no document level comments by default
        return;
    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {

    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {

    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

        suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
    }

    /**
     * This method adds the custom javadoc tag for. You may do nothing if you do
     * not wish to include the Javadoc tag - however, if you do not include the
     * Javadoc tag then the Java merge capability of the eclipse plugin will
     * break.
     *
     * @param javaElement
     *            the java element
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }
        String s = getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * This method returns a formated date string to include in the Javadoc tag
     * and XML comments. You may return null if you do not want the date in
     * these documentation elements.
     *
     * @return a string representing the current timestamp, or null
     */
    protected String getDateString() {
        String result = null;
        if (!suppressDate) {
            result = currentDateStr;
        }
        return result;
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        sb.append("* @className 内部类:"+innerClass.getClass().getName()+"#");
        innerClass.addJavaDocLine(sb.toString().replace("#", "\n"));
        innerClass.addJavaDocLine("*/");
    }

    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        innerEnum.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerEnum.addJavaDocLine(sb.toString().replace("\n", " "));
        innerEnum.addJavaDocLine(" */");
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("TIMESTAMP","DATETIME");
        map.put("LONGVARCHAR","text");
        map.put("INTEGER","int");
        map.put("DECIMAL","decimal(65,30)");
        map.put("VARCHAR","varchar(60)");
        map.put("BIGINT","bigint");


        // 成员变量注释
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**\t"+introspectedColumn.getRemarks()+"\t*/");

        // 成员变量注解
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        String primaryKeyName = primaryKeyColumns.get(0).getJavaProperty();
        String primaryKeyDataType= primaryKeyColumns.get(0).getJdbcTypeName();
        String primaryKeyRemarks = primaryKeyColumns.get(0).getRemarks();


        String javaPropertyName = introspectedColumn.getJavaProperty();
        String jdbcTypeName = introspectedColumn.getJdbcTypeName();
        String remarks = introspectedColumn.getRemarks();



        if (javaPropertyName.equals(primaryKeyName)){
            field.addAnnotation("@Id");
            field.addAnnotation("@GeneratedValue(generator = \"idFactory\")");
            field.addAnnotation("@GenericGenerator(name = \"idFactory\",strategy = \"user.service.api.factory.IdFactory\")");
            field.addAnnotation("@Column(name = \""+primaryKeyColumns.get(0).getActualColumnName()+"\",columnDefinition = \""+map.get(primaryKeyDataType)+" COMMENT  \'"+primaryKeyRemarks+"\'\")");

            field.addAnnotation("@ApiModelProperty(value = \"主键:"+primaryKeyRemarks+"\",name = \""+primaryKeyName+"\",dataType = \""+primaryKeyDataType+"\")");
        }
        else {
            if ("DECIMAL".equals(jdbcTypeName)){
                field.addAnnotation("@JsonSerialize(using = BigDecimalJsonSerializer.class)");
                field.addAnnotation("@JsonDeserialize(using = BigDecimalJsonDeserializer.class)");

            }

            field.addAnnotation("@Column(name = \""+introspectedColumn.getActualColumnName()+"\",columnDefinition = \""+map.get(jdbcTypeName)+" COMMENT  \'"+remarks+"\'\")");

            field.addAnnotation("@ApiModelProperty(value = \"" + remarks + "\",name = \"" + javaPropertyName + "\",dataType = \"" + jdbcTypeName + "\")");
        }
    }



    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 生成类注释
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        topLevelClass.addJavaDocLine("/**");
        sb.append(" * @author 创建者: ");
        sb.append(systemPro.getProperty("user.name"));
        sb.append("#");
        sb.append(" * @createTime 创建时间:");
        sb.append(currentDateStr);
        topLevelClass.addJavaDocLine(sb.toString().replace("#", "\n"));
        topLevelClass.addJavaDocLine(" */");
        topLevelClass.addJavaDocLine("\n");

        //  生成API注解
        String domainObjectName = introspectedTable.getTableConfiguration().getDomainObjectName();
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Entity");
        topLevelClass.addAnnotation("@Table(name = \""+introspectedTable.getTableConfiguration().getTableName()+"\")");
        topLevelClass.addAnnotation("@JsonIgnoreProperties(value = { \"hibernateLazyInitializer\", \"handler\" })");
        topLevelClass.addAnnotation("@ApiModel(value = \""+domainObjectName+"\""+",description = \""+domainObjectName+"实体类\")");

    }

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        if (suppressAllComments) {
            return;
        }

        method.addJavaDocLine("/**");
        StringBuilder sb = new StringBuilder();
        sb.append("* @method :方法名\t"+method.getName()+"#");
        sb.append("\t* @description :方法作用描述\t#");
        sb.append("\t*#");
        sb.append("\t* @param :参数\t"+method.getParameters()+"#");
        sb.append("\t* @return :返回值\t"+method.getReturnType());
        method.addJavaDocLine(sb.toString().replace("#", " \n"));
        method.addJavaDocLine("*/");
    }

    public void addGetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
//        if (suppressAllComments) {
//            return;
//        }
//        method.addJavaDocLine("/**");
//        StringBuilder sb = new StringBuilder();
//        sb.append(" * ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString().replace("\n", " "));
//        sb.setLength(0);
//        sb.append(" * @return ");
//        sb.append(introspectedColumn.getActualColumnName());
//        sb.append(" ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString().replace("\n", " "));
//        method.addJavaDocLine(" */");
    }



    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
//        if (suppressAllComments) {
//            return;
//        }
//        StringBuilder sb = new StringBuilder();
//        field.addJavaDocLine("/**");
//        sb.append("*,");
//        sb.append(introspectedTable.)
//        sb.append(introspectedTable.getFullyQualifiedTable());
//        field.addJavaDocLine(sb.toString().replace(",", "\n"));
//        field.addJavaDocLine(" */");
    }

    public void addSetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
//        if (suppressAllComments) {
//            return;
//        }
//        method.addJavaDocLine("/**");
//        StringBuilder sb = new StringBuilder();
//        sb.append(" * ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString().replace("\n", " "));
//        Parameter parm = method.getParameters().get(0);
//        sb.setLength(0);
//        sb.append(" * @param ");
//        sb.append(parm.getName());
//        sb.append(" ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString().replace("\n", " "));
//        method.addJavaDocLine(" */");
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        sb.append("* @className 内部类:" + innerClass.getClass().getName() + "#");
        innerClass.addJavaDocLine(sb.toString().replace("#", "\n"));
        innerClass.addJavaDocLine("*/");
    }
}