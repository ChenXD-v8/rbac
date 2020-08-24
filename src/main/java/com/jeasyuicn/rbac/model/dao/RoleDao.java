package com.jeasyuicn.rbac.model.dao;


import com.jeasyuicn.rbac.model.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends CrudRepository<Role,Long> {

    List<Role> findAllByEnable(boolean b);

    Role findFirstByRoleName(String roleName);
}
