package com.ccl.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ccl.entity.wexin.TWxUserInfo;
import com.ccl.entity.wexin.TWxUserInfoVo;

/**
 * 微信用户信息表 Mapper 接口
 *
 * @author liuc
 * @since 2021-09-03
 */
public interface TWxUserInfoMapper extends BaseMapper<TWxUserInfo> {

    TWxUserInfoVo selectWxUserInfoByUnionId(String unionId);

    Integer insertWxUserInfo(TWxUserInfoVo wxUserInfoVo);

    Integer updWxUserInfoByUnionId(TWxUserInfoVo wxUserInfoVo);

    TWxUserInfoVo selectWxUserInfoByMobile(String mobile);

    Integer updWxUserInfoByPublicOpenid(TWxUserInfoVo wxUserInfoVo);

}
