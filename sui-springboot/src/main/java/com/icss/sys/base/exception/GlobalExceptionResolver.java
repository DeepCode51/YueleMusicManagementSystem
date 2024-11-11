package com.icss.sys.base.exception;

import com.icss.sys.base.entity.ResultInfo;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        try {
            httpServletResponse.setContentType("application/json");
             //设置字符编码使用的码表
            httpServletResponse.setCharacterEncoding("utf-8");
             //通知浏览器使用utf-8解码
            httpServletResponse.setHeader("Content-Type","text/html;charset=utf-8");
            PrintWriter writer = httpServletResponse.getWriter();
            if (e != null) {
                if (e.getCause() != null && e.getCause().getMessage() != null) {
                    JSONObject text = JSONObject.fromObject(ResultInfo.error("操作失败！" + e.getCause().getMessage()));
                    writer.write(text.toString());
                } else if (e.getMessage() != null) {
                    JSONObject text = JSONObject.fromObject(ResultInfo.error("操作失败！" + e.getMessage()));
                    writer.write(text.toString());
                } else {
                    JSONObject text = JSONObject.fromObject(ResultInfo.error("操作失败！"+ e.toString()));
                    writer.write(text.toString());
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return super.resolveException(httpServletRequest, httpServletResponse, o, e);
    }
}