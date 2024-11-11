<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${packageName}.dao.${className}Dao">

    <!--【${comments}】查询的列-->
    <sql id="${className?uncap_first}Columns">
    <#list attrs as attr>
        <#if attr.inputType == 'associate'>
        <#list "${attr.dictType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>.<#list "${attr.dictType}"?split(":") as codename><#if codename_index == 1>${codename}</#if></#list> As ${attr.columnFieldName}Name,
        </#if>
        a.${attr.columnName?lower_case} As ${attr.columnFieldName}<#if attr_has_next>,</#if>
    </#list>
    </sql>

    <!--【${comments}】关联对应表-->
    <sql id="${className?uncap_first}CollectJoins">
        <#list attrs as attr>
        <#if attr.inputType == 'associate'>
        LEFT JOIN <#list "${attr.dictType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list> ON <#list "${attr.dictType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>.id = a.${attr.columnName?lower_case}
        </#if>
        </#list>
    </sql>

    <!--【${comments}】根据id获取数据-->
    <select id="get" resultType="${className?uncap_first}">
        SELECT
        <include refid="${className?uncap_first}Columns"/>
        FROM ${tableName} a
        <include refid="${className?uncap_first}CollectJoins"/>
        WHERE a.id = <#noparse>#{</#noparse>id<#noparse>}</#noparse>
    </select>

    <!--【${comments}】根据查询条件获取列表数据-->
    <select id="findList" resultType="${className?uncap_first}">
        SELECT
        <include refid="${className?uncap_first}Columns"/>
        FROM ${tableName} a
        <include refid="${className?uncap_first}CollectJoins"/>
        <where>
        <#list attrs as attr>
            <#if attr.queryType == 'between'>
                <if test="${attr.columnFieldName}Range != null and ${attr.columnFieldName}Range != ''">
            </#if>
            <#if attr.queryType != 'between'>
            <if test="${attr.columnFieldName} != null and ${attr.columnFieldName} != ''<#if attr.inputType == 'select'> or ${attr.columnFieldName} == 0</#if>">
            </#if>
                <#if attr.queryType == 'like'>
                AND a.${attr.columnName?lower_case} LIKE concat('%<#noparse>',#{</#noparse>${attr.columnFieldName}<#noparse>}</#noparse>,'%')
                </#if>
                <#if attr.queryType == 'equal'>
                AND a.${attr.columnName?lower_case} = <#noparse>#{</#noparse>${attr.columnFieldName}<#noparse>}</#noparse>
                </#if>
                <#if attr.queryType == 'between'>
                AND a.${attr.columnName?lower_case} BETWEEN LEFT(<#noparse>#</#noparse>{${attr.columnFieldName}Range},10) AND RIGHT(<#noparse>#</#noparse>{${attr.columnFieldName}Range},10)
                </#if>
            </if>
        </#list>
        </where>
        ORDER BY a.update_date DESC
    </select>

    <!--【${comments}】新增数据-->
    <insert id="insert">
        INSERT INTO ${tableName}
        (
        <#list attrs as attr>
            ${attr.columnName?lower_case}<#if attr_has_next>,</#if>
        </#list>
        )
        VALUES
        (
        <#list attrs as attr>
            <#noparse>#{</#noparse>${attr.columnFieldName}<#noparse>}</#noparse><#if attr_has_next>,</#if>
        </#list>
        )
    </insert>

    <!--【${comments}】修改数据-->
    <update id="update">
        UPDATE ${tableName}
            SET
        <#list attrs as attr>
            ${attr.columnName?lower_case} = <#noparse>#{</#noparse>${attr.columnFieldName}<#noparse>}</#noparse><#if attr_has_next>,</#if>
        </#list>
        WHERE id = <#noparse>#{id}</#noparse>
    </update>

    <!--【${comments}】删除数据-->
    <delete id="delete">
        DELETE FROM ${tableName}
        WHERE id = <#noparse>#{id}</#noparse>
    </delete>

    <!--【${comments}】批量删除数据-->
    <delete id="delAll">
        DELETE FROM ${tableName}
        WHERE id IN
        <foreach item="id" collection="array" open="(" separator="," close=")">
            <#noparse>#{id}</#noparse>
        </foreach>
    </delete>

</mapper>
