package com.icss.sys.base.module.mail.entity;

/**
 * Created by Administrator on 2019/9/29.
 */
public class Mail {
    private String mailUser;
    private String mailTitle;
    private String mailContent;

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }
}
