package com.zrh.enhance.plugin.web.dao;

import com.zrh.enhance.plugin.web.model.PluginInfo;

import java.util.List;

public interface PluginInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plugin_info
     *
     * @mbggenerated Sat Nov 09 09:14:51 CST 2019
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plugin_info
     *
     * @mbggenerated Sat Nov 09 09:14:51 CST 2019
     */
    int insert(PluginInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plugin_info
     *
     * @mbggenerated Sat Nov 09 09:14:51 CST 2019
     */
    int insertSelective(PluginInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plugin_info
     *
     * @mbggenerated Sat Nov 09 09:14:51 CST 2019
     */
    PluginInfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plugin_info
     *
     * @mbggenerated Sat Nov 09 09:14:51 CST 2019
     */
    int updateByPrimaryKeySelective(PluginInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table plugin_info
     *
     * @mbggenerated Sat Nov 09 09:14:51 CST 2019
     */
    int updateByPrimaryKey(PluginInfo record);

    List<PluginInfo> selectList();
}