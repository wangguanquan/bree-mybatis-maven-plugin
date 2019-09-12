<@pp.dropOutputFile />
<#list bree.daoImpls as dao>
    <@pp.changeOutputFile name = "/main/java/${dao.classPath}/${dao.className}.java" />
package ${dao.packageName};

import org.springframework.beans.factory.annotation.Autowired;
<#list dao.importLists as import>
<#if import??>import ${import};</#if>
</#list>

/**
* The Table ${dao.tableName!}.
* 注意:此结构由系统生成, 禁止手工修改以免被覆盖, 请通过bree-mybatis插件生成<#if dao.tableName != dao.desc>
* ${dao.desc!}</#if>
 * @author ${bree.author}
*/
<#if dao.annotationArray.size() gt 0>
    <#list dao.annotationArray as annotation>
@${annotation.className}
    </#list>
</#if>
public class ${dao.className}<#if dao.extend??> extends ${dao.extend.className}</#if><#if dao.implementArray.size() gt 0> implements <#list dao.implementArray as impl><#if impl_index gt 0>, </#if>${impl.className}</#list></#if> {

    @Autowired
    private ${dao.doMapper.className} ${dao.doMapper.className?uncap_first};
    <#list dao.methods as method>

    /**
     * ${method.desc!method.name!}.
        <#list  method.params as param>
     * @param ${param.param} ${param.param}
        </#list>
     * @return ${method.returnClass!}
     */
    public ${method.returnClass!} ${method.name}(<#list  method.params as param><#if param_index gt 0>, </#if>${param.paramType!} <#assign pagingParam = param.param/>${param.param}</#list>) {
    <#if method.pagingFlag == "true">
        <#if method.noCount == "true">
        ${pagingParam}.setData(${dao.doMapper.className?uncap_first}.${method.name}Result(<#list  method.params as param><#if param_index gt 0>, </#if>${param.param}</#list>));
        // 分页但不求Count，可以提升查询速度。移动端分页不需要知道总共多少页。
        // 直到取得的数据量小于pageSize时表示结束
        ${pagingParam}.setTotal(-1);
        <#else>
        int total = ${dao.doMapper.className?uncap_first}.${method.name}Count(<#list  method.params as param><#if param_index gt 0>, </#if>${param.param}</#list>);
        if (total > 0) {
            ${pagingParam}.setData(${dao.doMapper.className?uncap_first}.${method.name}Result(<#list  method.params as param><#if param_index gt 0>, </#if>${param.param}</#list>));
        }
        ${pagingParam}.setTotal(total);
        </#if>
        return ${pagingParam};
    <#else>
        return ${dao.doMapper.className?uncap_first}.${method.name}(<#list  method.params as param><#if param_index gt 0>, </#if>${param.param}</#list>);
    </#if>
    }
    </#list>
}
</#list>
