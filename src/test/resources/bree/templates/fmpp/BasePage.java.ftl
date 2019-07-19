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

    private List<T> data;              // 对象记录结果集
    private int     total     = 0;     // 总记录数
    private int     limit     = 20;    // 默认每页显示记录数
    private int     totalPage = 1;     // 总页数
    private int     pageNo    = 1;     // 当前页


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
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * Returns total of records
     *
     * @return the record count
     */
    public int getTotal() {
        return total;
    }

    /**
     * Setting the total number
     * 
     * @param total the record count
     */
    public void setTotal(int total) {
        this.total = total;
        init();
    }

    /**
     * Returns number of records to show per page
     *
     * @return the limit of per page
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Setting the limit of per page
     *
     * @param limit the limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the page total
     *
     * @return the page total
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * Setting the page total
     *
     * @param totalPage the page total
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * Returns current page No. the first page No. is 'one'
     *
     * @return the page no
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * Setting page No.
     *
     * @param pageNo the page No.
     */
    public void setPageNo(int pageNo) {
        if (pageNo <= 0) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }

    /**
     * Return the offset of cursor to read
     */
    public int getOffset() {
        return (pageNo - 1) * limit;
    }

    @Override
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