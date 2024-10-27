package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Notes;
import com.example.mapper.NotesMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class NotesService {

    @Resource
    private NotesMapper notesMapper;

    /**
     * 新增
     */
    public void add(Notes notes) {
        notes.setDate(DateUtil.today());
        Account currentUser = TokenUtils.getCurrentUser();
        notes.setAdminId(currentUser.getId());
        notesMapper.insert(notes);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        notesMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            notesMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Notes notes) {
        notes.setDate(DateUtil.today());
        Account currentUser = TokenUtils.getCurrentUser();
        notes.setAdminId(currentUser.getId());
        notesMapper.updateById(notes);
    }

    /**
     * 根据ID查询
     */
    public Notes selectById(Integer id) {
        return notesMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Notes> selectAll(Notes notes) {
        return notesMapper.selectAll(notes);
    }

    /**
     * 分页查询
     */
    public PageInfo<Notes> selectPage(Notes notes, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Notes> list = notesMapper.selectAll(notes);
        return PageInfo.of(list);
    }

}