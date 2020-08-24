package com.jeasyuicn.rbac.model.dao;

import com.jeasyuicn.rbac.model.entity.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionDao extends CrudRepository<Permission,Long> {
    /**
     * 获取根节点
     * @return
     */
    List<Permission> findAllByParentIsNull();

    /**
     * 根据父节点找子节点
     * @param parent
     * @return
     */
    List<Permission> findAllByParent(Permission parent);

    Set<Permission> findAllByEnableOrderByWeightDesc(boolean enable);
}
