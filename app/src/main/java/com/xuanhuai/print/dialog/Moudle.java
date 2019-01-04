package com.xuanhuai.print.dialog;

public class Moudle {

    private String ck_select;
    private String tv_order_num;
    private String tv_odd_num;
    private String tv_complete_rate;
    private String tv_urgen_sign;
    private String tv_project_time;
    private String  iv_print;

    public Moudle(String ck_select, String tv_order_num, String tv_odd_num, String tv_complete_rate, String tv_urgen_sign, String tv_project_time, String iv_print) {
        this.ck_select = ck_select;
        this.tv_order_num = tv_order_num;
        this.tv_odd_num = tv_odd_num;
        this.tv_complete_rate = tv_complete_rate;
        this.tv_urgen_sign = tv_urgen_sign;
        this.tv_project_time = tv_project_time;
        this.iv_print = iv_print;
    }

    @Override
    public String toString() {
        return "Moudle{" +
                "ck_select='" + ck_select + '\'' +
                ", tv_order_num='" + tv_order_num + '\'' +
                ", tv_odd_num='" + tv_odd_num + '\'' +
                ", tv_complete_rate='" + tv_complete_rate + '\'' +
                ", tv_urgen_sign='" + tv_urgen_sign + '\'' +
                ", tv_project_time='" + tv_project_time + '\'' +
                ", iv_print='" + iv_print + '\'' +
                '}';
    }

    public String getCk_select() {
        return ck_select;
    }

    public void setCk_select(String ck_select) {
        this.ck_select = ck_select;
    }

    public String getTv_order_num() {
        return tv_order_num;
    }

    public void setTv_order_num(String tv_order_num) {
        this.tv_order_num = tv_order_num;
    }

    public String getTv_odd_num() {
        return tv_odd_num;
    }

    public void setTv_odd_num(String tv_odd_num) {
        this.tv_odd_num = tv_odd_num;
    }

    public String getTv_complete_rate() {
        return tv_complete_rate;
    }

    public void setTv_complete_rate(String tv_complete_rate) {
        this.tv_complete_rate = tv_complete_rate;
    }

    public String getTv_urgen_sign() {
        return tv_urgen_sign;
    }

    public void setTv_urgen_sign(String tv_urgen_sign) {
        this.tv_urgen_sign = tv_urgen_sign;
    }

    public String getTv_project_time() {
        return tv_project_time;
    }

    public void setTv_project_time(String tv_project_time) {
        this.tv_project_time = tv_project_time;
    }

    public String getIv_print() {
        return iv_print;
    }

    public void setIv_print(String iv_print) {
        this.iv_print = iv_print;
    }
}
