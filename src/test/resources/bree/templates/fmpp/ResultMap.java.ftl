<@pp.dropOutputFile />
<#list bree.resultMaps as resultMap>
    <@pp.changeOutputFile name = "/main/java/${resultMap.classPath}/${resultMap.className}.java" />
package ${resultMap.packageName};

import java.io.Serializable;
<#list resultMap.importLists as import>
<#if import??>import ${import};</#if>
</#list>

/**
 * The table ${resultMap.desc!}
 * 注意:此结构由系统生成, 禁止手工修改以免被覆盖, 请通过bree-mybatis插件生成
 * @author ${bree.author}
 */
public class ${resultMap.className} implements Serializable {

    private static final long serialVersionUID = -1L;

    <#list resultMap.fieldList as field>

    /**
     * ${field.name} ${field.desc!}.
     */
    private ${field.javaType} ${field.name};
    </#list>
    <#list resultMap.fieldList as field>

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
