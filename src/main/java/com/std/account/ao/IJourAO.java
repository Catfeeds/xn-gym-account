/**
 * @Title IJourAO.java 
 * @Package com.std.account.ao 
 * @Description 
 * @author xieyj  
 * @date 2016年12月23日 下午9:05:07 
 * @version V1.0   
 */
package com.std.account.ao;

import java.util.List;

import com.std.account.annotation.ServiceModule;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.Jour;

/** 
 * @author: xieyj 
 * @since: 2016年12月23日 下午9:05:07 
 * @history:
 */
@ServiceModule
public interface IJourAO {
    String DEFAULT_ORDER_COLUMN = "code";

    /**
     * 外部支付待回调
     * @param accountNumber
     * @param bankcardNumber
     * @param transAmount
     * @param bizType
     * @param bizNote
     * @param channelTypeList
     * @param systemCode 
     * @create: 2016年12月23日 下午9:09:43 xieyj
     * @history:
     */
    public String doChangeAmount(String accountNumber, String bankcardNumber,
            Long transAmount, String bizType, String bizNote,
            List<String> channelTypeList, String systemCode);

    /**
     * 回调支付
     * @param code
     * @param rollbackResult
     * @param rollbackUser
     * @param rollbackNote
     * @param systemCode 
     * @create: 2016年12月24日 上午8:21:37 xieyj
     * @history:
     */
    public void doCallBackChange(String code, String rollbackResult,
            String rollbackUser, String rollbackNote, String systemCode);

    /**
     * @param start
     * @param limit
     * @param condition
     * @return 
     * @create: 2016年12月24日 上午7:55:52 xieyj
     * @history:
     */
    public Paginable<Jour> queryJourPage(int start, int limit, Jour condition);

    /**
     * @param condition
     * @return 
     * @create: 2016年12月24日 上午7:55:59 xieyj
     * @history:
     */
    public List<Jour> queryJourList(Jour condition);

    /**
     * @param code
     * @param systemCode
     * @return 
     * @create: 2016年12月24日 上午8:20:47 xieyj
     * @history:
     */
    public Jour getJour(String code, String systemCode);

    /**
     * 人工单条调账
     * @param code
     * @param checkAmount
     * @param checkUser
     * @param checkNote
     * @param systemCode 
     * @create: 2016年12月25日 下午3:58:53 xieyj
     * @history:
     */
    public void checkJour(String code, String checkAmount, String checkUser,
            String checkNote, String systemCode);
}
