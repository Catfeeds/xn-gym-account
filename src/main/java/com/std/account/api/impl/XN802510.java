package com.std.account.api.impl;

import com.std.account.ao.IJourAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802510Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 外部账支付
 * @author: xieyj 
 * @since: 2016年12月23日 下午8:56:59 
 * @history:
 */
public class XN802510 extends AProcessor {
    private IJourAO jourAO = SpringContextHolder.getBean(IJourAO.class);

    private XN802510Req req = null;

    /** 
    * @see com.xnjr.base.api.IProcessor#doBusiness()
    */
    @Override
    public Object doBusiness() throws BizException {
        Long transAmount = StringValidater.toLong(req.getTransAmount());
        return jourAO.doChangeAmount(req.getAccountNumber(),
            req.getBankcardNumber(), transAmount, req.getBizType(),
            req.getBizNote(), req.getChannelTypeList(), req.getSystemCode());
    }

    /** 
    * @see com.xnjr.base.api.IProcessor#doCheck(java.lang.String)
    */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802510Req.class);
        StringValidater.validateBlank(req.getAccountNumber(),
            req.getBankcardNumber(), req.getBizType(), req.getBizNote(),
            req.getSystemCode());
        StringValidater.validateAmount(req.getTransAmount());
    }
}