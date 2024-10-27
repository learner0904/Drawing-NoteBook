package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notes;
import com.example.service.NotesService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/notes")
public class NotesController {

    @Resource
    private NotesService notesService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Notes notes) {
        notesService.add(notes);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        notesService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        notesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Notes notes) {
        notesService.updateById(notes);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Notes notes = notesService.selectById(id);
        return Result.success(notes);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Notes notes) {
        List<Notes> list = notesService.selectAll(notes);
        return Result.success(list);
    }


    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Notes notes,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Notes> page = notesService.selectPage(notes, pageNum, pageSize);
        return Result.success(page);
    }

}