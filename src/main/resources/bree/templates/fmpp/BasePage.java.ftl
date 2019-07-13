<@pp.dropOutputFile />
<#if bree.useBasePage>
<#assign count=0 />
<#list bree.voList as vo>

    <#assign count=count+1 />
    <#if count gt 1 >
        <#break>
    </#if>

    <@pp.changeOutputFile name = "/main/java/${vo.classPath}/BasePage.java" />
package ${vo.packageName};

import java.util.List;

/**
 * Base entry use for paging
 */
public class BasePage<T> {

    private List<T> data;                    // 对象记录结果集
    private int     total           = 0;     // 总记录数
    private int     limit           = 20;    // 默认每页显示记录数
    private int     totalPage       = 1;     // 总页数
    private int     pageNo      = 1;     // 当前页


    private void init() {
        this.totalPage = (this.total - 1) / this.limit + 1;

        // 根据输入可能错误的当前号码进行自动纠正
        if (pageNo < 1) {
            this.pageNo = 1;
        } else if (pageNo > this.totalPage) {
            this.pageNo = this.totalPage;
        }
    }

    /**
     * Returns a page data
     * 
     * @return list of {@link T}
     */
    public List<T> getData() {
        return data;
    }

    /**
     * Setting a page data
     * 
     * @param data a page list data
     */
    public void setDatas(List<T> data) {
        this.data = data;
    }

    /**
     * 得到记录总数
     *
     * @return {int}
     */
    public int getTotal() {
        return total;
    }

    /**
     * 设置总记录数
     * 
     * @param total
     */
    public void setTotal(int total) {
        this.total = total;
        init();
    }

    /**
     * 得到每页显示多少条记录
     *
     * @return {int}
     */
    public int getLimit() {
        return limit;
    }

    /**
     * 得到每页显示多少条记录
     *
     * @return {int}
     */
    public int getPageSize() {
        return limit;
    }

    /**
     * 设置每页多少记录
     * 
     * @param limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * 得到页面总数
     *
     * @return {int}
     */
    public int getPageNos() {
        return totalPage;
    }

    /**
     * 得到当前页号
     *
     * @return {int}
     */
    public int getCurrPageNo() {
        return pageNo;
    }

    /**
     * 设置当前行
     * @param pageNo
     */
    public void setCurrPageNo(int pageNo) {
        if (pageNo == 0) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }

    /**
    * 得到开始行
    */
    public int getOffset() {
        return (pageNo - 1) * limit;
    }

    public String toString() {
        return "[total=" + total +
            ",totalPage=" + totalPage +
            ",pageNo=" + pageNo +
            ",limit=" + limit +
            ",data.size=" + (data != null ? data.size() : 0) +
            "]";
    }
}
</#list>
</#if>