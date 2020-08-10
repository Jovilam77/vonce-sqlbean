package cn.vonce.sql.spring.page;

import cn.vonce.common.bean.PagingRS;
import cn.vonce.common.enumerate.ResultCode;
import cn.vonce.sql.bean.Order;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.page.PageHelper;
import cn.vonce.sql.page.PagingService;
import cn.vonce.sql.page.ResultData;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ReqPageHelper
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/7/30 20:37
 */
public class ReqPageHelper<T> extends PageHelper<T> {


    public ReqPageHelper(Integer pagenum, Integer pagesize, String timestamp) {
        super(pagenum, pagesize, timestamp);
    }

    public ReqPageHelper(Integer pagenum, Integer pagesize, Order[] orders, String timestamp) {
        super(pagenum, pagesize, orders, timestamp);
    }

    /**
     * 实例化分页助手 - 根据请求参数自动获取
     *
     * @param request
     */
    public ReqPageHelper(HttpServletRequest request) {
        super();
        Integer pagenum = null;
        Integer pagesize = null;
        Order[] orders = null;
        String timestamp = null;
        try {
            pagenum = request.getParameter("pagenum") == null ? 0 : Integer.parseInt(request.getParameter("pagenum"));
            pagesize = request.getParameter("pagesize") == null ? 10 : Integer.parseInt(request.getParameter("pagesize"));
            orders = super.getOrder(request.getParameterValues("sortdatafield"), request.getParameterValues("sortdatafield"));
            timestamp = request.getParameter("timestamp");
        } catch (NumberFormatException nfe) {
            try {
                throw new Exception("初始化 PagingHelper 对象失败 , 请检查 pagenum 或 pagesize 参数是否为正确数字 : " + nfe.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.init(pagenum, pagesize, orders, timestamp);
    }

    /**
     * 获得分页数据
     *
     * @param select      查询条件拓展
     * @param pageService 分页接口
     * @return
     */
    @Override
    public ReqPageHelper<T> paging(Select select, PagingService pageService) {
        super.paging(select, pageService);
        return this;
    }

    /**
     * 获得分页数据
     *
     * @param tClazz
     * @param select      SqlBean
     * @param pageService 分页接口
     * @return
     */
    @Override
    public ReqPageHelper<T> paging(Class<T> tClazz, Select select, PagingService pageService) {
        super.paging(tClazz, select, pageService);
        return this;
    }

    /**
     * 返回结果集
     *
     * @param message
     * @return
     */
    public PagingRS toResult(String message) {
        PagingRS result = new PagingRS();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(message == null || message.equals("") ? "获取列表成功" : message);
        ResultData<List<T>> resultData = super.getResultData();
        result.setData(resultData.getData());
        result.setPagenum(resultData.getPagenum());
        result.setPagesize(resultData.getPagesize());
        result.setTotalRecords(resultData.getTotalRecords());
        result.setTotalPage(resultData.getTotalPage());
        result.setTimestamp(resultData.getTimestamp());
        return result;
    }


}
