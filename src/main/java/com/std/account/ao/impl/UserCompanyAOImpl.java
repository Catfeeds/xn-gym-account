package com.std.account.ao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.std.account.ao.IUserCompanyAO;
import com.std.account.bo.ICompanyBO;
import com.std.account.bo.IUserBO;
import com.std.account.bo.IUserCompanyBO;
import com.std.account.domain.Company;
import com.std.account.domain.User;
import com.std.account.exception.BizException;

/**
 * 用户企业关联表
 * @author: wuql
 * @since: 2016年1月15日 上午11:39:56 
 * @history:
 */
@Service
public class UserCompanyAOImpl implements IUserCompanyAO {
    @Autowired
    IUserCompanyBO userCompanyBO;

    @Autowired
    IUserBO userBO;

    @Autowired
    ICompanyBO companyBO;

    public List<Company> queryUserCompanyListByUserId(String userId) {
        return userCompanyBO.queryCompanyList(userId);

    }

    @Override
    public void doBindUserCompany(String userId, String companyId,
            String remark) {
        User user = userBO.getUser(userId);
        if (user == null) {
            throw new BizException("xn000001", "该用户不存在");
        }
        if (!companyBO.isCompanyExist(companyId)) {
            throw new BizException("xn000001", "该公司不存在");
        }
        if (userCompanyBO.isUserCompanyExist(userId, companyId)) {
            throw new BizException("xn000001", "该用户已与该公司关联！");
        }
        userCompanyBO.saveUserCompany(userId, companyId, remark);
    }

    @Override
    public void doUnbindUserCompany(String userId, String companyId) {
        if (userCompanyBO.isUserCompanyExist(userId, companyId)) {
            userCompanyBO.removeUserCompany(userId, companyId);
        } else {
            throw new BizException("xn000001", "该用户公司关联不存在！");
        }
    }
}
