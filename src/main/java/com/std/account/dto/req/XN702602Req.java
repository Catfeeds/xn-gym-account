package com.std.account.dto.req;

public class XN702602Req extends APageReq {
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */
    private static final long serialVersionUID = 1612316828815612651L;

    // 账号(必传字段，因为只能查某个账号的流水)
    private String accountNumber;

    // 流水号（Long）
    private String afjNo;

    // 业务类型（0虚拟币兑换;1充值-1取现;2转入-2转出;9蓝补-9红冲）
    private String bizType;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAfjNo() {
        return afjNo;
    }

    public void setAfjNo(String afjNo) {
        this.afjNo = afjNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

}
