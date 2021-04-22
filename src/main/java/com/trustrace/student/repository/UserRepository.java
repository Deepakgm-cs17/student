package com.trustrace.student.repository;

import com.trustrace.student.model.UserModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{
    @Query("select s from UserModel s where username like %?1%")
    Page<UserModel> findByName(String name, Pageable pageable);

    @Modifying
    @Query("UPDATE UserModel s SET username=?1 where userId=?2")
    @Transactional
    public void findUserReplace(String value,Long id);

    @Modifying
    @Query("UPDATE UserModel s SET email=?1 where userId=?2")
    @Transactional
    public void findEmailReplace(String value,Long id);

    @Modifying
    @Query("UPDATE UserModel s SET email=null where userId=?1")
    @Transactional
    public void deleteEmail(Long id);

    @Modifying
    @Query("UPDATE UserModel s SET username=null where userId=?1")
    @Transactional
    public void deleteUsername(Long id);
}