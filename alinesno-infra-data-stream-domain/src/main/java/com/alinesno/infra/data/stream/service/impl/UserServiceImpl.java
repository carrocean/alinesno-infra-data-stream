package com.alinesno.infra.data.stream.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.stream.entity.UserEntity;
import com.alinesno.infra.data.stream.mapper.UserMapper;
import com.alinesno.infra.data.stream.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 用户Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class UserServiceImpl extends IBaseServiceImpl<UserEntity, UserMapper> implements IUserService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
}
