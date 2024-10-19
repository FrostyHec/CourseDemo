package org.frosty.server.services.course.impl;

import lombok.RequiredArgsConstructor;
import org.frosty.server.services.course.CourseLikeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseLikeImpl implements CourseLikeService {
    // 点赞课程
    public void createCourseLike(Long courseId, Long userId) {
        // 逻辑处理

    }

    // 取消点赞课程
    public void deleteCourseLike(Long courseId, Long userId) {
        // 逻辑处理
    }

    // 检查用户是否点赞了课程
    public boolean checkCourseLike(Long courseId, Long userId) {
        // 逻辑处理
        return false;
    }
}
