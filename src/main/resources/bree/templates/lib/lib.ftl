<#--空格处理-->
<#function space param>
    <#if param?length gt 15><#return ""/>
    <#else>
        <#return "               "?substring(param?length)/>
    </#if>
</#function>

<#-- operation 2 sql -->
<#-- Don't use this function, replace it with operation.operation -->
<#function operation2Sql param>
    <#if param?starts_with("insert")><#return "insert"/></#if>
    <#if param?starts_with("update")><#return "update"/></#if>
    <#if param?starts_with("delete")><#return "delete"/></#if>
    <#return "select"/>
</#function>

<#-- mapper-xml result -->
<#function mapperResult operation>
    <#if operation.resultMap??><#return ' resultMap="${operation.resultMap}"'/></#if>
    <#if operation.resultType??><#return ' resultType="${operation.resultType}"'/></#if>
    <#if operation.operation == "insert"><#return ''/></#if>
    <#if operation.operation == "update"><#return ''/></#if>
    <#if operation.operation == "delete"><#return ''/></#if>
    <#return ' resultMap="BaseResultMap"'/>
</#function>

<#function timeout operation>
    <#if operation.timeout??><#return ' timeout="${operation.timeout}"'/></#if>
    <#return ""/>
</#function>

<#-- insert  时字段处理 -->
<#function insertVal column>
    <#if column.column == "LAST_UPDATED" || column.column == "DATE_CREATED"><#return "now()"></#if>
    <#if column.column == "IS_DELETED"><#return "'N'"></#if>
    <#return '${"#"}{${column.property}, jdbcType=${column.jdbcType}}'/>
</#function>

<#-- Update 时字段处理 -->
<#function updateVal column>
    <#if column.column == "LAST_UPDATED" || column.column == "DATE_CREATED"><#return "now()"></#if>
    <#return '${"#"}{${column.property}, jdbcType=${column.jdbcType}}'/>
</#function>

<#-- update 中需要设置的字段 -->
<#function updateIncludeColumn column primaryKeys>
    <#if column.column == "CREATOR" || column.column == "DATE_CREATED">
        <#return false>
    </#if>
    <#list primaryKeys as pkColumn>
        <#if pkColumn.column == column.column><#return false></#if>
    </#list>
    <#return true>
</#function>

<#-- 下划线转驼峰 -->
<#function toCamelCase s>
    <#if s?contains("_")>
        <#assign result = ""/>
        <#list s?lower_case?split("_") as x>
            <#if x_index == 0>
                <#assign result = result + x/>
            <#else>
                <#assign result = result + x?capitalize/>
            </#if>
        </#list>
        <#return result>
    <#else>
        <#return s?lower_case>
    </#if>
</#function>

<#-- 驼峰转下划线 -->
<#function toUnderlineName s>
    <#if s?contains("_")>
        <#return s?upper_case>
    <#else>
        <#return s?replace("([A-Z]+)", "_$1", "r")?upper_case>
    </#if>
</#function>