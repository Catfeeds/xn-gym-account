package com.std.account.api.impl;

import com.std.account.ao.IUserAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.domain.User;
import com.std.account.dto.req.XN702200Req;
import com.std.account.dto.res.XN702200Res;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 根据userId获取用户信息
 * @author: myb858 
 * @since: 2015年8月23日 下午1:48:57 
 * @history:
 */
public class XN702200 extends AProcessor {
    private IUserAO userAO = SpringContextHolder.getBean(IUserAO.class);

    private XN702200Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        User user = userAO.doGetUser(req.getUserId());
        XN702200Res res = new XN702200Res();
        if (user != null) {
            res.setUserId(user.getUserId());
            res.setMobile(user.getMobile());
            res.setLoginPwdStrength(user.getLoginPwdStrength());
            res.setUserKind(user.getUserKind());
            res.setUserReferee(user.getUserReferee());
            res.setIdKind(user.getIdKind());
            res.setIdNo(user.getIdNo());
            res.setRealName(user.getRealName());
            res.setTradePwdStrength(user.getTradePwdStrength());
            res.setCreateDatetime(user.getCreateDatetime());
            res.setRemark(user.getRemark());
            res.setStatus(user.getStatus());
        }
        return res;
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN702200Req.class);
        StringValidater.validateBlank(req.getUserId());
    }

}
