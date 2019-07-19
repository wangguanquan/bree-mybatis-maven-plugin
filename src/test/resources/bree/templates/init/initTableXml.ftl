<@pp.dropOutputFile />
<#import "../lib/lib.ftl" as lib/>
<#list bree.tables as table>
    <@pp.changeOutputFile name = "/${bree.tablesPath}/${table.name}.xml" />
<!DOCTYPE table SYSTEM "../config/table-config-1.1.dtd">
<table name="${table.name}" physicalName="${table.physicalName}"<#if table.remark??> remark="${table.remark!}"</#if>>
    <!--  特殊字符说明  &lt;&gt;   <> -->
    <!-- BaseResultMap 和 Base_Column_List 已全量生成include引用即可-->

<#if table.createDefaultOperation>
    <insert id="insert" paramType="object" remark="插入表:${table.name}">
        INSERT INTO ${table.name}(
        <#list table.columnList as column>
            <#if column.column != "ID"><#if column_index gt 1>, </#if><#if column.reserved>`${column.column}`<#else>${column.column}</#if></#if>
        </#list>
        ) VALUES (
        <#list table.columnList as column>
            <#if column.column != "ID"><#if column_index gt 1>, </#if>${lib.insertVal(column)}</#if>
        </#list>
        )
    </insert>

<#if table.primaryKeys??>
    <update id="update" paramType="object" remark="更新表:${table.name}">
        UPDATE ${table.name}
        SET
        <#assign c_idx = 0>
        <#list table.columnList as column>
            <#if lib.updateIncludeColumn(column,table.primaryKeys.columnList)><#assign c_idx = c_idx+1>
            <#if c_idx gt 1>, </#if><#if column.reserved>`${column.column}`<#else>${column.column}</#if>${lib.space(column.column)} = ${lib.updateVal(column)}
            </#if>
        </#list>
        WHERE
        <#list table.primaryKeys.columnList as column>
            <#if column_index gt 0>AND </#if>${column.column}${lib.space(column.column)} = ${"#"}{${column.property}, jdbcType=${column.jdbcType}}
        </#list>
    </update>

    <delete id="deleteBy${table.primaryKeys.pkName}" remark="根据主键删除数据:${table.name}">
        DELETE FROM ${table.name}
        WHERE
        <#list table.primaryKeys.columnList as column>
            <#if column_index gt 0>AND </#if>${column.column} = ${"#"}{${column.property}, jdbcType=${column.jdbcType}}
        </#list>
    </delete>

    <select id="getBy${table.primaryKeys.pkName}" multiplicity="one" remark="根据主键获取数据:${table.name}">
        SELECT *
        FROM ${table.name}
        WHERE
        <#list table.primaryKeys.columnList as column>
            <#if column_index gt 0>AND </#if>${column.column} = ${"#"}{${column.property}, jdbcType=${column.jdbcType}}
        </#list>
    </select>
</#if>
</#if>
</table>
</#list>
