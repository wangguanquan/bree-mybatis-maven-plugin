<@pp.dropOutputFile />
<#import "../lib/lib.ftl" as lib/>
<#list bree.xmlMappers as xmlMapper>
    <@pp.changeOutputFile name = "/main/${xmlMapper.classPath}/${xmlMapper.doMapper.className}.xml" />
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${xmlMapper.doMapper.packageName}.${xmlMapper.doMapper.className}">
    <!-- 自动生成, 请勿修改。修改 ${xmlMapper.table.sqlName}.xml -->

<#--生成BaseResultMap-->
    <resultMap id="BaseResultMap"  type="${xmlMapper.doClass.packageName}.${xmlMapper.doClass.className}">
        <#list xmlMapper.table.columnList as column>
        <#if column.sqlName =="ID"><id column="${column.sqlName}" property="${column.javaName}" jdbcType="${column.sqlType}" javaType="${column.javaType}"/><#else><result column="${column.sqlName}" property="${column.javaName}" jdbcType="${column.sqlType}" javaType="${column.javaType}"/></#if>
        </#list>
    </resultMap>
<#--生成自定义ResultMap-->
<#list xmlMapper.resultMaps as resultMap>

    <!-- ${resultMap.desc!} -->
    <resultMap id="${resultMap.name}" type="${resultMap.packageName}.${resultMap.className}">
<#-- Column -->
    <#list resultMap.columnList as column>
        <#if column.sqlName =="ID" && column.key??><id column="${column.sqlName}" property="${column.javaName}" javaType="${column.javaType}"/><#else><result column="${column.sqlName}" property="${column.javaName}" javaType="${column.javaType}"/></#if>
    </#list>
    <#if resultMap.innerXML??>
    ${resultMap.innerXML!}
    </#if>
    </resultMap>
</#list>

<#-- baseSql -->
    <sql id="Base_Column_List">
        <#list xmlMapper.table.columnList as column><#if column_index gt 0>,</#if>${column.sqlName}</#list>
    </sql>

    <#list xmlMapper.cfTable.sqlList as sqlTag>
    <#if sqlTag.remark??><!--${sqlTag.remark!}--></#if>
    ${sqlTag.content}
    </#list>

<#-- sql部分  -->
    <#list xmlMapper.cfTable.operations as operation>

    <#if operation.multiplicity.code=="paging"><#--分页-->
    <#if operation.cdataPageCount??><#--判断是否既有求和语句-->
    <!--${operation.remark!operation.name} PageCount-->
    <${operation.om!} id="${operation.name}Count"  resultType="int"${lib.timeout(operation)}>
${operation.cdataPageCount!}
    </${operation.om!}>
    </#if>

    <!--${operation.remark!operation.name} PageResult-->
    <${operation.om!} id="${operation.name}Result"  ${lib.mapperResult(operation)}${lib.timeout(operation)}>
    ${operation.cdata!}
        limit ${"#"}{pageSize} offset ${"#"}{offset}
    </${operation.om!}>
    <#else><#--非分页-->
    <!--${operation.remark!operation.name}-->
    <${operation.om!} id="${operation.name}" ${lib.mapperResult(operation)}${lib.timeout(operation)}>
${operation.cdata!}
    </${operation.om!}>
    </#if>
    </#list>
</mapper>
</#list>
