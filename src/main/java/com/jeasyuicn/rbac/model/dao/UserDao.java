package com.jeasyuicn.rbac.model.dao;

import com.jeasyuicn.rbac.model.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends PagingAndSortingRepository<User,Long> {

    /**
     *根据账号统计用户
     * @param account
     * @return
     */
    int countByAccount(String account);

    /**
     *根据账号查询用户信心
     * @param account
     * @return
     */
    User findFirstByAccount(String account);
}
