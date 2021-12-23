package cn.vonce.sql.spring.page;

import cn.vonce.common.bean.RS;
import cn.vonce.common.enumerate.ResultCode;
import cn.vonce.sql.bean.Order;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.page.PageHelper;
import cn.vonce.sql.page.PagingService;
import cn.vonce.sql.page.ResultData;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String[] sortdatafield = null;
        String[] sortorder = null;
        Order[] orders = null;
        String timestamp = null;
        boolean startByZero = true;
        try {
            //当前页数
            if (request.getParameter("page") != null) {
                pagenum = Integer.parseInt(request.getParameter("page"));
                startByZero = false;
            } else if (request.getParameter("pagenum") != null) {
                pagenum = Integer.parseInt(request.getParameter("pagenum"));
            }
            //每页数量
            if (request.getParameter("pageSize") != null) {
                pagesize = Integer.parseInt(request.getParameter("pageSize"));
            } else if (request.getParameter("pagesize") != null) {
                pagesize = Integer.parseInt(request.getParameter("pagesize"));
            }
            //排序字段
            if (request.getParameterValues("prop") != null) {
                sortdatafield = request.getParameterValues("prop");
            } else if (request.getParameter("sortdatafield") != null) {
                sortdatafield = request.getParameterValues("sortdatafield");
            }
            //排序类型
            if (request.getParameterValues("order") != null) {
                sortorder = request.getParameterValues("order");
            } else if (request.getParameter("sortorder") != null) {
                sortorder = request.getParameterValues("sortorder");
            }
            timestamp = request.getParameter("timestamp");
            orders = super.getOrder(sortdatafield, sortorder);
        } catch (NumberFormatException nfe) {
            try {
                throw new Exception("初始化 PagingHelper 对象失败 , 请检查 pagenum(page) 和 pagesize(pageSize) 参数是否为正确数字 : " + nfe.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.init(pagenum, pagesize, startByZero, orders, timestamp);
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
     * @param msg
     * @return
     */
    public RS toResult(String msg) {
        ResultData<List<T>> resultData = super.getResultData();
        RS result = new RS();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(msg == null || msg.equals("") ? "获取列表成功" : msg);
        result.setData(resultData.getData());
        result.put("pagenum", resultData.getPagenum());
        result.put("pagesize", resultData.getPagesize());
        result.put("totalRecords", resultData.getTotalRecords());
        result.put("totalPage", resultData.getTotalPage());
        result.put("timestamp", resultData.getTimestamp());
        result.put("startByZero", super.getStartByZero());
        return result;
    }

    /**
     * 返回结果集
     *
     * @return
     */
    public RS toResult() {
        return toResult(null);
    }

    /**
     * 返回结果集
     *
     * @param msg
     * @return
     */
    public RS result(String msg) {
        ResultData<List<T>> resultData = super.getResultData();
        RS result = new RS();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(msg == null || msg.equals("") ? "获取列表成功" : msg);
        Map<String, Object> data = new HashMap<>();
        data.put("page", resultData.getPagenum());
        data.put("pageSize", resultData.getPagesize());
        data.put("total", resultData.getTotalRecords());
        data.put("rows", resultData.getData());
        data.put("timestamp", resultData.getTimestamp());
        data.put("startByZero", super.getStartByZero());
        result.setData(data);
        return result;
    }

    /**
     * 返回结果集
     *
     * @return
     */
    public RS result() {
        return result(null);
    }


}
