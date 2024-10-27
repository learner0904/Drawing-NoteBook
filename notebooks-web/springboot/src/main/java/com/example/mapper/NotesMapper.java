package com.example.mapper;

import com.example.entity.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 公告信息Mapper层
 **/
@Mapper
public interface NotesMapper {

    // 新增公告
    @Insert("INSERT INTO notes (comment,  date, code, picture, admin_id) " +
            "VALUES (#{comment},  #{date}, #{code}, #{picture}, #{adminId})")
    void insert(Notes notes);

    // 根据ID删除公告
    @Delete("DELETE FROM notes WHERE id = #{id}")
    void deleteById(Integer id);

    // 更新公告信息
    @Update("UPDATE notes SET comment = #{comment}, date = #{date}, " +
            "code = #{code}, picture = #{picture} , admin_id = #{adminId} WHERE id = #{id}")
    void updateById(Notes notes);

    // 根据ID查询公告
    @Select("SELECT * FROM notes WHERE id = #{id}")
    Notes selectById(Integer id);

    // 查询所有公告（可根据传入的字段作为过滤条件）
    @Select("<script>" +
            "SELECT * FROM notes" +
            "<where>" +
            "<if test='comment != null'>AND comment LIKE CONCAT('%', #{comment}, '%')</if>" +
            "<if test='code != null'>AND code LIKE CONCAT('%', #{code}, '%')</if>" +
            "</where>" +
            "</script>")
    List<Notes> selectAll(Notes notes);
}
