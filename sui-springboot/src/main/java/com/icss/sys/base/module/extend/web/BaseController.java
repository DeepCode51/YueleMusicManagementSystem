package com.icss.sys.base.module.extend.web;

import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.exception.AppException;
import com.icss.sys.base.exception.TokenException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.SessionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 基础控制器，其他控制器需集成此控制器获得initBinder自动转换的功能
 */
@Controller
public class BaseController {

    /**
     * 初始化数据绑定
     * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 2. 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder, HttpServletRequest request, HttpServletResponse response) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                //setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
                setValue(text == null ? null : text.trim());
            }

            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";  //将null 的转为"";
            }
        });
        // Date 类型转换 防止日期控件后台数据接收不了
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
        // Date 类型转换 防止日期控件后台数据接收不了
        binder.registerCustomEditor(Boolean.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 权限异常
     */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public ResultInfo authorizationException(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo resultInfo = ResultInfo.error("权限不足，请联系管理员！");
        resultInfo.setCode(401);//权限不足
        return resultInfo;
    }

    /**
     * 登录异常
     * @return
     */
    @ExceptionHandler({SessionException.class})
    public ResultInfo sessionException(SessionException sessionException) {
        ResultInfo resultInfo = ResultInfo.error("会话过期，请重新登录！");
        resultInfo.setCode(401);//会话过期
        return resultInfo;
    }

    /**
     * toke异常
     * @return
     */
    @ExceptionHandler({TokenException.class})
    public ResultInfo sessionException(TokenException tokenException) {
        ResultInfo resultInfo = ResultInfo.error("会话过期，请重新登录！");
        resultInfo.setCode(402);//toke过期
        return resultInfo;
    }
    /**
     * 其他异常
     * @return
     */
    @ExceptionHandler({AppException.class})
    public ResultInfo appException(AppException appException) {
        ResultInfo resultInfo = ResultInfo.error(appException.getMessage());
        return resultInfo;
    }

}
