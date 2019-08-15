package cn.vonce.sql.page;


import cn.vonce.common.bean.PagingRS;
import cn.vonce.common.enumerate.ResultCode;
import cn.vonce.sql.bean.Paging;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.SqlSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 分页助手，使用该分页助手必须实现PagingService 接口
 *
 * @param <T>
 * @author jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年3月14日上午11:59:50
 */
public class PageHelper<T> {

    public static final String SELECT = "select";
    public static final String SELECT_MAP_LIST = "selectMapList";
    public static final String COUNT = "count";

    private Logger logger = LoggerFactory.getLogger(PageHelper.class);

    /**
     * 实例化分页助手 - 手动设置参数
     *
     * @param pagenum   当前页
     * @param pagesize  每页数量
     * @param timestamp 时间戳
     */
    public PageHelper(Integer pagenum, Integer pagesize, String timestamp) {
        this(pagenum, pagesize, null, null, timestamp);
    }

    /**
     * 实例化分页助手 - 手动设置参数
     *
     * @param pagenum       当前页
     * @param pagesize      每页数量
     * @param sortdatafield 排序字段
     * @param sortorder     排序方式
     * @param timestamp     时间戳
     */
    public PageHelper(Integer pagenum, Integer pagesize, String[] sortdatafield, String[] sortorder, String timestamp) {
        if (pagenum == null) {
            pagenum = 0;
        }
        if (pagesize == null || pagesize == 0) {
            pagesize = 10;
        }
        this.setPagenum(pagenum);
        this.setPagesize(pagesize);
        this.setSortdatafield(sortdatafield);
        this.setSortorder(sortorder);
        this.setTimestamp(timestamp);
    }

    /**
     * 实例化分页助手 - 根据请求参数自动获取
     *
     * @param request
     */
    public PageHelper(HttpServletRequest request) {
        super();
        Integer pagenum = null;
        Integer pagesize = null;
        try {
            pagenum = request.getParameter("pagenum") == null ? 0 : Integer.parseInt(request.getParameter("pagenum"));
            pagesize = request.getParameter("pagesize") == null ? 10 : Integer.parseInt(request.getParameter("pagesize"));
            this.setSortdatafield(request.getParameterValues("sortdatafield"));
            this.setSortorder(request.getParameterValues("sortorder"));
            this.setTimestamp(request.getParameter("timestamp"));
        } catch (NumberFormatException nfe) {
            try {
                throw new Exception("初始化 PagingHelper 对象失败 , 请检查 pagenum 或 pagesize 参数是否为正确数字 : " + nfe.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pagenum == null) {
            pagenum = 0;
        }
        if (pagesize == null || pagesize == 0) {
            pagesize = 10;
        }
        this.setPagenum(pagenum);
        this.setPagesize(pagesize);
    }

    private Integer pagenum;
    private Integer pagesize;
    private Integer totalRecords;
    private Integer totalPage;
    private String[] sortdatafield;
    private String[] sortorder;
    private String timestamp;
    private List<T> dataList;
    private PagingMethod pagingMethod;

    /**
     * 获取当前页
     *
     * @return
     * @author jovi
     * @date 2017年3月14日上午11:57:50
     */
    public Integer getPagenum() {
        return pagenum;
    }

    /**
     * 设置当前页
     *
     * @param pagenum
     * @author Jovi
     * @date 2017年7月19日下午5:32:19
     */
    private void setPagenum(Integer pagenum) {
        this.pagenum = pagenum;
    }

    /**
     * 获取每页数量
     *
     * @return
     * @author jovi
     * @date 2017年3月14日上午11:57:55
     */
    public Integer getPagesize() {
        return pagesize;
    }

    /**
     * 设置每页数量
     *
     * @param pagesize
     * @author Jovi
     * @date 2017年7月19日下午5:32:58
     */
    private void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    /**
     * 获取总数量
     *
     * @return
     * @author jovi
     * @date 2017年3月14日上午11:59:27
     */
    public Integer getTotalRecords() {
        return totalRecords;
    }

    /**
     * 获取总页数
     *
     * @return
     * @author jovi
     * @date 2017年3月14日上午11:58:01
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * 获取排序的字段
     *
     * @return
     * @author jovi
     * @date 2017年5月23日上午11:49:57
     */
    public String[] getSortdatafield() {
        return sortdatafield;
    }

    /**
     * 设置排序的字段
     *
     * @param sortdatafield
     * @author Jovi
     * @date 2017年7月19日下午5:34:23
     */
    private void setSortdatafield(String[] sortdatafield) {
        this.sortdatafield = sortdatafield;
    }

    /**
     * 获取排序的类型
     *
     * @return
     * @author jovi
     * @date 2017年5月23日上午11:50:28
     */
    public String[] getSortorder() {
        return sortorder;
    }

    /**
     * 设置排序类型
     *
     * @param sortorder
     * @author Jovi
     * @date 2017年7月19日下午5:35:02
     */
    private void setSortorder(String[] sortorder) {
        this.sortorder = sortorder;
    }

    /**
     * 获取分页时间戳
     *
     * @return
     * @author Jovi
     * @date 2017年7月19日下午4:10:04
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 设置分页时间戳(格式必须为："yyyy-MM-dd HH:mm:ss"， 不正确则设置为空)
     *
     * @param timestamp
     * @author Jovi
     * @date 2017年7月19日下午5:25:54
     */
    private void setTimestamp(String timestamp) {
        boolean convertSuccess = true;
        try {
            if (timestamp != null) {
                // 指定日期格式为四位年/两位月份/两位日期，注意yyyy-MM-dd区分大小写；
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 设置lenient为false.
                // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                format.setLenient(false);
                format.parse(timestamp);
            } else {
                convertSuccess = false;
            }
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        if (convertSuccess) {
            this.timestamp = timestamp;
        }
    }

    /**
     * 获取分页列表数据
     *
     * @return
     * @author jovi
     * @date 2017年3月14日上午11:58:09
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 设置分页列表数据
     *
     * @param dataList
     * @author jovi
     * @date 2017年3月14日上午11:58:15
     */
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    /**
     * 获取pagingMethod分页查询的方法名称<br>
     * 如果pagingMethod为空 默认 count 方法名为"count",select方法名为"select"
     *
     * @return
     * @author jovi
     * @date 2017年3月10日下午7:10:59
     */
    public PagingMethod getPagingMethod() {
        if (pagingMethod == null) {
            pagingMethod = new PagingMethod(COUNT, SELECT);
        }
        return pagingMethod;
    }

    /**
     * 设置pagingMethod，count(统计),select(查询)方法的名称
     *
     * @param pagingMethod
     * @author jovi
     * @date 2017年3月10日下午7:12:00
     */
    public void setPagingMethod(PagingMethod pagingMethod) {
        this.pagingMethod = pagingMethod;
    }

    /**
     * 总数量、总页数处理
     *
     * @param count
     * @author Jovi
     * @date 2018年4月21日下午6:49:45
     */
    public void dispose(long count) {
        // 保存总数
        this.totalRecords = (int) count;
        // 计算最多有几页
        int countPage = totalRecords % pagesize == 0 ? totalRecords / pagesize : totalRecords / pagesize + 1;
        // 返回总页数
        totalPage = countPage;
        // 如果提交的页数比总实际页数大，那就等于实际总页数
        if (pagenum > totalPage) {
            pagenum = totalPage;
        }
    }

    /**
     * 获得分页数据
     *
     * @param select     查询条件拓展
     * @param pageService 分页接口
     * @author jovi
     * @date 2017年3月14日上午11:58:41
     */
    public PageHelper<T> paging(Select select, PagingService pageService) {
        return paging(null, select, pageService);
    }

    /**
     * 获得分页数据
     *
     * @param tClazz
     * @param select     SqlBean
     * @param pageService 分页接口
     * @author jovi
     * @date 2017年3月14日上午11:58:41
     */
    public PageHelper<T> paging(Class<T> tClazz, Select select, PagingService pageService) {
        try {
            Class<? extends PagingService> clazz = pageService.getClass();
            Method countMethod = null;
            Method selectMethod = null;
            if (tClazz != null) {
                countMethod = clazz.getMethod(this.getPagingMethod().getCount(), Class.class, Select.class);
                selectMethod = clazz.getMethod(this.getPagingMethod().getSelect(), Class.class, Select.class);
            } else {
                countMethod = clazz.getMethod(this.getPagingMethod().getCount(), Select.class);
                selectMethod = clazz.getMethod(this.getPagingMethod().getSelect(), Select.class);
            }
            // 衍生一个对象用于select查询
            Select sqlBeanSelect = (Select) select.copy();
            // 预防设置了查询字段，故将查询字段换位统计字段
            select.getColumn().clear();
            select.column(SqlHelperCons.COUNT + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.ALL + SqlHelperCons.END_BRACKET);
            // 先统计数量
            long count;
            if (tClazz != null) {
                count = (long) countMethod.invoke(pageService, tClazz, select);
            } else {
                count = (long) countMethod.invoke(pageService, select);
            }
            // 计算共有几页
            this.dispose(count);
            //设置分页
            sqlBeanSelect.setPage(pagenum, pagesize);
            // controller可传递排序语句
            addOrderBy(sqlBeanSelect);
            Object obj = null;
            if (tClazz != null) {
                obj = selectMethod.invoke(pageService, tClazz, sqlBeanSelect);
            } else {
                obj = selectMethod.invoke(pageService, sqlBeanSelect);
            }
            this.setDataList((List<T>) obj);
            logger.debug(clazz.getName() + "获取分页数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("获取分页数据失败:" + e.getMessage(), e);
        }
        return this;
    }

    /**
     * 排序处理
     *
     * @param select
     * @author Jovi
     * @date 2018年4月21日下午5:46:50
     */
    private void addOrderBy(Select select) {
        if (sortdatafield != null && sortdatafield.length > 0) {
            for (int i = 0; i < sortdatafield.length; i++) {
                String field = sortdatafield[i];
                String sort = null;
                try {
                    sort = sortorder[i];
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                if (sort == null || sort.equals("") || !SqlSort.ASC.name().equals(sort.toUpperCase())) {
                    sort = SqlSort.DESC.name();
                }
                if (field != null && !"".equals(field) && sort != null && !"".equals(sort)) {
                    select.orderBy(field, sort.toUpperCase());
                }
            }
        }
    }

    /**
     * 返回结果集
     *
     * @param message
     * @return
     * @author jovi
     * @date 2017年3月14日上午11:59:06
     */
    public PagingRS toResult(String message) {
        PagingRS result = new PagingRS();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(message == null || message.equals("") ? "获取列表成功" : message);
        result.setData(this.getDataList());
        result.setPagenum(this.getPagenum());
        result.setPagesize(this.getPagesize());
        result.setTotalRecords(this.getTotalRecords());
        result.setTotalPage(this.getTotalPage());
        result.setTimestamp(this.getTimestamp());
        return result;
    }

    /**
     * 返回分页参数对象
     *
     * @return
     */
    public Paging getPaging() {
        return new Paging(this.pagenum, this.pagesize, this.sortdatafield, this.sortorder);
    }

    /**
     * 实现分类接口的方法
     *
     * @author jovi
     * @version 1.0
     * @email 766255988@qq.com
     * @date 2017年3月10日下午6:53:04
     */
    public static class PagingMethod {
        private String count;
        private String select;

        public PagingMethod() {
            super();
        }

        public PagingMethod(String count, String select) {
            super();
            this.count = count;
            this.select = select;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getSelect() {
            return select;
        }

        public void setSelect(String select) {
            this.select = select;
        }

    }
}
