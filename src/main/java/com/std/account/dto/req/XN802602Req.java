package com.std.account.dto.req;

public class XN802602Req {
    // 用户账号
    private String accountNumber;

    // 取现金额（精确到厘）
    private String amount;

    // 行别（alipay支付宝）
    private String bankCode;

    // 开户支行
    private String subbranch;

    // 行别对应的卡号
    private String bankcardNo;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSubbranch() {
        return subbranch;
    }

    public void setSubbranch(String subbranch) {
        this.subbranch = subbranch;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

}