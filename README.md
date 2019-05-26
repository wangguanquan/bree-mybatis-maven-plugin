# bree-mybatis-maven-plugin

bree-mybatis-maven-plugin源于github开源项目`mybatis-dalgen`，
对其进行魔改变为现在的版本，后来作者将项目迁到国内oschina地址是[mybatis.generator](https://gitee.com/bangis/mybatis.generator)
在此感谢原作者bangis.wangdf

bree与原项目相比有着更加丰富的配置项，可以对Mapper, dao, service, vo, dto等java
文件的`annotation`，`extends`以及`implements`进行配置，以满足不同项目的需求。

默认的bree通过DML生成初始的全字段的BaseResultMap和Base_Column_List，以及与表对应
的do对象。同时还会生成默认insert/update/deleteById/getById操作，如果项目集成第
三方工具已包含此功能可以通过`<create-default-operation value="false"/>`关闭默认
CRUD操作。

`add-to-git-if-new`配置项可以在生成新文件时执行`git add`方法。

## 使用方法

使用`mvn tree-mybatis:gen`命令运行插件，初次运行后需要在`config.xml`文件中配置数据源
和其它代码生成相关配置项，然后再次执行`mvn tree-mybatis:gen`命令，命令窗口会提示你输入要生成
的表名，输入表名回车即完成Mapper接口,vo,dto以及mapper.xml的自动生成。

所有的修改请在bree/xxxTables下对应的表名xml中修改，大部分的写法与直接写mybatis mapper
没有区别，修改后执行`mvn tree-mybatis:gen`命令

## 配置说明

## 示例

1. 增加分页查询的count语句自定义，在分页查询的name后加Count即可自动匹配，而不会自动生成count语句。
2. 同一个xml文件分页语句paging属性相同时，将会进行合并处理。
