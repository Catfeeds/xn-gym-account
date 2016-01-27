package com.std.account.api.impl;

import com.std.account.ao.IBankCardAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN801402Req;
import com.std.account.enums.EBankCardType;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 获取银行卡列表
 * @author: myb858 
 * @since: 2015年8月23日 下午2:45:57 
 * @history:
 */
public class XN801402 extends AProcessor {
    private IBankCardAO bankCardAO = SpringContextHolder
        .getBean(IBankCardAO.class);

    private XN801402Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        if (EBankCardType.User.getCode().equalsIgnoreCase(req.getType())) {
            return bankCardAO.queryBankCardList(req.getUserId(),
                EBankCardType.User);
        } else {
            return bankCardAO.queryBankCardList(req.getUserId(),
                EBankCardType.Company);
        }

    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN801402Req.class);
        StringValidater.validateBlank(req.getUserId(), req.getType());
    }
}
