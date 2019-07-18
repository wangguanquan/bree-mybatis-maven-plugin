<@pp.dropOutputFile />
<#import "../lib/lib.ftl" as lib/>
<#list bree.xmlMappers as xmlMapper>
    <@pp.changeOutputFile name = "/main/${xmlMapper.classPath}/${xmlMapper.doMapper.className}.xml" />
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${xmlMapper.doMapper.packageName}.${xmlMapper.doMapper.className}">
    <!-- 自动生成, 请勿修改。修改 ${xmlMapper.table.name}.xml -->

<#-- 生成BaseResultMap -->
    <resultMap id="BaseResultMap"  type="${xmlMapper.doClass.packageName}.${xmlMapper.doClass.className}">
        <#list xmlMapper.table.columnList as column>
        <#if column.primaryKey ><id column="${column.column}" property="${column.property}"<#if column.jdbcType??> jdbcType="${column.jdbcType}"</#if><#if column.javaType??> javaType="${column.javaType}"</#if><#if column.typeHandler??> typeHandler="${column.typeHandler}"</#if> /><#else><result column="${column.column}" property="${column.property}"<#if column.jdbcType??> jdbcType="${column.jdbcType}"</#if><#if column.javaType??> javaType="${column.javaType}"</#if><#if column.typeHandler??> typeHandler="${column.typeHandler}"</#if> /></#if>
        </#list>
    </resultMap>
<#-- 生成自定义ResultMap -->
<#list xmlMapper.resultMaps as resultMap>

    <#if resultMap.desc??><!-- ${resultMap.desc!} --></#if>
    <resultMap id="${resultMap.id}" type="${resultMap.packageName}.${resultMap.className}">
<#-- Column -->
    <#list resultMap.columnList as column>
        <#if column.primaryKey ><id column="${column.column}" property="${column.property}"<#if (column.jdbcType)??> jdbcType="${column.jdbcType}"</#if><#if column.javaType??> javaType="${column.javaType}"</#if><#if column.typeHandler??> typeHandler="${column.typeHandler}"</#if> /><#else><result column="${column.column}" property="${column.property}"<#if column.jdbcType??> jdbcType="${column.jdbcType}"</#if><#if column.javaType??> javaType="${column.javaType}"</#if><#if column.typeHandler??> typeHandler="${column.typeHandler}"</#if> /></#if>
    </#list>
    <#if resultMap.innerXML??>
    ${resultMap.innerXML!}
    </#if>
    </resultMap>
</#list>

<#-- baseSql -->
    <sql id="Base_Column_List">
        <#list xmlMapper.table.columnList as column><#if column_index gt 0>,</#if><#if column.reserved>`${column.column}`<#else>${column.column}</#if></#list>
    </sql>
    <#list xmlMapper.cfTable.sqlList as sqlTag>

    <#if sqlTag.remark??><!-- ${sqlTag.remark!} --></#if>
    ${sqlTag.content}
    </#list>

<#-- sql部分  -->
    <#list xmlMapper.cfTable.operations as operation>

    <#if operation.multiplicity == "paging"><#-- 分页 -->
    <#if operation.cdataPageCount??><#-- 判断是否既有求和语句 -->
    <!-- ${operation.remark!operation.id} PageCount -->
    <${operation.operation!} id="${operation.id}Count" resultType="int"${lib.timeout(operation)}${operation.others!}>
        ${operation.cdataPageCount!}
    </${operation.operation!}>
    </#if>

    <!-- ${operation.remark!operation.id} PageResult -->
    <${operation.operation!} id="${operation.id}Result"${lib.mapperResult(operation)}${lib.timeout(operation)}${operation.others!}>
    ${operation.cdata!}
        limit ${"#"}{pageSize} offset ${"#"}{offset}
    </${operation.operation!}>
    <#else><#-- 非分页 -->
    <!-- ${operation.remark!operation.id} -->
    <${operation.operation!} id="${operation.id}"${lib.mapperResult(operation)}${lib.timeout(operation)}${operation.others!}>
        ${operation.cdata!}
    </${operation.operation!}>
    </#if>
    </#list>
</mapper>
</#list>
