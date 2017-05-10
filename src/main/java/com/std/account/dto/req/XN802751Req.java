package com.std.account.dto.req;

import java.util.List;

public class XN802751Req {
    // 系统编号(必填)
    private String systemCode;

    // 取现订单编号(必填)
    private List<String> codeList;

    // 审核人(必填)
    private String approveUser;

    // 审核结果(必填)
    private String approveResult;

    // 审核说明(必填)
    private String approveNote;

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(String approveResult) {
        this.approveResult = approveResult;
    }

    public String getApproveNote() {
        return approveNote;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

}
