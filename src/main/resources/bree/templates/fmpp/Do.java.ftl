<@pp.dropOutputFile />
<#list bree.dos as DO>
<@pp.changeOutputFile name = "/main/java/${DO.classPath}/${DO.className}.java" />
package ${DO.packageName};

<#list DO.importLists as import>
<#if import??>import ${import};</#if>
</#list>

/**
 * The table ${DO.desc}
 * 注意:此结构由系统生成, 禁止手工修改以免被覆盖, 请通过bree-mybatis插件生成
 * @author ${bree.author}
 */
<#if DO.annotationArray.size() gt 0>
<#list DO.annotationArray as annotation>
@${annotation.className}
</#list>
</#if>
public class ${DO.className}<#if DO.extend??> extends ${DO.extend.className}</#if><#if DO.implementArray.size() gt 0> implements <#list DO.implementArray as impl><#if impl_index gt 0>, </#if>${impl.className}</#list></#if> {
    <#list DO.fieldList as field>

    /**
     * The ${field.name} ${field.desc}.
     */
    private ${field.javaType} ${field.name};
    </#list>
    <#list DO.fieldList as field>

    /**
     * Setting the ${field.name} ${field.desc}.
     */
    public void set${field.name?cap_first}(${field.javaType} ${field.name}) {
        this.${field.name} = ${field.name};
    }

    /**
     * Returns the ${field.name} ${field.desc}.
     *
     * @return the ${field.name} value
     */
    public ${field.javaType} <#if field.javaType=='boolean'>is<#else>get</#if>${field.name?cap_first}() {
        return ${field.name};
    }
    </#list>
}
</#list>
