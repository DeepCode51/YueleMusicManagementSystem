package com.icss.sys.base.module.mail.web;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.mail.entity.Mail;
import com.icss.sys.base.module.mail.entity.MailSend;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import static com.icss.sys.base.module.config.service.SysConfigService.getSysConfig;


@Controller
@RequestMapping("/admin/sys/mail")
public class SysMailController extends BaseController {


    @RequestMapping("/manage")
    public ModelAndView list(ModelAndView modelAndView, Mail mail){
        modelAndView.setViewName("module/mail/mailManage");
        return modelAndView;
    }


    @RequestMapping("/send")
    @ResponseBody
    public ResultInfo send(Mail mail){
        String mailSmtp = getSysConfig("mailSmtp");
        String mailPort = getSysConfig("mailPort");
        String mailName = getSysConfig("mailName");
        String mailPassword = getSysConfig("mailPassword");
        try {
            boolean send = MailSend.sendEmail(mailSmtp, mailPort, mailName, mailPassword, mail.getMailUser(), mail.getMailTitle(), mail.getMailContent(), "0");
            if(send){
                return ResultInfo.ok("邮件发送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultInfo.error("邮件发送失败");
    }


}

