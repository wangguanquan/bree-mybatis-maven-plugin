<@pp.dropOutputFile />
<#list bree.voList as vo>
    <@pp.changeOutputFile name = "/main/java/${vo.classPath}/${vo.className}.java" />
package ${vo.packageName};

<#list vo.importLists as import>
<#if import??>import ${import};</#if>
</#list>

/**
 * The table ${vo.desc!}
 * 注意:此结构由系统生成, 禁止手工修改以免被覆盖, 请通过bree-mybatis插件生成
 * @author ${bree.author}
 */
<#if vo.annotationArray.size() gt 0>
  <#list vo.annotationArray as annotation>
@${annotation.className}
  </#list>
</#if>
public class ${vo.className}<#if vo.extend??> extends ${vo.extend.className}</#if><#if vo.implementArray.size() gt 0> implements <#list vo.implementArray as impl><#if impl_index gt 0>, </#if>${impl.className}</#list></#if> {
    <#list vo.fieldList as field>

    /**
     * ${field.name} ${field.desc!}.
     */
    private ${field.javaType} ${field.name};
    </#list>
    <#list vo.fieldList as field>

    /**
     * Setting the ${field.name} ${field.desc!}.
     */
    public void set${field.name?cap_first}(${field.javaType} ${field.name}) {
        this.${field.name} = ${field.name};
    }

    /**
     * Returns the ${field.name} ${field.desc!}.
     *
     * @return the ${field.javaType}
     */
    public ${field.javaType} get${field.name?cap_first}() {
        return ${field.name};
    }
    </#list>
}
</#list>
