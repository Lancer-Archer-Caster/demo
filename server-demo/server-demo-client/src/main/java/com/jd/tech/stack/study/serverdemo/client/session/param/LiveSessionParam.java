package com.jd.tech.stack.study.serverdemo.client.session.param;

/**
 * Description: 直播场次查询参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class LiveSessionParam {

    /** 场次标题 */
    private String title;

    /** 操作人 */
    private String operator;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}