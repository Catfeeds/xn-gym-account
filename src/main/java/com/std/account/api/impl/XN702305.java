package com.std.account.api.impl;

import org.apache.commons.lang3.StringUtils;

import com.std.account.ao.IIdentityAO;
import com.std.account.api.AProcessor;
import com.std.account.common.DateUtil;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.domain.UserPicture;
import com.std.account.dto.req.XN702305Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 分页获取人工实名认证日志
 * @author: myb858 
 * @since: 2015年10月27日 下午4:19:08 
 * @history:
 */
public class XN702305 extends AProcessor {

    private IIdentityAO identityAO = SpringContextHolder
        .getBean(IIdentityAO.class);

    private XN702305Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        UserPicture condition = new UserPicture();
        condition.setId(StringValidater.toLong(req.getId()));
        condition.setUserId(req.getUserId());
        condition.setVerifyStatus(req.getVerifyStatus());
        condition.setVerifyUser(req.getVerifyUser());
        condition.setCreateDatetimeStart(DateUtil.getFrontDate(
            req.getDateStart(), false));
        condition.setCreateDatetimeEnd(DateUtil.getFrontDate(req.getDateEnd(),
            true));
        String column = req.getOrderColumn();
        if (StringUtils.isBlank(column)) {
            column = IIdentityAO.DEFAULT_ORDER_COLUMN;
        }
        condition.setOrder(column, req.getOrderDir());
        int start = Integer.valueOf(req.getStart());
        int limit = Integer.valueOf(req.getLimit());
        return identityAO.queryUserPicturePage(start, limit, condition);
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN702305Req.class);
        StringValidater.validateNumber(req.getStart(), req.getLimit());
    }

}
