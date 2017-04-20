package com.std.account.ao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IAccountAO;
import com.std.account.ao.IExchangeCurrencyAO;
import com.std.account.ao.IWeChatAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IExchangeCurrencyBO;
import com.std.account.bo.IUserBO;
import com.std.account.bo.base.Paginable;
import com.std.account.common.PropertiesUtil;
import com.std.account.domain.Account;
import com.std.account.domain.ExchangeCurrency;
import com.std.account.domain.User;
import com.std.account.enums.EBizType;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EExchangeCurrencyStatus;
import com.std.account.enums.EPayType;
import com.std.account.enums.ESystemCode;
import com.std.account.exception.BizException;
import com.std.account.util.AmountUtil;
import com.std.account.util.CalculationUtil;

@Service
public class ExchangeCurrencyAOImpl implements IExchangeCurrencyAO {

    @Autowired
    private IUserBO userBO;

    @Autowired
    private IAccountAO accountAO;

    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IExchangeCurrencyBO exchangeCurrencyBO;

    @Autowired
    IWeChatAO weChatAO;

    @Override
    @Transactional
    public Object payExchange(String fromUserId, String toUserId, Long amount,
            String currency, String payType) {
        User fromUser = userBO.getRemoteUser(fromUserId);
        // 获取微信公众号支付prepayid
        if (EPayType.WEIXIN_H5.getCode().equals(payType)) {
            return weixinH5Pay(fromUser, toUserId, amount, currency, payType);
        } else if (EPayType.WEIXIN_QR_CODE.getCode().equals(payType)) {
            return weixinQrCodePay(fromUser, toUserId, amount, currency,
                payType);
        } else {
            throw new BizException("XN000000", "现只支持微信H5和微信二维码，其他方式不支持");
        }
    }

    /**
     * 二维码扫描支付
     * @param fromUserId
     * @param toUserId
     * @param amount
     * @param currency
     * @param payType
     * @param systemCode
     * @return 
     * @create: 2017年4月20日 下午7:01:28 xieyj
     * @history:
     */
    private Object weixinQrCodePay(User fromUser, String toUserId, Long amount,
            String currency, String payType) {
        String bizType = null;
        String fromBizNote = null;
        String toBizNote = null;
        Long rmbAmount = 0L;
        if (ECurrency.CG_CGB.getCode().equals(currency)) {
            bizType = EBizType.AJ_CGBSM.getCode();
            fromBizNote = "菜狗币购买";
            toBizNote = "菜狗币售卖";
            rmbAmount = AmountUtil.mul(amount, 1 / exchangeCurrencyBO
                .getExchangeRate(ECurrency.CNY.getCode(), currency));
        } else {
            throw new BizException("xn000000", "暂未支持当前币种微信扫描支付");
        }
        String payGroup = exchangeCurrencyBO.payExchange(fromUser.getUserId(),
            toUserId, rmbAmount, amount, currency, payType,
            fromUser.getSystemCode());
        return weChatAO.getPrepayIdNative(fromUser.getUserId(), toUserId,
            bizType, fromBizNote, toBizNote, amount, payGroup);
    }

    /** 
     * 微信H5支付
     * @param user
     * @param amount
     * @param currency
     * @param payType
     * @return 
     * @create: 2017年4月20日 下午6:02:46 xieyj
     * @history: 
     */
    private Object weixinH5Pay(User fromUser, String toUser, Long amount,
            String currency, String payType) {
        Long rmbAmount = AmountUtil.mul(amount, 1 / exchangeCurrencyBO
            .getExchangeRate(ECurrency.CNY.getCode(), currency));
        rmbAmount = AmountUtil.rmbJinFen(rmbAmount);

        String payGroup = exchangeCurrencyBO.payExchange(fromUser.getUserId(),
            toUser, rmbAmount, amount, currency, payType,
            fromUser.getSystemCode());

        return weChatAO.getPrepayIdH5(fromUser.getUserId(),
            fromUser.getOpenId(), toUser, EBizType.EXCHANGE_CURRENCY.getCode(),
            EBizType.EXCHANGE_CURRENCY.getValue(),
            EBizType.EXCHANGE_CURRENCY.getValue(), rmbAmount, payGroup,
            PropertiesUtil.Config.SELF_PAY_BACKURL);
    }

    @Override
    @Transactional
    public void paySuccess(String payGroup, String payCode, Long transAmount) {
        List<ExchangeCurrency> resultList = exchangeCurrencyBO
            .queryExchangeCurrencyList(payGroup);
        if (CollectionUtils.isEmpty(resultList)) {
            throw new BizException("XN000000", "找不到对应的兑换记录");
        }
        ExchangeCurrency exchangeCurrency = resultList.get(0);
        if (!transAmount.equals(exchangeCurrency.getFromAmount())) {
            throw new BizException("XN000000", "金额校验错误，非正常调用");
        }
        // 更新状态
        exchangeCurrencyBO.paySuccess(exchangeCurrency.getCode(),
            EExchangeCurrencyStatus.PAYED.getCode(), payCode, transAmount);
        // 去方币种兑换
        accountAO.transAmountCZB(exchangeCurrency.getToUserId(),
            exchangeCurrency.getFromUserId(), exchangeCurrency.getToCurrency(),
            transAmount, EBizType.EXCHANGE_CURRENCY.getCode(), "币种兑换", "币种兑换");
    }

    @Override
    public Paginable<ExchangeCurrency> queryExchangeCurrencyPage(int start,
            int limit, ExchangeCurrency condition) {
        Paginable<ExchangeCurrency> page = exchangeCurrencyBO.getPaginable(
            start, limit, condition);
        if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
            for (ExchangeCurrency exchangeCurrency : page.getList()) {
                User fromUser = userBO.getRemoteUser(exchangeCurrency
                    .getFromUserId());
                exchangeCurrency.setFromUser(fromUser);
                User toUser = userBO.getRemoteUser(exchangeCurrency
                    .getToUserId());
                exchangeCurrency.setToUser(toUser);
            }
        }
        return page;
    }

    @Override
    public ExchangeCurrency getExchangeCurrency(String code) {
        ExchangeCurrency exchangeCurrency = exchangeCurrencyBO
            .getExchangeCurrency(code);
        User fromUser = userBO.getRemoteUser(exchangeCurrency.getFromUserId());
        exchangeCurrency.setFromUser(fromUser);
        return exchangeCurrency;
    }

    @Override
    public Double getExchangeRate(String fromCurrency, String toCurrency) {
        return exchangeCurrencyBO.getExchangeRate(fromCurrency, toCurrency);
    }

    @Override
    @Transactional
    public String doExchange(String userId, Long fromAmount,
            String fromCurrency, String toCurrency) {
        User user = userBO.getRemoteUser(userId);
        ExchangeCurrency dbOrder = exchangeCurrencyBO.doExchange(user,
            fromAmount, fromCurrency, toCurrency);
        // 开始资金划转
        String remark = CalculationUtil.divi(fromAmount)
                + ECurrency.getCurrencyMap().get(fromCurrency).getValue()
                + "转化为" + CalculationUtil.divi(dbOrder.getToAmount())
                + ECurrency.getCurrencyMap().get(toCurrency).getValue();
        Account fromAccount = accountBO.getAccountByUser(
            dbOrder.getFromUserId(), dbOrder.getFromCurrency());
        Account toAccount = accountBO.getAccountByUser(dbOrder.getToUserId(),
            dbOrder.getToCurrency());
        accountBO.transAmount(fromAccount.getSystemCode(),
            fromAccount.getAccountNumber(), EChannelType.NBZ, null,
            -dbOrder.getFromAmount(), EBizType.EXCHANGE_CURRENCY.getCode(),
            remark);
        accountBO
            .transAmount(toAccount.getSystemCode(),
                toAccount.getAccountNumber(), EChannelType.NBZ, null,
                dbOrder.getToAmount(), EBizType.EXCHANGE_CURRENCY.getCode(),
                remark);
        return dbOrder.getCode();
    }

    @Override
    public String applyExchange(String userId, Long fromAmount,
            String fromCurrency, String toCurrency) {
        User user = userBO.getRemoteUser(userId);
        // 判断每月次数是否超限制
        if (ESystemCode.ZHPAY.getCode().equals(user.getSystemCode())) {
            exchangeCurrencyBO.doCheckMonthTimes(userId, fromCurrency);
        }
        return exchangeCurrencyBO.applyExchange(user, fromAmount, fromCurrency,
            toCurrency);
    }

    @Override
    public void approveExchange(String code, String approveResult,
            String approver, String approveNote) {
        ExchangeCurrency dbOrder = exchangeCurrencyBO.getExchangeCurrency(code);
        if (EExchangeCurrencyStatus.TO_PAY.getCode()
            .equals(dbOrder.getStatus())) {
            if (EBoolean.YES.getCode().equals(approveResult)) {
                exchangeCurrencyBO.approveExchangeYes(dbOrder, approver,
                    approveNote);
                // 开始资金划转
                String remark = CalculationUtil.divi(dbOrder.getFromAmount())
                        + dbOrder.getFromCurrency() + "虚拟币转化为"
                        + CalculationUtil.divi(dbOrder.getToAmount())
                        + dbOrder.getToCurrency();
                Account fromAccount = accountBO.getAccountByUser(
                    dbOrder.getFromUserId(), dbOrder.getFromCurrency());
                Account toAccount = accountBO.getAccountByUser(
                    dbOrder.getToUserId(), dbOrder.getToCurrency());
                accountBO.transAmount(fromAccount.getSystemCode(),
                    fromAccount.getAccountNumber(), EChannelType.NBZ, null,
                    -dbOrder.getFromAmount(),
                    EBizType.EXCHANGE_CURRENCY.getCode(), remark);
                accountBO.transAmount(toAccount.getSystemCode(),
                    toAccount.getAccountNumber(), EChannelType.NBZ, null,
                    dbOrder.getToAmount(),
                    EBizType.EXCHANGE_CURRENCY.getCode(), remark);
            } else {
                exchangeCurrencyBO.approveExchangeNo(dbOrder, approver,
                    approveNote);
            }
        } else {
            throw new BizException("xn000000", code + "不处于待审批状态");
        }

    }
}
