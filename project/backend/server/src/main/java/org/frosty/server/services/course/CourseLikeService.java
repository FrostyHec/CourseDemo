package org.frosty.server.services.course;


public interface CourseLikeService{

        void createCourseLike(Long courseId, Long userId);

        void deleteCourseLike(Long courseId, Long userId);

        boolean checkCourseLike(Long courseId, Long userId);

}
