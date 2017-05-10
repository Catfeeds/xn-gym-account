package com.std.account.ao;

import java.util.List;

import com.std.account.bo.base.Paginable;
import com.std.account.domain.Account;

public interface IAccountAO {
    String DEFAULT_ORDER_COLUMN = "account_number";

    /**
     * 个人创建多账户
     * @param userId
     * @param realName
     * @param accountType
     * @param currency
     * @param systemCode
     * @return 
     * @create: 2016年12月23日 下午7:52:47 xieyj
     * @history:
     */
    public void distributeAccount(String userId, String realName,
            String accountType, List<String> currencyList, String systemCode);

    /**
     * 更新户名
     * @param userId
     * @param realName
     * @param systemCode 
     * @create: 2017年1月4日 上午11:41:02 xieyj
     * @history:
     */
    public void editAccountName(String userId, String realName,
            String systemCode);

    // 同样币种不同用户间资金划转
    void transAmountCZB(String fromUserId, String toUserId, String currency,
            Long transAmount, String bizType, String fromBizNote,
            String toBizNote);

    /**
     * 分页查询账户
     * @param start
     * @param limit
     * @param condition
     * @return 
     * @create: 2015年6月26日 下午4:04:11 myb858
     * @history:
     */
    public Paginable<Account> queryAccountPage(int start, int limit,
            Account condition);

    /**
     * 列表查询账户
     * @param condition
     * @return 
     * @create: 2015年6月26日 下午4:04:11 myb858
     * @history:
     */
    public List<Account> queryAccountList(Account condition);

    /**
     * 根据accountNumber查询账户
     * @param accountNumber
     * @return 
     * @create: 2016年12月23日 下午6:48:33 xieyj
     * @history:
     */
    public Account getAccount(String accountNumber);

    /**
     * 根据用户编号,币种获取账户列表
     * @param userId
     * @param currency
     * @return 
     * @create: 2016年12月28日 下午2:21:47 xieyj
     * @history:
     */
    public List<Account> getAccountByUserId(String userId, String currency);

    public void transAmount(String accountNumber, String channelType,
            String channelOrder, String transAmount, String bizType,
            String bizNote);
}
