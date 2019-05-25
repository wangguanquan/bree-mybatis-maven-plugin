<@pp.dropOutputFile />
<#list bree.doMappers as doMapper>
<@pp.changeOutputFile name = "/main/java/${doMapper.classPath}/${doMapper.className}.java" />
package ${doMapper.packageName};

<#list doMapper.importLists as import>
<#if import??>import ${import};</#if>
</#list>
<#if doMapper.hasParam>
import org.apache.ibatis.annotations.Param;
</#if>

/**
 * 由于需要对分页支持,请直接使用对应的DAO类
 * 注意:此结构由系统生成, 禁止手工修改以免被覆盖, 请通过bree-mybatis插件生成
 * The Table ${doMapper.tableName!}.<#if doMapper.tableName != doMapper.desc>
 * ${doMapper.desc!}</#if>
 * @author ${bree.author}
 */
<#if doMapper.annotationArray.size() gt 0>
<#list doMapper.annotationArray as annotation>
@${annotation.className}
</#list>
</#if>
public interface ${doMapper.className}<#if doMapper.extend??> extends ${doMapper.extend.className}</#if><#if doMapper.implementArray.size() gt 0> implements <#list doMapper.implementArray as impl><#if impl_index gt 0>, </#if>${impl.className}</#list></#if> {
    <#list doMapper.methods as method>

    /**
     * ${method.desc!method.name!}.
    <#list  method.params as param>
     * @param ${param.param} ${param.param}
    </#list>
     * @return ${method.returnClass!}
     */
    ${method.returnClass!} ${method.name}(<#list  method.params as param><#if param_index gt 0>, </#if><#if param.pAnnot || method.params?size gt 1>@Param("${param.param}")</#if>${param.paramType!} ${param.param}</#list>);
    </#list>
}
</#list>
